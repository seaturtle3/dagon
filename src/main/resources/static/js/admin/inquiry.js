// 목록 조회에만 admin 경로 사용
const listApiUrl = '/api/inquiries/admin';
// 단건 조회 및 삭제는 일반 경로 사용
const detailApiUrl = '/api/inquiries';
const token = localStorage.getItem('authToken');

let inquiryCurrentPage = 0;
let inquiryTotalPages = 0;
let inquiryCurrentKeyword = '';

const searchBtn = document.getElementById('inquiry-search-btn');
const searchInput = document.getElementById('inquiry-search');

const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
};

const inquiryTableBody = document.querySelector('#inquiry-table tbody');
const paginationDiv = document.getElementById('inquiry-pagination');

const detailPopup = document.getElementById('inquiry-detail');
const detailContent = document.getElementById('inquiry-details');
const detailCloseBtn = detailPopup.querySelector('.close');

async function fetchInquiries(page = 0, keyword = '') {
    inquiryCurrentPage = page;
    inquiryCurrentKeyword = keyword;
    try {
        const response = await fetch(`${listApiUrl}?page=${page}&keyword=${encodeURIComponent(keyword)}`, {
            method: 'GET',
            headers
        });

        const data = await response.json();
        if (!Array.isArray(data.content)) {
            console.error('inquiries가 배열이 아님:', data);
            return;
        }

        inquiryTotalPages = data.totalPages;
        renderInquiryTable(data.content);
        renderPagination();
    } catch (error) {
        console.error('fetchInquiries 에러:', error);
    }
}

function renderInquiryTable(inquiries) {
    inquiryTableBody.innerHTML = '';
    if (inquiries.length === 0) {
        inquiryTableBody.innerHTML = '<tr><td colspan="6" style="text-align:center;">검색 결과가 없습니다.</td></tr>';
        return;
    }

    inquiries.forEach(inquiry => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>${inquiry.id}</td>
            <td class="title" style="color:blue; text-decoration:underline; cursor:pointer;">${inquiry.title}</td>
            <td>${inquiry.userName}</td>
            <td>${new Date(inquiry.createdAt).toLocaleString()}</td>
            <td>${inquiry.answeredAt ? '답변 완료' : '미답변'}</td>
            <td>
                <button class="detail-btn" data-id="${inquiry.id}">상세보기</button>
                <button class="delete-btn" data-id="${inquiry.id}">삭제</button>
            </td>
        `;

        tr.querySelector('.title').addEventListener('click', () => openDetail(inquiry.id));
        tr.querySelector('.detail-btn').addEventListener('click', () => openDetail(inquiry.id));

        tr.querySelector('.delete-btn').addEventListener('click', () => {
            if (confirm('정말 삭제하시겠습니까?')) {
                deleteInquiry(inquiry.id);
            }
        });

        inquiryTableBody.appendChild(tr);
    });
}

function renderPagination() {
    paginationDiv.innerHTML = '';

    const prevBtn = document.createElement('button');
    prevBtn.textContent = '이전';
    prevBtn.disabled = inquiryCurrentPage <= 0;
    prevBtn.addEventListener('click', () => {
        if (inquiryCurrentPage > 0) fetchInquiries(inquiryCurrentPage - 1, inquiryCurrentKeyword);
    });
    paginationDiv.appendChild(prevBtn);

    const pageInfo = document.createElement('span');
    pageInfo.textContent = ` ${inquiryCurrentPage + 1} / ${inquiryTotalPages} `;
    paginationDiv.appendChild(pageInfo);

    const nextBtn = document.createElement('button');
    nextBtn.textContent = '다음';
    nextBtn.disabled = inquiryCurrentPage >= inquiryTotalPages - 1;
    nextBtn.addEventListener('click', () => {
        if (inquiryCurrentPage < inquiryTotalPages - 1) fetchInquiries(inquiryCurrentPage + 1, inquiryCurrentKeyword);
    });
    paginationDiv.appendChild(nextBtn);
}

function openDetail(id) {
    fetch(`${detailApiUrl}/${id}`, { headers })
        .then(res => {
            if (!res.ok) throw new Error('상세 정보를 불러오는 데 실패했습니다.');
            return res.json();
        })
        .then(data => {
            detailContent.innerHTML = `
                <h3>${data.title}</h3>
                <p><strong>작성자:</strong> ${data.userName}</p>
                <p><strong>파트너명:</strong> ${data.partnerName || '-'}</p>
                <p><strong>내용:</strong></p>
                <p>${data.content}</p>
                <p><strong>답변 완료:</strong> ${data.answeredAt ? new Date(data.answeredAt).toLocaleString() : '미답변'}</p>
        <p><strong>답변 내용:</strong></p>
<p>${data.answerContent ? data.answerContent : '아직 답변이 등록되지 않았습니다.'}</p>
                <div id="answer-section" style="margin-top: 20px;">
                    <h4>답변 작성</h4>
                    <textarea id="answer-content" rows="5" style="width: 100%;"></textarea>
                    <button id="submit-answer-btn" style="margin-top: 10px;">답변 전송</button>
                </div>
            `;
            detailPopup.classList.remove('hidden');

            // 답변 버튼 핸들러
            document.getElementById('submit-answer-btn').addEventListener('click', () => {
                const answer = document.getElementById('answer-content').value.trim();
                if (!answer) {
                    alert('답변 내용을 입력하세요.');
                    return;
                }

                fetch(`${detailApiUrl}/${id}/answer`, {
                    method: 'POST',
                    headers,
                    body: JSON.stringify({ answerContent: answer })
                })
                    .then(res => {
                        if (!res.ok) throw new Error('답변 전송 실패');
                        alert('답변이 등록되었습니다.');
                        detailPopup.classList.add('hidden');
                        fetchInquiries(inquiryCurrentPage, inquiryCurrentKeyword);
                    })
                    .catch(err => alert(err.message));
            });
        })
        .catch(err => alert(err.message));
}
detailCloseBtn.addEventListener('click', () => {
    detailPopup.classList.add('hidden');
});

window.addEventListener('click', (e) => {
    if (e.target === detailPopup) {
        detailPopup.classList.add('hidden');
    }
});

function deleteInquiry(id) {
    fetch(`${detailApiUrl}/${id}`, {
        method: 'DELETE',
        headers
    })
        .then(res => {
            if (res.status === 200 || res.status === 204) {
                alert('삭제되었습니다.');
                fetchInquiries(inquiryCurrentPage, inquiryCurrentKeyword);
            } else if (res.status === 403) {
                alert('삭제 권한이 없습니다.');
            } else if (res.status === 401) {
                alert('로그인이 필요합니다.');
            } else {
                throw new Error('삭제에 실패했습니다.');
            }
        })
        .catch(err => alert(err.message));
}

searchBtn.addEventListener('click', () => {
    inquiryCurrentKeyword = searchInput.value.trim();
    fetchInquiries(0, inquiryCurrentKeyword);
});

// 초기 로드
fetchInquiries();


