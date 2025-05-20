
const size = 10;
let currentPage = 0;

async function loadData(page = 0) {
    currentPage = page; // 페이지 갱신
    const keyword = document.getElementById('searchKeyword').value;
    const type = document.getElementById('searchType').value;
    const status = document.getElementById('statusFilter').value;

    const params = new URLSearchParams({
        page: page,
        size: size,
        keyword: keyword,
        type: type,
        status: status
    });

    try {
        const response = await fetch(`/api/partners?${params.toString()}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getAuthToken()
            }
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || '서버 오류');
        }

        const data = await response.json();
        renderpatnerTable(data);
        renderPaginationPatner(data);
    } catch (error) {
        console.error('Error fetching data', error);
        alert('데이터를 불러오는 중 오류가 발생했습니다.');
    }
}

function renderpatnerTable(data) {
    const body = document.getElementById('tableBody');
    body.innerHTML = '';

    if (data.content.length === 0) {
        body.innerHTML = `
            <tr>
                <td colspan="4">검색 결과가 없습니다.</td>
            </tr>
        `;
        return;
    }

    data.content.forEach(item => {
        const row = document.createElement('tr');
        row.style.cursor = 'pointer';
        row.onclick = () => goToDetail(item.pid);
        row.innerHTML = `
            <td>${item.pid}</td>
            <td>${item.pname}</td>
             <td>${item.uid}</td>
            <td>${item.displayName}</td>
            <td>${item.pstatus}</td>
           
        `;
        body.appendChild(row);
    });
}

function renderPaginationPatner(data) {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    // totalPages가 1일 때에도 버튼을 보이게 하기
    const first = document.createElement('button');
    first.innerHTML = '&laquo;';
    first.onclick = () => loadData(0);
    pagination.appendChild(first);

    const startPage = Math.max(0, currentPage - 2);
    const endPage = Math.min(data.totalPages - 1, currentPage + 2);

    for (let i = startPage; i <= endPage; i++) {
        const button = document.createElement('button');
        button.textContent = i + 1;
        if (i === currentPage) {
            button.classList.add('active');
        }
        button.onclick = () => loadData(i);
        pagination.appendChild(button);
    }

    // "Last" button을 보이게 하여 마지막 페이지로 이동
    const last = document.createElement('button');
    last.innerHTML = '&raquo;';
    last.onclick = () => loadData(data.totalPages - 1);
    pagination.appendChild(last);
}

function goToDetail(id) {
    window.location.href = `/partner/detail/${id}`;
}

document.addEventListener('DOMContentLoaded', () => {
    loadData(0); // 페이지가 처음 로드되었을 때
});

// 검색 버튼 클릭 시 로드
document.getElementById('partner-search-btn').addEventListener('click', () => loadPartnerData(0));
