function updateSpan() {
    const date = document.getElementById("modal-date").value;
    const people = document.getElementById("modal-people").value;
    const region = document.getElementById("modal-region").value;
    const fish = document.getElementById("modal-fishType").value;

    const today = new Date().toISOString().split("T")[0];

    document.getElementById("selected-date").textContent = date || today;
    document.getElementById("selected-people").textContent = people || "1";
    document.getElementById("selected-region").textContent = region || "전체";
    document.getElementById("selected-fish").textContent = fish || "전체";
}

window.addEventListener("DOMContentLoaded", () => {
    updateSpan();
});