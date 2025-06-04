document.getElementById("productForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const token = localStorage.getItem('authToken');

    const productData = {
        name: document.getElementById("prodName").value,
        description: document.getElementById("prodDesc").value,
        price: parseFloat(document.getElementById("prodPrice").value)
        // partnerUno는 서버에서 token을 통해 추출하므로 JS에서 전달할 필요 없음
    };

    const response = await fetch("/api/partner/product/create", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(productData)
    });

    if (response.ok) {
        alert("상품이 등록되었습니다.");
        document.getElementById("productForm").reset();
        loadProductList();
    } else {
        alert("상품 등록 실패");
    }
});

const item = document.createElement("div");
item.className = "product-item";

async function loadProductList() {
    const token = localStorage.getItem('authToken');

    const response = await fetch(`/api/partner/product/my-products`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    if (response.ok) {
        const products = await response.json();
        renderProductList(products);
    } else {
        alert("상품을 불러오지 못했습니다.");
    }
}


function loadProductRegisterForm() {
    const container = document.getElementById("productRegisterContainer");
    if (!container) return;

    container.innerHTML = `
        <h2>상품 등록</h2>
        <form id="productForm">
            <label for="prodName">상품명:</label><br />
            <input type="text" id="prodName" name="prodName"><br /><br />

            <label for="prodRegion">지역:</label><br />
            <select id="prodRegion" name="prodRegion">
                <option value="">선택</option>
                <option value="GANGWON">강원도</option>
                <option value="GYEONGGI">경기도</option>
                <option value="GYEONGNAM">경상남도</option>
                <option value="GYEONGBUK">경상북도</option>
                <option value="GWANGJU">광주</option>
                <option value="DAEGU">대구</option>
                <option value="DAEJEON">대전</option>
                <option value="BUSAN">부산</option>
                <option value="SEOUL">서울</option>
                <option value="SEJONG">세종</option>
                <option value="ULSAN">울산</option>
                <option value="INCHEON">인천</option>
                <option value="JEONNAM">전라남도</option>
                <option value="JEONBUK">전라북도</option>
                <option value="JEJU">제주도</option>
                <option value="CHUNGNAM">충청남도</option>
                <option value="CHUNGBUK">충청북도</option>
            </select><br /><br />

            <label for="mainType">메인 타입:</label><br />
            <select id="mainType" name="mainType" required>
                <option value="">선택</option>
                <option value="SEA">바다낚시</option>
                <option value="FRESHWATER">민물낚시</option>
            </select><br /><br />

            <label for="subType">서브 타입:</label><br />
            <select id="subType" name="subType" required>
                <option value="">선택</option>
            </select><br /><br />

            <label for="prodDescription">설명:</label><br />
            <textarea id="prodDescription" name="prodDescription" rows="4" cols="50"></textarea><br /><br />

            <label for="maxPerson">최대 인원:</label><br />
            <input type="number" id="maxPerson" name="maxPerson"><br /><br />

            <label for="minPerson">최소 인원:</label><br />
            <input type="number" id="minPerson" name="minPerson"><br /><br />

            <label for="weight">무게:</label><br />
            <input type="number" id="weight" name="weight" step="0.1"><br /><br />

            <label for="prodAddress">주소:</label><br />
            <input type="text" id="prodAddress" name="prodAddress"><br /><br />

            <label for="prodEvent">이벤트:</label><br />
            <input type="text" id="prodEvent" name="prodEvent"><br /><br />

            <label for="prodNotice">공지사항:</label><br />
            <input type="text" id="prodNotice" name="prodNotice"><br /><br />

            <button type="submit" id="submitProductBtn">등록</button>
        </form>
    `;

    const subTypeOptions = {
        SEA: [
            { value: "BREAK_WATER", label: "방파제" },
            { value: "ROCKY_SHORE", label: "갯바위" },
            { value: "ESTUARY", label: "하구" },
            { value: "BOAT", label: "선상" },
            { value: "MUD_FLAT", label: "갯벌" },
            { value: "ARTIFICIAL", label: "인공낚시터" },
            { value: "OPEN_SEA", label: "해상" },
            { value: "BEACH", label: "해변" },
            { value: "REEF", label: "암초" },
            { value: "OTHER_SEA", label: "기타_바다" }
        ],
        FRESHWATER: [
            { value: "RIVER", label: "강" },
            { value: "RESERVOIR", label: "저수지" },
            { value: "DAM", label: "댐" },
            { value: "POND", label: "연못" },
            { value: "SMALL_LAKE", label: "소류지" },
            { value: "CANAL", label: "수로" },
            { value: "FLOATING_PLATFORM", label: "좌대" },
            { value: "OPEN_AREA", label: "노지" },
            { value: "OTHER_FRESHWATER", label: "기타_민물" }
        ]
    };

    const mainTypeSelect = document.getElementById("mainType");
    const subTypeSelect = document.getElementById("subType");

    function populateSubTypes() {
        const mainType = mainTypeSelect.value;
        subTypeSelect.innerHTML = '<option value="">선택</option>';

        if (mainType && subTypeOptions[mainType]) {
            subTypeSelect.disabled = false;
            subTypeOptions[mainType].forEach(opt => {
                const option = document.createElement("option");
                option.value = opt.value;
                option.textContent = opt.label;
                subTypeSelect.appendChild(option);
            });
        } else {
            subTypeSelect.disabled = true;
        }
    }

    mainTypeSelect.addEventListener("change", populateSubTypes);
    populateSubTypes(); // 초기 서브타입 세팅

    // 등록 버튼 (form submit 이벤트) 처리
    const productForm = document.getElementById("productForm");
    productForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const token = localStorage.getItem("authToken");
        if (!token) {
            alert("로그인이 필요합니다.");
            return;
        }

        const getValue = id => document.getElementById(id)?.value || "";

        const productData = {
            prodName: getValue("prodName"),
            prodRegion: getValue("prodRegion"),
            mainType: getValue("mainType"),
            subType: getValue("subType"),
            prodDescription: getValue("prodDescription"),
            maxPerson: Number(getValue("maxPerson")),
            minPerson: Number(getValue("minPerson")),
            weight: parseFloat(getValue("weight")),
            prodAddress: getValue("prodAddress"),
            prodEvent: getValue("prodEvent"),
            prodNotice: getValue("prodNotice")
        };

        try {
            const response = await fetch("/api/partner/product", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(productData)
            });

            if (response.ok) {
                alert("상품이 성공적으로 등록되었습니다.");
                productForm.reset();
                loadProductList();
                showTab("productManagement");
            } else {
                const errorData = await response.json();
                alert("등록 실패: " + (errorData.message || "오류 발생"));
            }
        } catch (err) {
            console.error("등록 중 오류 발생:", err);
            alert("등록 중 오류가 발생했습니다.");
        }
    });
}




