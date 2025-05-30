function loadEventData(page) {
    const type = document.getElementById("event-search-type").value;
    const keyword = document.getElementById("event-search-keyword").value;
    const status = document.getElementById("event-status").value;

    const url = new URL(`/api/event`, window.location.origin);
    url.searchParams.set("page", page);
    url.searchParams.set("type", type);
    url.searchParams.set("keyword", keyword);
    if (status) url.searchParams.set("status", status); // 상태 있을 때만

    fetch(url, {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("authToken")
        }
    })
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("event-table-body");
            tbody.innerHTML = "";

            data.content.forEach((event, index) => {
                const row = document.createElement("tr");

                row.innerHTML = `
                    <td>${event.isTop ? "✔" : ""}</td>
                    <td>${index + 1}</td>
                    <td>${event.thumbnailUrl ? `<img src="${event.thumbnailUrl}" class="event-thumbnail">` : ""}</td>
                    <td><a href="/admin/event/${event.eventId}?page=${page}">${event.title}</a></td>
                    <td>${event.startAt && event.endAt ? `${event.startAt} ~ ${event.endAt}` : "상시 이벤트"}</td>
                    <td>${event.eventStatus}</td>
                `;

                tbody.appendChild(row);
            });

            renderEventPagination(data);
        });
}

// 이벤트용 페이징
function renderEventPagination(data) {
    const paginationDiv = document.getElementById("event-pagination");
    paginationDiv.innerHTML = "";

    const totalPages = data.totalPages;
    const currentPage = data.pageable.pageNumber;

    if (currentPage > 0) {
        const prevButton = document.createElement('button');
        prevButton.innerText = '이전';
        prevButton.onclick = () => loadEventData(currentPage - 1);
        paginationDiv.appendChild(prevButton);
    }

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.innerText = i + 1;
        pageButton.onclick = () => loadEventData(i);
        if (i === currentPage) {
            pageButton.style.fontWeight = 'bold';
        }
        paginationDiv.appendChild(pageButton);
    }

    if (currentPage < totalPages - 1) {
        const nextButton = document.createElement('button');
        nextButton.innerText = '다음';
        nextButton.onclick = () => loadEventData(currentPage + 1);
        paginationDiv.appendChild(nextButton);
    }
}





document.addEventListener("DOMContentLoaded", () => {
    loadEventData(0);
    loadNotificationData(0);
});