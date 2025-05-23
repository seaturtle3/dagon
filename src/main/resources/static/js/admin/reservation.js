const ReservationApp = (() => {
    const pageSize = 10;
    let currentPage = 0;

    async function loadReservationData(page) {
        currentPage = page;

        // 검색어와 검색기준 읽기
        const searchType = document.getElementById("reservation-search-type").value;
        const keyword = document.getElementById("reservation-search-keyword").value.trim();

        let url = `/api/reservation?page=${page}&size=${pageSize}&sortBy=createdAt&direction=desc`;

        // 검색어가 있으면 쿼리 파라미터 추가
        if (keyword) {
            url += `&searchType=${encodeURIComponent(searchType)}&keyword=${encodeURIComponent(keyword)}`;
        }

        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error("네트워크 오류");
            const data = await response.json();

            const tbody = document.getElementById("reservation-table-body");
            tbody.innerHTML = "";

            data.content.forEach(reservation => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${reservation.reservationId}</td>
                    <td>${reservation.prod_name || reservation.productName}</td>
                    <td>${reservation.optionName}</td>
                    <td>${reservation.userName}</td>
                    <td>${formatDateTime(reservation.fishingAt)}</td>
                    <td>${reservation.numPerson}</td>
                    <td>${reservation.reservationStatus}</td>
                    <td>${reservation.paymentsMethod}</td>
                    <td>${formatDateTime(reservation.paidAt)}</td>
                     <td><button class="cancel-btn" data-id="${reservation.reservationId}">취소</button></td>
                `;
                tbody.appendChild(tr);
            });

            document.querySelectorAll(".cancel-btn").forEach(button => {
                button.addEventListener("click", (e) => {
                    const reservationId = e.target.getAttribute("data-id");
                    cancelReservation(reservationId);
                });
            });

            renderPagination(data.number, data.totalPages);
        } catch (error) {
            alert("데이터를 불러오는 중 오류가 발생했습니다.");
            console.error(error);
        }
    }


    async function cancelReservation(reservationId) {
        if (!confirm("예약을 정말 취소하시겠습니까?")) return;

        try {
            // JWT 토큰 가져오기 (예: localStorage에 저장했다고 가정)
            const token = localStorage.getItem("authToken");
            if (!token) {
                alert("로그인이 필요합니다.");
                return;
            }

            const response = await fetch(`/api/reservation/cancel/${reservationId}`, {
                method: "DELETE",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            if (response.ok) {
                alert("예약이 성공적으로 취소되었습니다.");
                loadReservationData(currentPage); // 현재 페이지 다시 로드
            } else {
                const errorText = await response.text();
                alert(`취소 실패: ${errorText}`);
            }
        } catch (error) {
            alert("예약 취소 중 오류가 발생했습니다.");
            console.error(error);
        }
    }




    function formatDateTime(dateTimeStr) {
        if (!dateTimeStr) return "";
        const dt = new Date(dateTimeStr);
        return dt.toLocaleString();
    }

    function renderPagination(current, totalPages) {
        const pagination = document.getElementById("reservation-pagination");
        pagination.innerHTML = "";

        const prevBtn = document.createElement("button");
        prevBtn.textContent = "이전";
        prevBtn.disabled = current === 0;
        prevBtn.onclick = () => loadReservationData(current - 1);
        pagination.appendChild(prevBtn);

        const pageInfo = document.createElement("span");
        pageInfo.textContent = ` ${current + 1} / ${totalPages} 페이지 `;
        pagination.appendChild(pageInfo);

        const nextBtn = document.createElement("button");
        nextBtn.textContent = "다음";
        nextBtn.disabled = current + 1 >= totalPages;
        nextBtn.onclick = () => loadReservationData(current + 1);
        pagination.appendChild(nextBtn);
    }

    // 초기 데이터 로드
    // DOMContentLoaded 시 이벤트 연결
    document.addEventListener("DOMContentLoaded", () => {
        document.getElementById("reservation-search-button").addEventListener("click", () => {
            loadReservationData(0);
        });
        loadReservationData(0);
    });

    return {
        loadReservationData
    };
})();