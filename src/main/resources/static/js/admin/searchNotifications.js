document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('search-container').innerHTML = `
        <div class="flex items-center gap-2 mb-4">
            <input type="number" id="search-user-id" class="border p-2 rounded" placeholder="유저 번호">
            <select id="search-type" class="border p-2 rounded">
                <option value="">전체 유형</option>
                <option value="NOTICE">공지사항</option>
                <option value="REPLY">답변</option>
                <option value="RESERVATION">예약 알림</option>
            </select>
            <button onclick="searchNotifications()" class="bg-blue-500 text-white px-4 py-2 rounded">검색</button>
        </div>
    `;
    searchNotifications();
});

function searchNotifications(page = 0, size = 10) {
    const uno = document.getElementById('search-user-id').value;
    const type = document.getElementById('search-type').value;
    const authToken = localStorage.getItem('authToken');



    const params = new URLSearchParams({
        uno: uno,
        type: type,
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
            const tableBody = document.getElementById('notification-table-body');
            tableBody.innerHTML = '';

            if (data.content.length === 0) {
                const noDataRow = document.createElement('tr');
                noDataRow.innerHTML = `<td colspan="6" class="text-center text-gray-500">검색된 알림이 없습니다.</td>`;
                tableBody.appendChild(noDataRow);
            } else {
                data.content.forEach(notification => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${notification.receiverId}</td>
                        <td>${notification.title}</td>
                        <td>${notification.content}</td>
                        <td>${notification.senderName}</td>
                        <td>${notification.type}</td>
                        <td>${new Date(notification.createdAt).toLocaleString()}</td>
                        <td>${notification.isRead ? '읽음' : '안 읽음'}</td>
                    `;
                    tableBody.appendChild(row);
                });

                renderPagination(data);
            }
        })
        .catch(error => {
            console.error('알림 검색 오류:', error);
        });
}

function renderPagination(data) {
    const paginationContainer = document.getElementById('notification-pagination');
    paginationContainer.innerHTML = '';

    for (let i = 0; i < data.totalPages; i++) {
        const button = document.createElement('button');
        button.textContent = i + 1;
        button.className = 'px-2 py-1 border rounded mx-1';
        button.onclick = () => searchNotifications(i);
        if (i === data.number) {
            button.classList.add('bg-blue-500', 'text-white');
        }
        paginationContainer.appendChild(button);
    }
}
