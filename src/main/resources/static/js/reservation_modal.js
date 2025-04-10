function updateSpan() {
    const date = document.getElementById("modal-date").value;
    const people = document.getElementById("modal-people").value;
    const region = document.getElementById("modal-region").value;
    const fishType = document.getElementById("modal-fishType").value;

    const today = new Date().toISOString().split("T")[0];

    document.getElementById("selected-date").textContent = date || today;
    document.getElementById("selected-people").textContent = people || "1";
    document.getElementById("selected-region").textContent = region || "전체";
    document.getElementById("selected-fish").textContent = fishType || "전체";
}

document.getElementById("search-btn").addEventListener('click', function() {

    const today = new Date().toISOString().split("T")[0];

    const date = document.getElementById("modal-date").value || today;
    const people = document.getElementById("modal-people").value || 1;
    const region = document.getElementById("modal-region").value || "전체";
    const fishType = document.getElementById("modal-fishType").value || "전체";

    const url = `/reservation?date=${date}&people=${people}&region=${region}&fishType=${fishType}`;
    window.location.href = url;
})

window.addEventListener("DOMContentLoaded", () => {
    updateSpan();
});