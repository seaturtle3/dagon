document.addEventListener('DOMContentLoaded', function () {
    console.log('페이지가 로드되었습니다.');

    // 기본은 대시보드만 보이도록 (loadUsers 제거)
    document.getElementById('dashboard-link').addEventListener('click', showDashboard);
    document.getElementById('user-info-link').addEventListener('click', function () {
        showUserInfo(); // 이 안에서 loadUsers() 호출됨
    });
    document.getElementById('partner-list-link').addEventListener('click',showPartnerList); // 이 안에서 loadUsers() 호출됨


    document.getElementById('search-btn').addEventListener('click', function () {
        loadUsers(); // 검색할 때는 무조건 호출됨
    });
});
// 대시보드와 회원 정보 페이지 전환
function showDashboard() {
    document.getElementById('dashboard').classList.remove('hidden');
    document.getElementById('user-info').classList.add('hidden');
    document.getElementById('partner-list').classList.add('hidden');  // 파트너 신청 목록 숨김
    document.getElementById('user-detail').classList.add('hidden');
}

function showUserInfo() {
    document.getElementById('user-info').classList.remove('hidden');
    document.getElementById('dashboard').classList.add('hidden');
    document.getElementById('user-detail').classList.add('hidden');
    document.getElementById('partner-list').classList.add('hidden');  // 파트너 신청 목록 숨김
    loadUsers();
}

function showPartnerList() {
    document.getElementById('partner-list').classList.remove('hidden');
    document.getElementById('dashboard').classList.add('hidden');
    document.getElementById('user-info').classList.add('hidden');
    loadData(0);  // 파트너 목록을 로드하는 함수 (필요에 따라 구현)
}



