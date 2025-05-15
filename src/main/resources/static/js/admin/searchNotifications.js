let currentUid = '';
let currentType = '';
let currentSize = 10;

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('search-container').innerHTML = `
        <div class="flex items-center gap-2 mb-4">
            <input type="text" id="search-user-uid" class="border p-2 rounded" placeholder="유저 아이디">
            <select id="search-type" class="border p-2 rounded">
                <option value="">전체 유형</option>
                <option value="NOTICE">공지사항</option>
                <option value="REPLY">답변</option>
                <option value="RESERVATION">예약 알림</option>
            </select>
            <button id="search-btns" class="bg-blue-500 text-white px-4 py-2 rounded">검색</button>
        </div>
    `;

    document.getElementById('search-btns').addEventListener('click', () => {
        currentUid = document.getElementById('search-user-uid').value.trim();
        currentType = document.getElementById('search-type').value;
        searchNotifications(0, currentSize);
    });

    searchNotifications(0, currentSize);
});

function renderPagination(data, currentPage, pageSize) {
    console.log('renderPagination 시작');
    const paginationDiv = document.getElementById('notification-pagination');
    if (!paginationDiv) {
        console.error('notification-pagination 요소를 찾을 수 없음');
        return;
    }

    // 기존 내용 삭제
    paginationDiv.innerHTML = '';

    const totalPages = data.totalPages;
    console.log('총 페이지 수:', totalPages);

    if (totalPages <= 1) {
        console.log('페이지 1 이하, 페이지네이션 표시 안 함');
        return; // 페이지가 1 이하라면 페이지네이션 필요 없음
    }

    for (let i = 0; i < totalPages; i++) {
        const btn = document.createElement('button');
        btn.textContent = i + 1;
        btn.classList.add('page-btn');
        if (i === currentPage) btn.classList.add('active');

        btn.addEventListener('click', () => {
            searchNotifications(i, pageSize);
        });

        paginationDiv.appendChild(btn);
    }
    console.log('renderPagination 종료');
}

function searchNotifications(page = 0, size = 10) {
    const authToken = localStorage.getItem('authToken');

    const params = new URLSearchParams({
        uid: currentUid,
        type: currentType,
        page: page,
        size: size
    });

    fetch(`/api/notifications?${params.toString()}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${authToken}`
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('전체 응답 데이터:', data);
            console.log('data.number:', data.number);
            console.log('data.size:', data.size);
            console.log('data.totalPages:', data.totalPages);


            const tableBody = document.getElementById('notification-table-body');
            tableBody.innerHTML = '';

            if (!data.content || data.content.length === 0) {
                const noDataRow = document.createElement('tr');
                noDataRow.innerHTML = `<td colspan="7" class="text-center text-gray-500">검색된 알림이 없습니다.</td>`;
                tableBody.appendChild(noDataRow);
            } else {
                data.content.forEach(notification => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                <td>${notification.receiverUid || ''}</td>
                <td>${notification.title || ''}</td>
                <td>${notification.content || ''}</td>
                <td>${notification.senderName || ''}</td>
                <td>${notification.type || ''}</td>
                <td>${notification.createdAt ? new Date(notification.createdAt).toLocaleString() : ''}</td>
                <td>${notification.isRead ? '읽음' : '안 읽음'}</td>
            `;
                    tableBody.appendChild(row);
                });
            }
            console.log("renderPagination 호출 직전");
            renderPagination(data, data.number, data.size);  // 한 번만 호출
        })
        .catch(error => {
            console.error('알림 검색 오류:', error);
        });
}

