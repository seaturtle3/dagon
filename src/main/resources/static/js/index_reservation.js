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

    console.log(`date : ${date}, people : ${people}, region : ${region}, fishType : ${fishType}`);
}


// 검색하기 버튼 클릭 시 선택한 옵션값과 함께 바다/민물 페이지로 이동
document.getElementById("search-btn").addEventListener('click', function () {
    const today = new Date().toISOString().split("T")[0];
    const date = document.getElementById("modal-date").value || today;
    const people = document.getElementById("modal-people").value || 1;
    const region = document.getElementById("modal-region").value || "전체";
    const fishType = document.getElementById("modal-fishType").value || "전체";
    const mainType = document.querySelector('input[name="waterType"]:checked').value;

    const url = mainType === "freshwater"
        ? `/fishing/freshwater?mainType=${mainType}&date=${date}&people=${people}&region=${region}&fishType=${fishType}`
        : `/fishing/sea?mainType=${mainType}&date=${date}&people=${people}&region=${region}&fishType=${fishType}`;

    window.location.href = url;

});

// 페이지 새로고침 로딩
window.addEventListener("DOMContentLoaded", () => {
    updateSpan();
    console.log("index 페이지 로딩 완료")
});