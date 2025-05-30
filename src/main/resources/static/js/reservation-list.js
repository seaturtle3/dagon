const token = localStorage.getItem("authToken");

function showTab(tabId) {
    document.querySelectorAll(".tab-content").forEach(div => {
        div.style.display = "none";
    });
    document.getElementById(tabId).style.display = "block";
}

async function loadReservations() {
    if (!token) {
        alert("로그인이 필요합니다.");
        return;
    }

    try {
        const res = await fetch("http://localhost:8095/api/reservation/partner", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!res.ok) {
            throw new Error("예약 목록을 불러오는 데 실패했습니다.");
        }

        const reservations = await res.json(); // 배열임
        window.currentReservations = reservations;

        const list = document.getElementById("reservationList");
        list.innerHTML = "";

        reservations.forEach(r => {
            const li = document.createElement("li");
            li.innerHTML = `
                <strong>${r.prod_name || r.productName}</strong> - 예약자: ${r.userName} - 날짜: ${formatDateTime(r.fishingAt)}
                <button onclick="showReservationDetail(${r.reservationId})">상세보기</button>
            `;
            list.appendChild(li);
        });

        showTab("reservationSummary");

    } catch (error) {
        console.error(error);
        alert(error.message);
    }
}

function showReservationDetail(reservationId) {
    const reservations = window.currentReservations || [];
    const r = reservations.find(x => x.reservationId === reservationId);
    if (!r) {
        alert("예약 정보를 찾을 수 없습니다.");
        return;
    }

    const detailDiv = document.getElementById("reservationDetailContent");
    detailDiv.innerHTML = `
        <p><strong>예약자:</strong> ${r.userName}</p>
        <p><strong>상품명:</strong> ${r.prod_name || r.productName}</p>
        <p><strong>옵션:</strong> ${r.optionName}</p>
        <p><strong>인원수:</strong> ${r.numPerson}</p>
        <p><strong>출조일시:</strong> ${formatDateTime(r.fishingAt)}</p>
        <p><strong>결제일시:</strong> ${formatDateTime(r.paidAt)}</p>
        <p><strong>상태:</strong> ${r.reservationStatus}</p>
        <p><strong>결제수단:</strong> ${r.paymentsMethod}</p>
        <button onclick="cancelReservation(${r.reservationId})">예약 취소</button>
        <button onclick="showTab('reservationSummary')">← 목록으로</button>
    `;

    showTab("reservationDetail");
}

async function cancelReservation(reservationId) {
    if (!confirm("정말 예약을 취소하시겠습니까?")) return;

    try {
        const res = await fetch(`http://localhost:8095/api/reservation/cancel/${reservationId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        const result = await res.text();

        if (res.ok) {
            alert(result);
            await loadReservations();
        } else {
            alert(`취소 실패: ${result}`);
        }
    } catch (error) {
        console.error(error);
        alert("예약 취소 중 오류가 발생했습니다.");
    }
}

function formatDateTime(dateString) {
    if (!dateString) return "-";
    const date = new Date(dateString);
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, "0");
    const dd = String(date.getDate()).padStart(2, "0");
    const hh = String(date.getHours()).padStart(2, "0");
    const mi = String(date.getMinutes()).padStart(2, "0");
    return `${yyyy}-${mm}-${dd} ${hh}:${mi}`;
}

document.addEventListener("DOMContentLoaded", () => {
    loadReservations();
});
