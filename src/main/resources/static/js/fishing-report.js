document.addEventListener("DOMContentLoaded", () => {
    loadFishingReports();
});

async function loadFishingReports() {
    const token = localStorage.getItem("authToken");
    if (!token) {
        alert("로그인이 필요합니다.");
        return;
    }

    const response = await fetch("/api/fishing-report/mine", {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    if (!response.ok) {
        alert("조황정보를 불러오지 못했습니다.");
        return;
    }

    const reports = await response.json();
    const container = document.getElementById("fishingReportContainer");
    container.innerHTML = ""; // 초기화

    if (reports.length === 0) {
        container.innerHTML = "<p>등록된 조황정보가 없습니다.</p>";
        return;
    }

    reports.forEach(report => {
        const div = document.createElement("div");
        div.className = "report-item";
        div.dataset.frId = report.frId;

        div.innerHTML = `
            <h3>제목:${report.title}</h3>
            <p>조항 날짜:${new Date(report.fishingAt).toLocaleDateString()}</p>
            <img src="${report.thumbnailUrl || '/img/default-thumbnail.png'}" alt="썸네일" width="200" />
            <p>내용:${report.content.slice(0, 100)}...</p>
            <button class="btn-edit">수정</button>
            <button class="btn-delete">삭제</button>
            <div class="edit-form-container" style="display:none; margin-top: 10px;"></div>
            <hr/>
        `;

        container.appendChild(div);

        const btnEdit = div.querySelector(".btn-edit");
        const btnDelete = div.querySelector(".btn-delete");
        const editFormContainer = div.querySelector(".edit-form-container");

        btnEdit.addEventListener("click", () => toggleEditForm(report, editFormContainer, btnEdit));
        btnDelete.addEventListener("click", () => deleteReport(report.frId));
    });
}

function toggleEditForm(report, container, editBtn) {
    if (container.style.display === "block") {
        container.style.display = "none";
        editBtn.textContent = "수정";
    } else {
        container.style.display = "block";
        editBtn.textContent = "닫기";

        container.innerHTML = `
            <form class="edit-report-form" enctype="multipart/form-data">
                <label>제목:<br/>
                    <input type="text" name="title" value="${escapeHtml(report.title)}" required />
                </label><br/><br/>
                <label>조황 날짜:<br/>
                    <input type="date" name="fishingAt" value="${formatDateForInput(report.fishingAt)}" required />
                </label><br/><br/>
                <label>내용:<br/>
                    <textarea name="content" rows="5" cols="40" required>${escapeHtml(report.content)}</textarea>
                </label><br/><br/>
                <label>썸네일 이미지 업로드:<br/>
                    <input type="file" name="thumbnailFile" accept="image/*" />
                </label><br/>
                <img id="thumbnailPreview" src="${report.thumbnailUrl || '/img/default-thumbnail.png'}" alt="썸네일 미리보기" width="200" /><br/><br/>
                <button type="submit">수정 완료</button>
            </form>
        `;

        const form = container.querySelector(".edit-report-form");
        const fileInput = form.querySelector('input[name="thumbnailFile"]');
        const previewImg = form.querySelector("#thumbnailPreview");

        fileInput.addEventListener("change", (e) => {
            const file = e.target.files[0];
            if (!file) {
                previewImg.src = report.thumbnailUrl || '/img/default-thumbnail.png';
                return;
            }
            const reader = new FileReader();
            reader.onload = evt => {
                previewImg.src = evt.target.result;
            };
            reader.readAsDataURL(file);
        });

        form.addEventListener("submit", async (e) => {
            e.preventDefault();

            const token = localStorage.getItem("authToken");
            if (!token) {
                alert("로그인이 필요합니다.");
                return;
            }

            const rawDate = form.fishingAt.value;
            const fishingAtWithTime = rawDate ? rawDate + "T00:00:00" : null;

            const dto = {
                title: form.title.value,
                fishingAt: fishingAtWithTime,
                content: form.content.value,
            };

            const formData = new FormData();
            formData.append("dto", new Blob([JSON.stringify(dto)], { type: "application/json" }));

            if (fileInput.files[0]) {
                formData.append("thumbnailFile", fileInput.files[0]);
            }

            try {
                const response = await fetch(`/api/fishing-report/${report.frId}`, {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${token}`
                    },
                    body: formData
                });

                if (response.ok) {
                    alert("수정 완료");
                    container.style.display = "none";
                    editBtn.textContent = "수정";
                    loadFishingReports();
                } else {
                    const errorData = await response.json();
                    alert("수정 실패: " + (errorData.message || "오류 발생"));
                }
            } catch (err) {
                console.error("수정 중 오류 발생:", err);
                alert("수정 중 오류가 발생했습니다.");
            }
        });
    }
}

async function deleteReport(frId) {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    const token = localStorage.getItem("authToken");
    const response = await fetch(`/api/fishing-report/${frId}`, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    if (response.ok) {
        alert("삭제 완료");
        loadFishingReports();
    } else {
        alert("삭제 실패");
    }
}

// 유저 입력값 HTML 이스케이프 처리 함수 (XSS 예방)
function escapeHtml(text) {
    if (!text) return "";
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// input type="date"용 날짜 포맷 변환 (ISO 8601)
function formatDateForInput(dateString) {
    if (!dateString) return "";
    const d = new Date(dateString);
    const yyyy = d.getFullYear();
    let mm = d.getMonth() + 1;
    let dd = d.getDate();
    if (mm < 10) mm = "0" + mm;
    if (dd < 10) dd = "0" + dd;
    return `${yyyy}-${mm}-${dd}`;
}