// 회원 목록 조회 함수
function loadUsers(page = 0, size = 10) {
    const search = document.getElementById('search').value.trim();
    const query = search ? `&search=${search}` : '';
    const url = `/api/admin/users?page=${page}&size=${size}${query}`;

    const token = localStorage.getItem('authToken'); // 토큰을 localStorage에서 가져옴
    const headers = {
        'Authorization': `Bearer ${token}`
    };

    fetch(url, {headers})
        .then(response => {
            if (!response.ok) {
                throw new Error('API 요청 실패');
            }
            return response.json();
        })
        .then(data => {
            console.log(data); // 데이터 확인
            if (data && data.content) {
                const userList = data.content;
                const totalPages = data.totalPages;
                renderUserList(userList); // 유저 목록을 렌더링
                renderUserPagination(totalPages, page); // 페이징 렌더링
                console.log('renderPagination 호출됨', totalPages, page);
                console.log('totalPages:', data.totalPages);
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
    console.log(userTable);
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
        console.log(row.innerHTML); // 로그로 생성된 테이블 행을 확인
        row.classList.add(user.isActive ? 'active-user' : 'inactive-user');
        userTable.appendChild(row);
    });
}

// 회원 페이징 처리
function renderUserPagination(totalPages, currentPage) {
    console.log('✅ renderPagination 호출됨', totalPages, currentPage);

    const pagination = document.querySelector('.pagination');
    pagination.innerHTML = '';

    if (!totalPages || totalPages === 0) {
        console.warn('⚠️ 페이지가 0이거나 없음');
        return;
    }

    for (let i = 0; i < totalPages; i++) {
        const pageItem = document.createElement('button');
        pageItem.innerText = i + 1;
        pageItem.classList.add('page-btn');
        if (i === currentPage) {
            pageItem.style.fontWeight = 'bold';
        }
        pageItem.addEventListener('click', () => loadUsers(i));
        pagination.appendChild(pageItem);
    }
}

// 회원 상세보기
function viewUserDetail(uid) {
    console.log("상세보기 클릭:", uid);
    const token = localStorage.getItem('authToken'); // 토큰을 localStorage에서 가져옴
    const headers = {
        'Authorization': `Bearer ${token}`
    };

    fetch(`/api/admin/user/${uid}`, {headers}) // 유저 상세 정보 API 호출
        .then(response => {
            if (!response.ok) {
                throw new Error('유저 상세 정보를 불러오는 데 실패했습니다.');
            }
            return response.json();
        })
        .then(user => {
            // 유저 정보를 받아서 페이지에 표시
            console.log(user); // 여기서 user 정보를 콘솔로 확인해보세요.
            showUserDetailPopup(user);// 페이지에 상세 정보를 표시하는 함수 호출
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// 회원 상세보기 팝업 (수정 및 삭제 통합)
function showUserDetailPopup(user) {
    const popup = document.getElementById('user-detail');
    const userDetails = document.getElementById('user-details');

        if (!popup || !userDetails) {
            console.error("❌ popup 또는 userDetails 요소가 없습니다.");
            return;
        }

        console.log("✅ showUserDetailPopup 호출됨, 유저:", user);

        userDetails.innerHTML = `...`; // 생략된 innerHTML 구성
        popup.classList.remove('hidden');


    userDetails.innerHTML = `
        <h3>회원 상세정보 및 수정</h3>
        <label>아이디: <input type="text" value="${user.uid}" readonly></label><br>
        <label>이름: <input type="text" id="edit-uname" value="${user.uname}"></label><br>
        <label>닉네임: <input type="text" id="edit-nickname" value="${user.nickname ?? ''}"></label><br>
        <label>이메일: <input type="email" id="edit-email" value="${user.email}"></label><br>
        <label>전화번호: <input type="text" id="edit-phone" value="${user.fullPhone}"></label><br>
        <label>프로필 이미지 URL: <input type="text" id="edit-profileImg" value="${user.profileImg ?? ''}"></label><br>
        <label>포인트: <input type="number" id="edit-points" value="${user.points}"></label><br>
        <label>레벨: <input type="number" id="edit-level" value="${user.level}"></label><br>
        <label>레벨 포인트: <input type="number" id="edit-levelPoint" value="${user.levelPoint ?? 0}"></label><br>
        <label>로그인 타입: <input type="text" id="edit-loginType" value="${user.loginType ?? ''}"></label><br>
        <label>가입일: <input type="text" value="${user.createdAt}" readonly></label><br>
        <label>권한: <input type="text" id="edit-role" value="${user.role}"></label><br>
         <label>활성화 여부: 
<input type="checkbox" id="edit-isActive" ${user.isActive ? 'checked' : ''}>
    </label><br><br>

        <button id="save-edit">저장</button>
        <button id="delete-btn">삭제</button>
        <button id="cancel-btn">닫기</button>
    `;

    popup.classList.remove('hidden');

    // 저장
    document.getElementById('save-edit').onclick = function () {
        const updatedUser = {
            uname: document.getElementById('edit-uname').value,
            nickname: document.getElementById('edit-nickname').value,
            email: document.getElementById('edit-email').value,
            phone: document.getElementById('edit-phone').value,
            profileImg: document.getElementById('edit-profileImg').value,
            points: parseInt(document.getElementById('edit-points').value),
            level: parseInt(document.getElementById('edit-level').value),
            levelPoint: parseInt(document.getElementById('edit-levelPoint').value),
            loginType: document.getElementById('edit-loginType').value,
            role: document.getElementById('edit-role').value,
            isActive: document.getElementById('edit-isActive').checked
        };

        const token = localStorage.getItem('authToken');
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };

        fetch(`/api/admin/user/${user.uno}`, {
            method: 'PUT',
            headers: headers,
            body: JSON.stringify(updatedUser)
        })
            .then(res => {
                if (!res.ok) throw new Error('수정 실패');
                return res.json();
            })
            .then(() => {
                alert('회원 정보가 수정되었습니다.');
                console.log("체크박스 상태:", document.getElementById('edit-isActive').checked);
                console.log("업데이트 유저:", updatedUser);
                popup.classList.add('hidden');
                loadUsers();
            })
            .catch(err => {
                console.error(err);
                alert('수정 중 오류 발생');
            });
    };

// 삭제
    document.getElementById('delete-btn').addEventListener('click', function() {
        const token = getAuthToken(); // ★ 토큰 가져오기 (로그인한 관리자 토큰)

        if (!token) {
            alert("인증되지 않은 요청입니다. 로그인 후 다시 시도해주세요.");
            return;
        }

        if (confirm('정말로 이 회원을 삭제하시겠습니까?')) {
            console.log('삭제 요청 대상:', user);
            console.log('삭제할 UNO:', user.uno);

            fetch(`/api/admin/user/${user.uno}`, { // ★ 관리자가 회원 삭제하는 API로 요청!
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // ★ 토큰은 Bearer 형식으로
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert('회원이 삭제되었습니다.');

                    }
                })
                .catch(error => {
                    console.error('삭제 오류:', error);
                    alert('삭제 중 오류 발생');
                });
        }
    });

    // 닫기
    document.getElementById('cancel-btn').onclick = function () {
        popup.classList.add('hidden');
    };
}


