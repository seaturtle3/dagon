let currenPage = 0;
const pageSize = 10;

document.addEventListener('DOMContentLoaded', () => {
    loadFAQs(currenPage);

    document.getElementById('faq-search-btn').addEventListener('click', () => {
        currenPage = 0;
        loadFAQs(currenPage);
    });
});

function loadFAQs(page) {
    const keyword = document.getElementById('faq-search-keyword').value;
    const type = document.getElementById('faq-search-type').value;

    let url = `/api/admin/faq?page=${page}&size=${pageSize}`;
    if (keyword) {
        url += `&keyword=${keyword}&faqType=${type}`;
    }

    fetch(url)
        .then(res => res.json())
        .then(data => {

            console.log("FAQ API 응답 전체:", data);
            console.log("totalPages:", data.totalPages);  // 여기서 undefined인지 확인
            console.log("currentPage:", data.number);     // 여기도

            renderFAQTable(data.content);
            renderPaginationFAQ(data.totalPages, data.number);
        });
}

function renderFAQTable(faqs) {
    const tbody = document.getElementById('faq-table-body');
    tbody.innerHTML = '';

    faqs.forEach(faq => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${faq.faqId}</td>
            <td>${faq.categoryName || '-'}</td>
            <td>${faq.question}</td>
            <td>${faq.displayOrder}</td>
            <td>${faq.isActive ? '활성' : '비활성'}</td>
            <td><button onclick="viewFAQ(${faq.faqId})">보기</button></td>
        `;
        tbody.appendChild(tr);
    });
}

function renderPaginationFAQ(totalPages, currentPage) {
    const pagination = document.getElementById('faq-pagination');
    pagination.innerHTML = '';

    // 이전 버튼
    const prevBtn = document.createElement('button');
    prevBtn.textContent = '이전';
    prevBtn.disabled = currentPage === 0;
    prevBtn.addEventListener('click', () => {
        if (currentPage > 0) loadFAQs(currentPage - 1);
    });
    pagination.appendChild(prevBtn);

    // 페이지 번호 버튼
    for (let i = 0; i < totalPages; i++) {
        const btn = document.createElement('button');
        btn.className = 'page-btn';
        btn.textContent = i + 1;
        if (i === currentPage) btn.disabled = true;
        btn.addEventListener('click', () => {
            loadFAQs(i);
        });
        pagination.appendChild(btn);
    }

    // 다음 버튼
    const nextBtn = document.createElement('button');
    nextBtn.textContent = '다음';
    nextBtn.disabled = currentPage === totalPages - 1;
    nextBtn.addEventListener('click', () => {
        if (currentPage < totalPages - 1) loadFAQs(currentPage + 1);
    });
    pagination.appendChild(nextBtn);
}


function viewFAQ(id) {
    console.log(id)
    fetch(`/api/admin/faq/${id}`)
        .then(res => res.json())
        .then(faq => {
            console.log('받은 FAQ:', faq);
            const detail = document.getElementById('faq-detail-content');
            detail.innerHTML = `
                <p><strong>질문:</strong> ${faq.question}</p>
                <p><strong>답변:</strong> ${faq.answer}</p>
                <p><strong>카테고리:</strong> ${faq.categoryName || '-'}</p>
                <p><strong>노출 순서:</strong> ${faq.displayOrder}</p>
                <p><strong>활성화:</strong> ${faq.isActive ? '예' : '아니오'}</p>

                <button id="btn-edit">수정</button>
                <button id="btn-delete">삭제</button>
            `;

            document.getElementById('faq-detail').classList.remove('hidden');

            // 수정 버튼 이벤트 연결
            document.getElementById('btn-edit').addEventListener('click', () => showEditForm(faq));

            // 삭제 버튼 이벤트 연결
            document.getElementById('btn-delete').addEventListener('click', () => deleteFAQ(faq.faqId));
        });
}

function showEditForm(faq) {
    const detail = document.getElementById('faq-detail-content');
    detail.innerHTML = `
        <label>질문: <input type="text" id="edit-question" value="${faq.question}"></label><br>
        <label>답변: <textarea id="edit-answer">${faq.answer}</textarea></label><br>
        <label>카테고리: <input type="text" id="edit-category" value="${faq.categoryName || ''}"></label><br>
        <label>노출 순서: <input type="number" id="edit-displayOrder" value="${faq.displayOrder}"></label><br>
        <label>활성화: <input type="checkbox" id="edit-isActive" ${faq.isActive ? 'checked' : ''}></label><br>

        <button id="btn-save">저장</button>
        <button id="btn-cancel">취소</button>
    `;

    // 저장 이벤트
    document.getElementById('btn-save').addEventListener('click', () =>{    console.log('저장할 FAQ id:', faq.faqId);
        saveFAQ(faq);});

    // 취소 이벤트 (다시 상세보기로)
    document.getElementById('btn-cancel').addEventListener('click', () => viewFAQ(faq.faqId));
}

function saveFAQ(faq) {
    const updatedFaq = {
        question: document.getElementById('edit-question').value,
        answer: document.getElementById('edit-answer').value,
        categoryId: faq.categoryId || 0, // 카테고리 ID를 받아올 수 있게 하거나, 별도 처리 필요
        displayOrder: parseInt(document.getElementById('edit-displayOrder').value, 10),
        isActive: document.getElementById('edit-isActive').checked


    };
    const id = faq.faqId;


    fetch(`/api/admin/faq/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(updatedFaq)
    })
        .then(res => {
            if (!res.ok) throw new Error('수정 실패');
            return res.json();
        })
        .then(data => {
            alert('수정 완료');
            currentPage = 0;       // 페이지 1번 설정
            loadFAQs(currentPage);
            document.getElementById('faq-detail').classList.add('hidden');
        })
        .catch(err => alert(err.message));
}

function deleteFAQ(id) {
    if (confirm('정말로 삭제하시겠습니까?')) {
        fetch(`/api/admin/faq/${id}`, {
            method: 'DELETE'
        })
            .then(res => {
                if (res.status === 204) {
                    alert('삭제 완료');
                    // FAQ 목록 페이지로 이동하거나 목록 새로고침
                    location.href = '/admin/dashboard';
                } else {
                    throw new Error('삭제 실패');
                }
            })
            .catch(err => alert(err.message));
    }
}
