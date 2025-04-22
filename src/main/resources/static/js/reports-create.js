
    // 팝업창 HTML을 동적으로 생성
    function openReportPopup() {
    // 이미 팝업이 있으면 다시 만들지 않음
    if (document.getElementById("reportPopup")) return;

    const popupHtml = `
            <div id="reportPopup" style="position:fixed; top:30%; left:50%; transform:translate(-50%, -30%); background:#fff; padding:20px; border:1px solid #ccc; border-radius:8px; box-shadow:0 4px 8px rgba(0,0,0,0.1); z-index:1000;">
                <h3>사용자 신고</h3>
                <label for="reportedUid">신고할 사용자 UID:</label><br>
                <input type="text" id="reportedUid" placeholder="사용자 UID"><br><br>

                <label for="reason">신고 사유:</label><br>
                <textarea id="reason" placeholder="신고 사유를 입력하세요" rows="4" cols="30"></textarea><br><br>

                <button onclick="submitReport()">제출</button>
                <button onclick="closeReportPopup()">닫기</button>
            </div>
        `;

    const wrapper = document.createElement("div");
    wrapper.innerHTML = popupHtml;
    document.body.appendChild(wrapper);
}

    function closeReportPopup() {
    const popup = document.getElementById("reportPopup");
    if (popup) popup.remove();
}



    function submitReport() {
        const reportedUid = document.getElementById("reportedUid").value;
        const reason = document.getElementById("reason").value;

        const loggedInUserUno = localStorage.getItem("userUno");

        const reportData = {
            reportedUid: reportedUid,
            reason: reason,
            loggedInUserUno : loggedInUserUno
        };

        fetch("/api/reports/create", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("authToken"),
                "Content-Type": "application/json"
            },
            body: JSON.stringify(reportData)
        })
            .then(res => {
                console.log("서버 응답 상태:", res.status);  // 응답 상태 로그

                if (!res.ok) {
                    return res.json().then(errorData => {
                        console.error("서버 오류 데이터:", errorData);  // 서버에서 반환한 오류 데이터 출력
                        throw new Error("신고 실패: " + (errorData.message || "알 수 없는 오류"));
                    });
                }
                return res.json();
            })
            .then(data => {
                console.log("서버 응답 데이터:", data);  // 성공 시 응답 데이터 로그
                alert(data.message || "신고가 접수되었습니다.");
                closeReportPopup();
            })
            .catch(err => {
                console.error("신고 처리 중 오류 발생:", err);  // 오류 발생 시 로그
                alert("신고 처리 중 오류가 발생했습니다.");
            });
    }
