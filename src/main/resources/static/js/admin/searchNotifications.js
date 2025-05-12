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
            <button id="search-btn" class="bg-blue-500 text-white px-4 py-2 rounded">검색</button>
        </div>
    `;

    document.getElementById('search-btn').addEventListener('click', () => {
        currentUid = document.getElementById('search-user-uid').value.trim();
        currentType = document.getElementById('search-type').value;
        searchNotifications(0, currentSize);
    });

    searchNotifications(0, currentSize);
});

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
            console.log('알림 데이터:', data);

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

            renderPagination(data, data.number, data.size);  // 한 번만 호출
        })
        .catch(error => {
            console.error('알림 검색 오류:', error);
        });
}

function renderPagination(data, currentPage, pageSize) {
    const paginationContainer = document.getElementById('notification-pagination');
    paginationContainer.innerHTML = '';

    if (!data.totalPages || data.totalPages <= 1) {
        return;
    }

    for (let i = 0; i < data.totalPages; i++) {
        console.log('페이지 버튼 생성:', i + 1);
        const button = document.createElement('button');
        button.textContent = i + 1;
        button.className = 'px-2 py-1 border rounded mx-1';
        if (i === currentPage) {
            button.classList.add('bg-blue-500', 'text-white');
            button.disabled = true; // 현재 페이지 버튼은 비활성화
        }
        button.addEventListener('click', () => {
            searchNotifications(i, pageSize);
        });
        paginationContainer.appendChild(button);
    }
}
