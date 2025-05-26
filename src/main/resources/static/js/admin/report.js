// 전역에 정의된 함수로 이동
function loadReportData(page = 0) {
    const searchType = document.getElementById("report-search-type").value;
    const searchKeyword = document.getElementById("report-search-keyword").value;

    const queryParams = new URLSearchParams();
    queryParams.append("page", page);
    if (searchKeyword.trim()) {
        queryParams.append(searchType, searchKeyword.trim());  // 동적으로 기준 적용
    }

    fetch(`/api/reports?${queryParams.toString()}`, {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("authToken")
        }
    })
        .then(res => res.json())
        .then(data => {
            const tableBody = document.getElementById("report-table-body");
            tableBody.innerHTML = "";

            data.content.forEach(report => {
                const row = document.createElement("tr");
                row.innerHTML = `
                <td>${report.id}</td>
                <td>${report.reporterUname || '-'}</td>
                <td>${report.reportedUserUname}</td>
                <td>${report.reason}</td>
                <td><button onclick="reactivateUser(${report.reportedUserId})" ${report.reportedUserEnabled ? 'disabled' : ''}>활성화</button></td>
                <td><button onclick="deactivateUser(${report.reportedUserId})" ${report.reportedUserEnabled ? 'disabled' : ''}>비활성화</button></td>
                <td><button onclick="deleteReport(${report.id})">삭제</button></td>
            `;
                tableBody.appendChild(row);
            });

            const pagination = document.getElementById("report-pagination");
            pagination.innerHTML = "";

// 이전 버튼
            const prevButton = document.createElement("button");
            prevButton.innerText = "이전";
            prevButton.disabled = page === 0;
            prevButton.addEventListener("click", () => loadReportData(page - 1));
            pagination.appendChild(prevButton);

// 페이지 번호 버튼
            for (let i = 0; i < data.totalPages; i++) {
                const pageButton = document.createElement("button");
                pageButton.innerText = i + 1;
                pageButton.style.padding = "8px 12px";
                pageButton.style.margin = "0 2px";
                pageButton.style.fontSize = "14px";
                if (i === page) pageButton.disabled = true;
                pageButton.addEventListener("click", () => loadReportData(i));
                pagination.appendChild(pageButton);
            }

// 다음 버튼
            const nextButton = document.createElement("button");
            nextButton.innerText = "다음";
            nextButton.disabled = page >= data.totalPages - 1;
            nextButton.addEventListener("click", () => loadReportData(page + 1));
            pagination.appendChild(nextButton);
        });
}
function deactivateUser(userId) {
    if (!confirm("해당 유저를 비활성화하시겠습니까?")) return;

    fetch(`/api/users/${userId}/deactivate`, {
        method: "PUT",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("authToken"),
            "Content-Type": "application/json"
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("비활성화 실패");
            alert("유저가 비활성화되었습니다.");
            loadReportData(0); // 목록 다시 로드
        })
        .catch(err => {
            console.error(err);
            alert("유저 비활성화 중 오류가 발생했습니다.");
        });
}

function reactivateUser(userId) {
    if (!confirm("해당 유저를 다시 활성화하시겠습니까?")) return;

    fetch(`/api/users/${userId}/reactivate`, {
        method: "PUT",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("authToken"),
            "Content-Type": "application/json"
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("활성화 실패");
            alert("유저가 다시 활성화되었습니다.");
            closeReportPopup();
            loadReportData(0); // 목록 다시 로드
        })
        .catch(err => {
            console.error(err);
            alert("유저 활성화 중 오류가 발생했습니다.");
        });
}

function deleteReport(reportId) {
    if (!confirm("해당 신고 내역을 삭제하시겠습니까?")) return;

    fetch(`/api/reports/${reportId}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("authToken")
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("삭제 실패");
            alert("신고가 삭제되었습니다.");
            loadReportData(0); // 목록 새로고침
        })
        .catch(err => {
            console.error(err);
            alert("신고 삭제 중 오류가 발생했습니다.");
        });
}






// DOM이 로드되었을 때 첫 데이터 로딩
document.addEventListener("DOMContentLoaded", function () {
    loadReportData(0);
});