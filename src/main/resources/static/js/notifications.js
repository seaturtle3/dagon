let baseUrl = '';  // baseUrl을 전역 변수로 정의
const token = localStorage.getItem("authToken");

if (token) {
    const decodedToken = JSON.parse(atob(token.split('.')[1])); // JWT 디코딩
    const receiverId = decodedToken.uno; // 'uno' 필드를 사용

    if (receiverId) {
        baseUrl = '/api/notifications/user/' + receiverId;
    }
}

// 알림 목록 불러오기
function loadNotifications() {
    if (!baseUrl) {
        console.warn("알림을 불러올 수 없습니다. baseUrl이 정의되지 않았습니다.");
        return;
    }

    fetch(baseUrl, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('notificationList');
            container.innerHTML = '';

            if (!Array.isArray(data)) {
                container.innerHTML = '<p>알림 데이터가 잘못되었습니다.</p>';
                return;
            }

            if (data.length === 0) {
                container.innerHTML = '<p>알림이 없습니다.</p>';
                return;
            }

            data.forEach(noti => {
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
        })
        .catch(err => {
            console.error("알림 불러오기 실패", err);
            document.getElementById('notificationList').innerHTML = '<p>알림을 불러올 수 없습니다.</p>';
        });
}

// 읽음 처리
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

// 삭제 처리
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
