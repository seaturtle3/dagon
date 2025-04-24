window.onload = function() {
    fetchFishingReports();  // 페이지 로딩 시 데이터 가져오기
};

function fetchFishingReports() {
    fetch('/api/fishing-report/get-all', {  // 정확히 이렇게!
        method: 'GET',
        credentials: 'include'
    })
        .then(response => response.json())  // 서버에서 받은 JSON 데이터 처리
        .then(data => {
            displayFishingReports(data);  // 테이블에 데이터 표시
        })
        .catch(error => console.error('Error fetching data:', error));  // 에러 처리
}

function displayFishingReports(reports) {
    if (!Array.isArray(reports)) {
        console.error('응답이 배열이 아님:', reports);
        return;
    }

    const tableBody = document.getElementById('fishingReportsTableBody');
    tableBody.innerHTML = '';  // 기존의 테이블 내용 초기화

    reports.forEach(report => {
        const row = document.createElement('tr');

        // 제목
        const titleCell = document.createElement('td');
        titleCell.textContent = report.title || '제목 없음';  // 제목이 없으면 기본값
        row.appendChild(titleCell);

        // 작성자
        const userCell = document.createElement('td');
        userCell.textContent = report.user ? report.user.uname : '작성자 정보 없음';  // user가 없으면 기본값
        row.appendChild(userCell);

        // 상품
        const productCell = document.createElement('td');
        productCell.textContent = report.product ? report.product.name : '상품 정보 없음';  // product가 없으면 기본값
        row.appendChild(productCell);

        // 조회수
        const viewsCell = document.createElement('td');
        viewsCell.textContent = report.views || 0;  // 조회수 없으면 0
        row.appendChild(viewsCell);

        // 날짜
        const dateCell = document.createElement('td');
        const date = new Date(report.fishingAt);
        dateCell.textContent = date.toLocaleString() || '날짜 정보 없음';  // 날짜가 없으면 기본값
        row.appendChild(dateCell);

        // 테이블에 row 추가
        tableBody.appendChild(row);
    });
}
