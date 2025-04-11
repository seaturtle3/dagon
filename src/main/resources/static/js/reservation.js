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

function syncModalValues() {
    // span 값 읽기
    const date = document.getElementById("selected-date").textContent.trim();
    const people = document.getElementById("selected-people").textContent.trim();
    const region = document.getElementById("selected-region").textContent.trim();
    const fishType = document.getElementById("selected-fish").textContent.trim();

    // 모달 input/select에 값 세팅
    if (date) document.getElementById("modal-date").value = date;
    document.getElementById("modal-people").value = people
    document.getElementById("modal-region").value = region
    document.getElementById("modal-fishType").value = fishType

    console.log(`date : ${date}, people : ${people}, region : ${region}, fishType : ${fishType}`);

}