function renderProductList(products) {
    const container = document.getElementById("productListContainer");
    container.innerHTML = "";

    if (!products.length) {
        container.innerHTML = "<p>등록된 상품이 없습니다.</p>";
        return;
    }

    products.forEach(product => {


        const item = document.createElement("div");
        item.className = "product-item";

        // 요약 정보
        const summary = document.createElement("div");
        summary.className = "summary";
        summary.innerHTML = `
            <strong>${product.prodName}</strong> - ${product.prodRegion || "지역 없음"} - ${product.prodPrice ? product.prodPrice + "원" : "가격 없음"}
        `;

        // 상세 정보
        const details = document.createElement("div");
        details.className = "details hidden";
        details.innerHTML = `
            <p>설명: ${product.prodDescription}</p>
            <p>바다&민물: ${product.mainType || "없음"}</p>
            <p>낚시 분류: ${product.subType || "없음"}</p>
            <p>최대 인원: ${product.maxPerson || "없음"}</p>
            <p>최소 인원: ${product.minPerson || "없음"}</p>
            <p>무게: ${product.weight || "없음"}</p>
            <p>주소: ${product.prodAddress || "없음"}</p>
            <p>이벤트: ${product.prodEvent || "없음"}</p>
            <p>공지사항: ${product.prodNotice || "없음"}</p>
            <p>등록일: ${product.createdAt ? new Date(product.createdAt).toLocaleDateString() : "없음"}</p>
            <button onclick="editProduct(${product.prodId})">수정</button>
            <button onclick="deleteProduct(${product.prodId})">삭제</button>
        <button onclick="toggleReportForm(${product.prodId})">🎣 조황 등록</button>
             <div id="reportForm-${product.prodId}" class="report-form hidden">
        <input type="text" placeholder="제목" id="reportTitle-${product.prodId}" /><br>
        <textarea placeholder="내용" id="reportContent-${product.prodId}"></textarea><br>
        <input type="date" id="reportDate-${product.prodId}" /><br>
        <input type="file" id="reportThumb-${product.prodId}" /><br>
        <button onclick="submitFishingReport(${product.prodId})">등록하기</button>
    </div>
            
        `;



        // 요약 클릭 시 상세 토글
        summary.addEventListener("click", () => {
            details.classList.toggle("hidden");
        });



        item.appendChild(summary);
        item.appendChild(details);
        container.appendChild(item);
    });
}

