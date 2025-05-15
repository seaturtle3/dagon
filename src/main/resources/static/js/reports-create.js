function openReportPopup() {
    if (document.getElementById("reportPopup")) return;

    const html = `
    <div id="reportPopup" style="position:fixed; top:30%; left:50%; transform:translate(-50%, -30%); background:white; padding:20px; border-radius:10px; border:1px solid #ccc; z-index:9999;">
        <h3>사용자 신고</h3>
        <label>신고 대상 UID:</label><br>
        <input type="text" id="reportedUid" placeholder="피신고자 UID를 입력하세요"><br><br>
        <label>신고 사유:</label><br>
        <textarea id="reason" rows="4" cols="30" placeholder="신고 사유를 입력하세요"></textarea><br><br>
        <button onclick="submitReport()">신고하기</button>
        <button onclick="closeReportPopup()">닫기</button>
    </div>
    `;
    const wrapper = document.createElement("div");
    wrapper.innerHTML = html;
    document.body.appendChild(wrapper);
}

function closeReportPopup() {
    const popup = document.getElementById("reportPopup");
    if (popup) popup.remove();
}

function submitReport() {
    const reportedUid = document.getElementById("reportedUid").value;
    const reason = document.getElementById("reason").value;

    // 입력값 유효성 검사
    if (!reportedUid || !reason) {
        alert("신고 대상 UID와 신고 사유를 모두 입력해주세요.");
        return;
    }

    const token = localStorage.getItem("authToken");
    console.log("클라이언트에서 가져온 토큰:", token);
    // 토큰이 없거나 유효하지 않으면 재로그인 유도
    if (!token) {
        alert("로그인 상태가 아닙니다. 다시 로그인 해주세요.");
        window.location.href = "/login";  // 로그인 페이지로 리다이렉트
        return;
    }

    console.log("클라이언트에서 가져온 토큰:", token);

    fetch("/api/reports/create", {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ reportedUid, reason })
    })
        .then(res => res.json().then(data => ({ status: res.status, body: data })))
        .then(({ status, body }) => {
            if (status === 200) {
                // 신고가 성공적으로 처리되면
                alert(body.message);
                closeReportPopup();
                loadReportList();  // 신고 목록을 새로 고침
            } else {
                // 실패 시 오류 메시지를 그대로 표시
                alert(body.message || "신고 처리 중 오류가 발생했습니다.");
            }
        })
        .catch(err => {
            console.error(err);
            console.log("신고 대상 UID:", reportedUid);
            console.log("신고 사유:", reason);
            alert("신고 처리 중 오류가 발생했습니다.");
        });
}
