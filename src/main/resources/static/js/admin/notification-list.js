// 공지사항 검색
function Notifications() {
    const searchKeyword = document.getElementById('notice-search-keyword').value.trim();
    const searchType = document.getElementById('notice-search-type').value.trim(); // 예: title, content, title+content 등
    console.log('검색어:', searchKeyword);
    console.log('검색 타입:', searchType);

    const url = `/api/notices?type=${encodeURIComponent(searchType)}&keyword=${encodeURIComponent(searchKeyword)}`;
    console.log('요청 URL:', url);


    fetch(url, {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("authToken")
        }
    })
        .then(response => {
            if (!response.ok) throw new Error('네트워크 응답 오류');
            return response.json();
        })
        .then(data => {
            if (data && data.content) {
                renderNotificationList(data.content);  // 공지사항 목록 렌더링
                setupPagination(data);  // 페이지네이션 설정
            } else {
                console.error('유효하지 않은 데이터:', data);
            }
        })
        .catch(error => console.error('Error fetching notices:', error));
}

function renderNotificationList(notices) {
    const tableBody = document.getElementById('notice-table').querySelector('tbody');
    console.log("테이블 바디:", tableBody);  // tableBody가 제대로 선택되었는지 확인

    tableBody.innerHTML = '';

    if (notices.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="6">검색된 공지사항이 없습니다.</td></tr>`;
        return;
    }

    notices.forEach((notice, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${notice.isTop === true ? "✔" : ""}</td>
            <td>${index + 1}</td>
            <td><a href="/notices/${notice.noticeId}">${notice.title}</a></td>
            <td>${new Date(notice.createdAt).toLocaleDateString()}</td>
            <td>${notice.views !== undefined ? notice.views : '0'}</td>
            <td><a href="/notices/${notice.noticeId}">상세보기</a></td>
        `;
        tableBody.appendChild(row);
    });
}

function setupPagination(data) {
    const paginationContainer = document.getElementById('notice-pagination');
    paginationContainer.innerHTML = '';

    const totalPages = data.totalPages;
    const currentPage = data.pageable.pageNumber;


    if (currentPage > 0) {
        const prevButton = document.createElement('button');
        prevButton.innerText = '이전';
        prevButton.onclick = () => loadNotificationData(currentPage - 1);
        paginationContainer.appendChild(prevButton);
    }

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.innerText = i + 1;
        pageButton.onclick = () => loadNotificationData(i);
        paginationContainer.appendChild(pageButton);
    }

    if (currentPage < totalPages - 1) {
        const nextButton = document.createElement('button');
        nextButton.innerText = '다음';
        nextButton.onclick = () => loadNotificationData(currentPage + 1);
        paginationContainer.appendChild(nextButton);
    }
}

function loadNotificationData(page) {
    const searchKeyword = document.getElementById('notice-search-keyword').value.trim();

    fetch(`/api/notices?page=${page}&keyword=${searchKeyword}`, {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("authToken")
        }
    })
        .then(response => response.json())
        .then(data => {
            renderNotificationList(data.content);
            setupPagination(data);
        })
        .catch(error => console.error('Error loading notices:', error));
}

document.addEventListener('DOMContentLoaded', function () {
    loadNotificationData(0);
    const searchBtn = document.getElementById('notice-search-btn');
    searchBtn.addEventListener('click', Notifications);
});
