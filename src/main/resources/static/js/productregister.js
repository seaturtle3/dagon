async function submitProduct() {
    const token = localStorage.getItem("authToken");
    if (!token) {
        alert("로그인이 필요합니다.");
        return;
    }

    const productData = {
        prodName: document.getElementById("prodName").value,
        prodRegion: document.getElementById("prodRegion").value,
        mainType: document.getElementById("mainType").value,
        subType: document.getElementById("subType").value,
        prodDescription: document.getElementById("prodDescription").value,
        maxPerson: Number(document.getElementById("maxPerson").value),
        minPerson: Number(document.getElementById("minPerson").value),
        weight: parseFloat(document.getElementById("weight").value),
        prodAddress: document.getElementById("prodAddress").value,
        prodEvent: document.getElementById("prodEvent").value,
        prodNotice: document.getElementById("prodNotice").value
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
            loadProductList();
            showTab("productManagement");
        } else {
            const errorData = await response.json();
            alert("등록 실패: " + errorData.message);
        }
    } catch (err) {
        console.error("에러 발생:", err);
        alert("등록 중 오류가 발생했습니다.");
    }
}

function showTab(tabName) {
    document.querySelectorAll(".tab-content").forEach(el => el.style.display = "none");

    if (tabName === "productRegister") {
        loadProductRegisterForm();
    } else if (tabName === "productManagement") {
        loadProductList();
    }

    document.getElementById(tabName).style.display = "block";
}

function loadProductRegisterForm() {
    const container = document.getElementById("productRegisterContainer");
    container.innerHTML = `
        <h2>상품 등록</h2>
        <form id="productForm">
            <label for="prodName">상품명:</label><br />
            <input type="text" id="prodName" name="prodName"><br /><br />

            <label for="prodRegion">지역:</label><br />
            <select id="prodRegion" name="prodRegion">
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
            <select id="mainType" name="mainType">
                <option value="">선택</option>
                <option value="SEA">바다낚시</option>
                <option value="FRESHWATER">민물낚시</option>
            </select><br /><br />

            <label for="subType">서브 타입:</label><br />
            <select id="subType" name="subType">
                <!-- JS로 채워짐 -->
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

            <button type="button" id="submitProductBtn">등록</button>
        </form>
    `;

    // 서브타입 옵션 함수 (내부에만 존재)
    function populateSubTypes() {
        const mainType = document.getElementById("mainType").value;
        const subTypeSelect = document.getElementById("subType");

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

        subTypeSelect.innerHTML = "";
        if (mainType && subTypeOptions[mainType]) {
            subTypeOptions[mainType].forEach(opt => {
                const option = document.createElement("option");
                option.value = opt.value;
                option.textContent = opt.label;
                subTypeSelect.appendChild(option);
            });
        }
    }

    // 이벤트 등록
    document.getElementById("mainType").addEventListener("change", populateSubTypes);

    // 서브 타입 초기화
    populateSubTypes();

    // 등록 버튼 이벤트
    document.getElementById("submitProductBtn").addEventListener("click", submitProduct);
}

