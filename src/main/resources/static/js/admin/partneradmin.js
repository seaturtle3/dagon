
    const size = 10;
    let currentPage = 0;

    async function loadData(page) {
    currentPage = page;
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
    renderTable(data);
    renderPagination(data);

} catch (error) {
    console.error('Error fetching data', error);
    alert('데이터를 불러오는 중 오류가 발생했습니다.');
}
}



    function renderTable(data) {
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
                    <td>${item.displayName}</td>
                    <td>${item.pstatus}</td>
                `;
    body.appendChild(row);
});
}

    function renderPagination(data) {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    if (data.totalPages > 1) {
    const first = document.createElement('button');
    first.innerHTML = '&laquo;';
    first.onclick = () => loadData(0);
    pagination.appendChild(first);
}

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

    if (data.totalPages > 1) {
    const last = document.createElement('button');
    last.innerHTML = '&raquo;';
    last.onclick = () => loadData(data.totalPages - 1);
    pagination.appendChild(last);
}
}

    function goToDetail(id) {
    window.location.href = `/partner/detail/${id}`;
}

    document.addEventListener('DOMContentLoaded', () => {
    loadData(0);
});
