const baseUrl = window.location.origin + "/api/notifications/by-user/"; // baseUrl 정의
const token = localStorage.getItem("authToken"); // 또는 쿠키/다른 저장소에서 불러오기
const userUno = localStorage.getItem("userUno");

let currentPage = 0;
const pageSize = 10;

function loadNotifications(page = 0) {
    currentPage = page;

    if (!baseUrl) {
        console.warn("알림을 불러올 수 없습니다. baseUrl이 정의되지 않았습니다.");
        return;
    }

    if (!userUno) {
        console.warn("로그인한 유저 UNO가 없습니다.");
        return;
    }

    const url = `${baseUrl}${userUno}?page=${page}&size=${pageSize}`;

    fetch(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('notificationList');
            container.innerHTML = '';

            const notifications = data.content || data;

            console.log("알림 응답 데이터:", data);

            if (!Array.isArray(notifications)) {
                container.innerHTML = '<p>알림 데이터가 잘못되었습니다.</p>';
                return;
            }

            if (notifications.length === 0) {
                container.innerHTML = '<p>알림이 없습니다.</p>';
                return;
            }

            notifications.forEach(noti => {
                const div = document.createElement('div');
                div.className = `notification ${noti.read ? 'read' : 'unread'}`;
                div.innerHTML = `
                    <div class="meta">${noti.createdAt} · ${noti.senderType} (${noti.senderName})</div>
                    <div class="title">${noti.title}</div>
                    <div class="content">${noti.content}</div>
                    <div class="actions">
                        ${!noti.read ? `<button onclick="markAsRead(${noti.id})">읽음</button>` : ''}
                        <button onclick="deleteNotification(${noti.id})">삭제</button>
                    </div>
                `;
                container.appendChild(div);
            });

            if (data.totalPages !== undefined) {
                renderPagination(data.totalPages, data.number); // totalPages, currentPage
            }
        })
        .catch(err => {
            console.error("알림 불러오기 실패", err);
            document.getElementById('notificationList').innerHTML = '<p>알림을 불러올 수 없습니다.</p>';
        });
}

function markAsRead(id) {
    fetch(`/api/notifications/${id}/read`, {
        method: 'PUT',
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(() => loadNotifications())
        .catch(err => console.error("읽음 처리 실패", err));
}

function renderPagination(totalPages, currentPage) {
    const paginationContainer = document.getElementById('notification-pagination');
    paginationContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const btn = document.createElement('button');
        btn.textContent = i + 1;
        btn.className = i === currentPage ? 'active-page' : '';
        btn.onclick = () => loadNotifications(i);
        paginationContainer.appendChild(btn);
    }
}

function deleteNotification(id) {
    if (!confirm("이 알림을 삭제하시겠습니까?")) return;

    fetch(`/api/notifications/${id}`, {
        method: 'DELETE',
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(() => loadNotifications())
        .catch(err => console.error("삭제 실패", err));
}