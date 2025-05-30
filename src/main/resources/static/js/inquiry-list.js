async function loadInquiries() {
    const token = localStorage.getItem('authToken');
    const partnerUno = 38; // 예시로 하드코딩, 실제 로그인된 파트너 uno를 넣어야 함

    if (!token) {
        alert("로그인이 필요합니다.");
        return;
    }

    const res = await fetch(`/api/inquiries/partner-inquiries?partnerUno=${partnerUno}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });

    const container = document.getElementById("inquiryContainer");
    container.innerHTML = "";

    if (res.ok) {
        const data = await res.json();
        console.log(data)
        if (data.length === 0) {
            container.innerHTML = "<p>문의가 없습니다.</p>";
            return;
        }

        data.forEach(inquiry => {
            const div = document.createElement("div");
            div.style.border = "1px solid #ccc";
            div.style.padding = "15px";
            div.style.marginBottom = "10px";


            div.innerHTML = `
    <p><strong>제목:</strong> ${inquiry.title}</p>
    <p><strong>내용:</strong> ${inquiry.content}</p>
    <p><strong>유형:</strong> ${inquiry.inquiryType}</p>
    <p><strong>작성자:</strong> ${inquiry.userName} (${inquiry.userUid})</p>
    <p><strong>업소명:</strong> ${inquiry.partnerName || '-'}</p>
    <p><strong>작성일:</strong> ${new Date(inquiry.createdAt).toLocaleString()}</p>
    <p><strong>답변 여부:</strong> ${inquiry.answerContent ? "✅" : "❌"}</p>
    <button onclick="toggleAnswerBox(${inquiry.id})">답변 ${inquiry.answerContent ? "수정" : "작성"}</button>
    <div id="answerBox_${inquiry.id}" style="display: none; margin-top: 10px;">
        <label for="answer_${inquiry.id}"><strong>답변:</strong></label><br />
        <textarea id="answer_${inquiry.id}" rows="3" cols="60">${inquiry.answerContent || ""}</textarea><br />
        <button onclick="submitAnswer(${inquiry.id})">답변 저장</button>
    </div>
`;
            container.appendChild(div);
        });
    } else {
        container.innerHTML = "<p>문의 목록을 불러오지 못했습니다.</p>";
    }
}
function toggleAnswerBox(inquiryId) {
    const box = document.getElementById(`answerBox_${inquiryId}`);
    box.style.display = box.style.display === 'none' ? 'block' : 'none';
}


async function submitAnswer(inquiryId) {
    const token = localStorage.getItem('authToken');
    const textarea = document.getElementById(`answer_${inquiryId}`);
    const answerContent = textarea.value;

    const res = await fetch(`/api/inquiries/${inquiryId}/answer`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ answerContent })
    });

    if (res.ok) {
        alert("답변이 저장되었습니다.");
        loadInquiries();
    } else {
        alert("답변 저장 실패");
    }
}

// 탭 이동 시마다 문의 목록 갱신
function showTab(tabId) {
    console.log("showTab 호출됨:", tabId);
    document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
    document.getElementById(tabId).classList.add('active');
    if (tabId === 'inquiryList') loadInquiries();
}

document.querySelector("button[onclick=\"showTab('inquiryList')\"]")
    .addEventListener('click', () => showTab('inquiryList'));

loadInquiries();