function toggleReportForm(prodId) {
    const form = document.getElementById(`reportForm-${prodId}`);
    if (form) {
        form.classList.toggle("hidden");
    }
}

async function submitFishingReport(prodId) {
    const title = document.getElementById(`reportTitle-${prodId}`).value;
    const content = document.getElementById(`reportContent-${prodId}`).value;
    const date = document.getElementById(`reportDate-${prodId}`).value;
    const thumbFile = document.getElementById(`reportThumb-${prodId}`).files[0];

    const token = localStorage.getItem('authToken'); // 토큰 가져오는 방식에 따라 수정

    const formData = new FormData();
    formData.append("dto", new Blob([JSON.stringify({
        title,
        content,
        fishingAt: date,
        prodId: prodId
    })], { type: "application/json" }));
    if (thumbFile) {
        formData.append("thumbnailFile", thumbFile);
    }

    try {
        const response = await fetch("/api/fishing-report", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            },
            body: formData
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error("서버 응답:", errorText);
            throw new Error("등록 실패");
        }
        alert("조황 등록 성공!");
        toggleReportForm(prodId); // 폼 닫기
    } catch (err) {
        console.error(err);
        alert("조황 등록 중 오류가 발생했습니다.");
    }
}

async function deleteProduct(prodId) {  // 매개변수명 통일
    const token = localStorage.getItem('authToken');
    if (!confirm("정말 삭제하시겠습니까?")) return;

    const response = await fetch(`/api/partner/product/delete/${prodId}`, {  // prodId 사용
        method: "DELETE",
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    if (response.ok) {
        alert("삭제되었습니다.");
    } else {
        alert("삭제 실패");
    }

    loadProductList();  // 삭제 후 목록 갱신
}

async function editProduct(prodId) {
    const token = localStorage.getItem('authToken');

    const response = await fetch(`/api/partner/product/${prodId}`, {
        method: "GET",
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    if (!response.ok) {
        alert("상품 정보를 불러오지 못했습니다.");
        return;
    }

    const product = await response.json();

    const container = document.getElementById("productListContainer");
    container.innerHTML = "";

    // 메인/서브 타입 데이터 (원하는대로 조절하세요)
    const subTypes = {
        SEA: [
            { value: "BREAK_WATER", label: "방파제" },
            { value: "ROCKY_SHORE", label: "갯바위" },
            { value: "ESTUARY", label: "하구" },
            { value: "BOAT", label: "선상" },
            { value: "MUD_FLAT", label: "갯벌" },
            { value: "ARTIFICIAL", label: "인공낚시터" },
            { value: "OPEN_SEA", label: "해상" },
            { value: "BEACH", label: "해변" },
            { value: "REEF", label: "암초" },
            { value: "OTHER_SEA", label: "기타_바다" },
        ],
        FRESHWATER: [
            { value: "RIVER", label: "강" },
            { value: "RESERVOIR", label: "저수지" },
            { value: "DAM", label: "댐" },
            { value: "POND", label: "연못" },
            { value: "SMALL_LAKE", label: "소류지" },
            { value: "CANAL", label: "수로" },
            { value: "FLOATING_PLATFORM", label: "좌대" },
            { value: "OPEN_AREA", label: "노지" },
            { value: "OTHER_FRESHWATER", label: "기타_민물" },
        ],
    };

    const form = document.createElement("form");
    form.id = "editProductForm";

    form.innerHTML = `
      <h2>상품 수정</h2>
      <label>이름: <input type="text" id="editProdName" value="${product.prodName}" /></label><br />
      <label>지역: <input type="text" id="editProdRegion" value="${product.prodRegion || ''}" /></label><br />
      
      <label>메인 타입:
        <select id="editMainType" required>
          <option value="">-- 선택하세요 --</option>
          <option value="SEA">바다낚시</option>
          <option value="FRESHWATER">민물낚시</option>
        </select>
      </label><br />

      <label>서브 타입:
        <select id="editSubType" required>
          <option value="">-- 메인 타입 먼저 선택하세요 --</option>
        </select>
      </label><br />

      <label>설명: <textarea id="editProdDesc">${product.prodDescription || ""}</textarea></label><br />
      <label>최대 인원: <input type="number" id="editMaxPerson" value="${product.maxPerson || 0}" /></label><br />
      <label>최소 인원: <input type="number" id="editMinPerson" value="${product.minPerson || 0}" /></label><br />
      <label>무게: <input type="number" step="0.1" id="editWeight" value="${product.weight || 0}" /></label><br />
      <label>주소: <input type="text" id="editProdAddress" value="${product.prodAddress || ""}" /></label><br />
      <label>이벤트: <input type="text" id="editProdEvent" value="${product.prodEvent || ""}" /></label><br />
      <label>공지사항: <input type="text" id="editProdNotice" value="${product.prodNotice || ""}" /></label><br />
      <button type="submit">저장</button>
      <button type="button" onclick="loadProductList()">취소</button>
    `;

    container.appendChild(form);

    const mainTypeSelect = document.getElementById("editMainType");
    const subTypeSelect = document.getElementById("editSubType");

    // 서브타입 옵션 채우기 함수
    function updateSubTypeOptions(mainType) {
        subTypeSelect.innerHTML = "";
        if (!mainType || !subTypes[mainType]) {
            subTypeSelect.innerHTML = '<option value="">-- 메인 타입 먼저 선택하세요 --</option>';
            subTypeSelect.disabled = true;
            return;
        }
        subTypeSelect.disabled = false;
        subTypeSelect.innerHTML = '<option value="">-- 서브 타입 선택 --</option>';
        subTypes[mainType].forEach(({ value, label }) => {
            const option = document.createElement("option");
            option.value = value;
            option.textContent = label;
            subTypeSelect.appendChild(option);
        });
    }

    // 초기 서브타입 옵션 설정 및 메인 타입 기본값 셋팅
    if (product.mainType) {
        mainTypeSelect.value = product.mainType;
        updateSubTypeOptions(product.mainType);
        if (product.subType) {
            subTypeSelect.value = product.subType;
        }
    } else {
        subTypeSelect.disabled = true;
    }

    // 메인 타입 변경시 서브 타입 갱신
    mainTypeSelect.addEventListener("change", (e) => {
        updateSubTypeOptions(e.target.value);
    });

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const updatedData = {
            prodName: document.getElementById("editProdName").value,
            mainType: mainTypeSelect.value,
            subType: subTypeSelect.value,
            prodDescription: document.getElementById("editProdDesc").value,
            maxPerson: parseInt(document.getElementById("editMaxPerson").value) || 0,
            minPerson: parseInt(document.getElementById("editMinPerson").value) || 0,
            weight: parseFloat(document.getElementById("editWeight").value) || 0,
            prodAddress: document.getElementById("editProdAddress").value,
            prodEvent: document.getElementById("editProdEvent").value,
            prodNotice: document.getElementById("editProdNotice").value,
            prodRegion: document.getElementById("editProdRegion") ? document.getElementById("editProdRegion").value : ""
        };

        const result = await fetch(`/api/partner/product/${prodId}`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(updatedData)
        });

        if (result.ok) {
            alert("수정이 완료되었습니다.");
            loadProductList();
        } else {
            alert("수정에 실패했습니다.");
        }
    });
}
