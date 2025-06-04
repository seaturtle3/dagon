const PartnerManagementModule = (() => {
    const apiEndpoint = '/api/partners/all'; // 관리자용 파트너 리스트 API 가정




    let currentSearchType = 'pname';
    let currentSearchKeyword = '';
    let currentPageIndex = 0;
    const pageSize = 10;



    // DOM Elements (초기화 함수 내에서 할당)
    let tableBodyElem = null;
    let paginationElem = null;
    let searchTypeElem = null;
    let searchKeywordElem = null;
    let searchBtnElem = null;

    // 초기화 이벤트 바인딩 및 DOM 요소 할당
    function initializeEventBindings() {
        tableBodyElem = document.getElementById('pm-table-body');
        paginationElem = document.getElementById('pm-pagination');
        searchTypeElem = document.getElementById('pm-search-type');
        searchKeywordElem = document.getElementById('pm-search-keyword');
        searchBtnElem = document.getElementById('pm-search-btn');

        if (!tableBodyElem || !paginationElem || !searchTypeElem || !searchKeywordElem || !searchBtnElem) {
            console.error('필수 DOM 요소가 존재하지 않습니다.');
            return;
        }

        searchBtnElem.addEventListener('click', () => {
            currentSearchType = searchTypeElem.value;
            currentSearchKeyword = searchKeywordElem.value.trim();
            loadPartnerPage(0);
        });
    }

    async function fetchAndRenderPartnerList(pageIndex) {
        currentPageIndex = pageIndex;
        const params = new URLSearchParams({
            page: pageIndex,
            size: pageSize,
            searchType: currentSearchType,
            keyword: currentSearchKeyword
        });

        try {
            const response = await fetch(`${apiEndpoint}?${params.toString()}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            if (!response.ok) throw new Error('서버 응답 실패');

            const result = await response.json();
            renderPartnerTable(result.content);
            renderPagination(result.totalPages, pageIndex);
        } catch (error) {
            alert('파트너 목록 로딩 실패: ' + error.message);
        }
    }

    function renderPartnerTable(partnerArray) {
        tableBodyElem.innerHTML = '';

        if (partnerArray.length === 0) {
            tableBodyElem.innerHTML = '<tr><td colspan="6">검색 결과가 없습니다.</td></tr>';
            return;
        }

        partnerArray.forEach((partner, idx) => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${currentPageIndex * pageSize + idx + 1}</td>
                <td>${escapeHtml(partner.pname)}</td>
                <td>${escapeHtml(partner.ceoName)}</td>
                <td>${escapeHtml(partner.paddress)}</td>
                <td>${escapeHtml(partner.license)}</td>
                <td><button class="detail-btn" data-uno="${partner.uno}">상세보기</button></td>
            `;

            tableBodyElem.appendChild(row);
        });

        // 상세보기 버튼 이벤트 바인딩
        document.querySelectorAll('.detail-btn').forEach(btn => {
            btn.addEventListener('click', event => {
                const uno = event.target.getAttribute('data-uno');
                alert(`파트너 상세보기 (uno: ${uno}) - 상세기능은 추가 구현 필요`);
            });
        });
    }

    function renderPagination(totalPages, currentPage) {
        paginationElem.innerHTML = '';

        // 이전 버튼
        const prevBtn = document.createElement('button');
        prevBtn.textContent = 'Prev';
        prevBtn.className = 'page-btn prev-btn';
        prevBtn.disabled = currentPage === 0;  // 첫 페이지면 비활성화
        prevBtn.addEventListener('click', () => {
            if (currentPage > 0) loadPartnerPage(currentPage - 1);
        });
        paginationElem.appendChild(prevBtn);

        // 페이지 번호 버튼들
        for (let i = 0; i < totalPages; i++) {
            const btn = document.createElement('button');
            btn.textContent = i + 1;
            btn.className = 'page-btn';
            if (i === currentPage) btn.classList.add('active');
            btn.addEventListener('click', () => loadPartnerPage(i));
            paginationElem.appendChild(btn);
        }

        // 다음 버튼
        const nextBtn = document.createElement('button');
        nextBtn.textContent = 'Next';
        nextBtn.className = 'page-btn next-btn';
        nextBtn.disabled = currentPage === totalPages - 1;  // 마지막 페이지면 비활성화
        nextBtn.addEventListener('click', () => {
            if (currentPage < totalPages - 1) loadPartnerPage(currentPage + 1);
        });
        paginationElem.appendChild(nextBtn);
    }

    function loadPartnerPage(pageIndex) {
        fetchAndRenderPartnerList(pageIndex);
    }

    // 간단한 HTML 이스케이프 함수 (XSS 방지)
    function escapeHtml(text) {
        if (!text) return '';
        return text.replace(/[&<>"']/g, function (m) {
            return ({
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                '"': '&quot;',
                "'": '&#39;'
            })[m];
        });
    }

    function init() {
        initializeEventBindings();
        loadPartnerPage(0);  // 첫 페이지 로딩
    }

    return {
        init,
        loadPartnerPage,
    };
})();

// ===== 전역 함수: 페이지 전환 함수 =====
function showPage(pageId) {
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('visible');
        page.style.display = 'none';  // 확실히 숨김 처리
    });
    const target = document.getElementById(pageId);
    if (target) {
        target.classList.add('visible');
        target.style.display = 'block';  // 보여주기 처리
    }
}

// ===== DOM 준비되면 초기화 및 이벤트 바인딩 =====
document.addEventListener('DOMContentLoaded', () => {
    // 파트너 관리 모듈 초기화
    PartnerManagementModule.init();

    // 메뉴 클릭 이벤트 바인딩 (예: 파트너 관리 링크)
    const partnerManagementLink = document.getElementById('partner-management-link');
    if (partnerManagementLink) {
        partnerManagementLink.addEventListener('click', () => {
            showPage('partner-management');
        });
    }

    // 다른 메뉴도 여기에 추가 가능 (예: 예약관리, 상품관리 등)
});