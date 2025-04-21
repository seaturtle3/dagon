// 페이지 로드 시 전체 회원 목록 불러오기
document.addEventListener('DOMContentLoaded', function() {
    loadUsers();
    // 대시보드 및 회원 정보 링크 클릭 이벤트
    document.getElementById('dashboard-link').addEventListener('click', showDashboard);
    document.getElementById('user-info-link').addEventListener('click', showUserInfo);

    // 검색 버튼 클릭 시 검색 결과 로드
    document.getElementById('search-btn').addEventListener('click', function() {
        loadUsers();
    });
});

// 회원 목록 조회 함수
function loadUsers(page = 0, size = 10) {
    const search = document.getElementById('search').value.trim();
    const url = `/api/admin/?page=${page}&size=${size}&search=${search}`;

    const token = localStorage.getItem('authToken'); // 토큰을 localStorage에서 가져옴
    const headers = {
        'Authorization': `Bearer ${token}`
    };

    fetch(url, { headers })
        .then(response => {
            if (!response.ok) {
                throw new Error('API 요청 실패');
            }
            return response.json();
        })
        .then(data => {
            if (data && data.content) {
                const userList = data.content;
                const totalPages = data.totalPages;
                renderUserList(userList);
                renderPagination(totalPages, page);
            } else {
                console.error('유효하지 않은 데이터:', data);
            }
        })
        .catch(error => {
            console.error('Error fetching users:', error);
            alert('회원 목록을 불러오는 데 실패했습니다.');
        });
}

function renderUserList(users) {
    const userTable = document.getElementById('user-table').getElementsByTagName('tbody')[0];
    userTable.innerHTML = ''; // 기존 데이터 비우기

    if (!users || users.length === 0) {
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="5">회원 정보가 없습니다.</td>`;
        userTable.appendChild(row);
        return;
    }

    users.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${user.uno}</td>
            <td>${user.uid}</td>
            <td>${user.uname}</td>
            <td>${user.email}</td>
            <td>
                <button class="btn btn-info" onclick="viewUserDetail('${user.uid}')">상세보기</button>
            </td>
        `;
        userTable.appendChild(row);
    });
}

// 회원 페이징 처리
function renderPagination(totalPages, currentPage) {
    const pagination = document.querySelector('.pagination');
    pagination.innerHTML = ''; // 기존 페이징 비우기

    for (let i = 0; i < totalPages; i++) {
        const pageItem = document.createElement('button');
        pageItem.innerText = i + 1;
        pageItem.classList.add('page-btn');
        pageItem.addEventListener('click', () => loadUsers(i));
        pagination.appendChild(pageItem);
    }
}

// 회원 상세보기
function viewUserDetail(uid) {
    const token = localStorage.getItem('authToken'); // 토큰을 localStorage에서 가져옴
    const headers = {
        'Authorization': `Bearer ${token}`
    };

    fetch(`/api/admin/users/${uid}`, { headers })
        .then(response => response.json())
        .then(user => {
            showUserDetailPopup(user);
        })
        .catch(error => console.error('Error fetching user details:', error));
}

// 회원 상세보기 팝업
function showUserDetailPopup(user) {
    const popup = document.getElementById('user-detail');
    const userDetails = document.getElementById('user-details');

    // 팝업 내용 채우기
    userDetails.innerHTML = `
        <p>아이디: ${user.uid}</p>
        <p>이름: ${user.uname}</p>
        <p>이메일: ${user.email}</p>
        <p>전화번호: ${user.phone}</p>
        <p>가입일: ${user.createdAt}</p>
    `;

    popup.classList.remove('hidden');

    // 수정 및 삭제 버튼
    document.getElementById('update-btn').onclick = function() {
        openEditUserPopup(user);
    };

    document.getElementById('delete-btn').onclick = function() {
        deleteUser(user.uid);
    };

    // 팝업 닫기
    document.getElementById('close-detail').onclick = function() {
        popup.classList.add('hidden');
    };
}
username
// 회원 수정 팝업 열기
function openEditUserPopup(user) {
    const editPopup = document.createElement('div');
    editPopup.innerHTML = `
        <h3>회원 수정</h3>
        <label>이름: <input type="text" id="edit-uname" value="${user.uname}"></label><br>
        <label>이메일: <input type="email" id="edit-email" value="${user.email}"></label><br>
        <label>전화번호: <input type="text" id="edit-phone" value="${user.phone}"></label><br>
        <button id="save-edit">저장</button>
        <button onclick="closeEditPopup()">취소</button>
    `;

    document.body.appendChild(editPopup);

    document.getElementById('save-edit').onclick = function() {
        updateUser(user.uid);
    };
}

// 회원 수정 API 호출
function updateUser(uid) {
    const updatedUser = {
        uname: document.getElementById('edit-uname').value,
        email: document.getElementById('edit-email').value,
        phone: document.getElementById('edit-phone').value
    };

    const token = localStorage.getItem('authToken'); // 토큰을 localStorage에서 가져옴
    const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };

    fetch(`/api/admin/users/${uid}`, {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(updatedUser)
    })
        .then(response => response.json())
        .then(data => {
            alert('회원 정보가 수정되었습니다.');
            loadUsers();
            closeEditPopup();
        })
        .catch(error => console.error('Error updating user:', error));
}
// 회원 수정 팝업 닫기
function closeEditPopup() {
    const editPopup = document.querySelector('div');
    editPopup.remove();
}

// 회원 삭제 API 호출
function deleteUser(uid) {
    if (confirm('정말로 이 회원을 삭제하시겠습니까?')) {
        const token = localStorage.getItem('authToken'); // 토큰을 localStorage에서 가져옴
        const headers = {
            'Authorization': `Bearer ${token}`
        };

        fetch(`/api/admin/users/${uid}`, {
            method: 'DELETE',
            headers: headers
        })
            .then(response => {
                alert('회원이 삭제되었습니다.');
                loadUsers();
                document.getElementById('user-detail').classList.add('hidden');
            })
            .catch(error => console.error('Error deleting user:', error));
    }
}


// 대시보드와 회원 정보 페이지 전환
function showDashboard() {
    document.getElementById('dashboard').classList.remove('hidden');
    document.getElementById('user-info').classList.add('hidden');
}

function showUserInfo() {
    document.getElementById('user-info').classList.remove('hidden');
    document.getElementById('dashboard').classList.add('hidden');
}
