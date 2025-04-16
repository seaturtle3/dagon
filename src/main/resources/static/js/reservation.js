window.addEventListener("DOMContentLoaded", () => {
    updateSpanFromURL()
    console.log("바다 페이지 로딩 완료")
});

// 모달에서 옵션 선택 후 화면에 출력되는 설정
function updateSpan() {
    const date = document.getElementById("modal-date").value;
    const people = document.getElementById("modal-people").value;
    const region = document.getElementById("modal-region").value;
    const fishType = document.getElementById("modal-fishType").value;
    const today = new Date().toISOString().split("T")[0];

    document.getElementById("selected-date").textContent = date || today;
    document.getElementById("selected-people").textContent = people || 1;
    document.getElementById("selected-region").textContent = region || "전체";
    document.getElementById("selected-fish").textContent = fishType || "전체";
}

function handleApply() {
    updateSpan(); // 화면에 선택 값 반영

    const selectedRadio = document.querySelector('input[name="waterType"]:checked');
    const type = selectedRadio ? selectedRadio.value : 'sea';

    const date = document.getElementById("modal-date").value;
    const people = document.getElementById("modal-people").value;
    const region = document.getElementById("modal-region").value;
    const fishType = document.getElementById("modal-fishType").value;

    const url = `/${type}?type=${type}&date=${date}&people=${people}&region=${region}&fishType=${fishType}`;
    window.location.href = url;
}

function updateSpanFromURL() {
    const params = new URLSearchParams(window.location.search);

    const date = params.get("date") || new Date().toISOString().split("T")[0];
    const people = params.get("people") || 1;
    const region = params.get("region") || "전체";
    const fishType = params.get("fishType") || "전체";
    const type = params.get("type") || "sea";

    // 화면 반영
    document.getElementById("selected-date").textContent = date;
    document.getElementById("selected-people").textContent = people;
    document.getElementById("selected-region").textContent = region;
    document.getElementById("selected-fish").textContent = fishType;

    // 모달 초기값 설정
    document.getElementById("modal-date").value = date;
    document.getElementById("modal-people").value = people;
    document.getElementById("modal-region").value = region;
    document.getElementById("modal-fishType").value = fishType;

    document.getElementById(type).checked = true;
}



// 세부사항 변경 누를 시 화면에 선택된 옵션과 동일한 값을 가져옴
function syncModalValues() {
    const date = document.getElementById("selected-date").textContent.trim();
    const people = document.getElementById("selected-people").textContent.trim();
    const region = document.getElementById("selected-region").textContent.trim();
    const fishType = document.getElementById("selected-fish").textContent.trim();
    const type = document.querySelector('input[name=waterType]:checked').value;

    if (type === "freshwater") {
        document.getElementById("freshwater").checked = true;
    } else {
        document.getElementById("sea").checked = true;
    }

    if (date) document.getElementById("modal-date").value = date;
    document.getElementById("modal-people").value = people
    document.getElementById("modal-region").value = region
    document.getElementById("modal-fishType").value = fishType

    const typeText = type === "freshwater" ? "민물" : "바다";

    console.log(`type : ${typeText}, date : ${date}, people : ${people}, region : ${region}, fishType : ${fishType}`);
}