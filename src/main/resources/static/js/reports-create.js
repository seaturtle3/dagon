
    // 팝업창 HTML을 동적으로 생성
    function openReportPopup() {
        if (document.getElementById("reportPopup")) return;

        const html = `
        <div id="reportPopup" style="position:fixed; top:30%; left:50%; transform:translate(-50%, -30%); background:white; padding:20px; border-radius:10px; border:1px solid #ccc; z-index:9999;">
            <h3>사용자 신고</h3>
            <label>신고 대상 UID:</label><br>
            <input type="number" id="reportedUid"><br><br>
            <label>신고 사유:</label><br>
            <textarea id="reason" rows="4" cols="30"></textarea><br><br>
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
        const token = localStorage.getItem("authToken");

        fetch("/api/reports/create", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({reportedUid, reason})
        })
            .then(res => res.json().then(data => ({status: res.status, body: data})))
            .then(({status, body}) => {
                if (status === 200) {
                    alert(body.message);
                    closeReportPopup();
                    loadReportList();
                } else {
                    throw new Error(body.message || "알 수 없는 오류");
                }
            })
            .catch(err => {
                console.error(err);
                console.log("신고 UID:", reportedUid);
                console.log("신고 사유:", reason);
                alert("신고 처리 중 오류가 발생했습니다.");
            });

    }
