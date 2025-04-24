function loadEventData(page) {
    const type = document.getElementById("event-search-type").value;
    const keyword = document.getElementById("event-search-keyword").value;

    fetch(`/api/event?page=${page}&type=${type}&keyword=${keyword}`, {
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
    <td><a href="/event/${event.eventId}?page=${page}">${event.title}</a></td>
    <td>${event.startAt && event.endAt ? `${event.startAt} ~ ${event.endAt}` : "상시 이벤트"}</td>
    <td>${event.eventStatus}</td>
            `;

                tbody.appendChild(row);
            });

            renderPagination(data);
        });
}

function renderPagination(data) {
    const paginationDiv = document.getElementById("event-pagination");
    paginationDiv.innerHTML = "";

    for (let i = 0; i < data.totalPages; i++) {
        const btn = document.createElement("button");
        btn.className = "page-btn";
        btn.innerText = i + 1;
        btn.onclick = () => loadEventData(i);
        paginationDiv.appendChild(btn);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    loadEventData(0);
});