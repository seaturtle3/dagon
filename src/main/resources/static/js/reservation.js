window.addEventListener("DOMContentLoaded", () => {
    updateSpanFromURL()
    console.log("바다 페이지 로딩 완료")
});

// 민물 선택했을 때 freshwater로 이동
function handleApply() {
    // 화면에 선택 값 반영
    const mainType = document.querySelector('input[name="waterType"]:checked').value;
    const subType = document.getElementById("modal-subType").value || "전체";
    const date = document.getElementById("modal-date").value;
    const people = document.getElementById("modal-people").value;
    const region = document.getElementById("modal-region").value;
    const fishType = document.getElementById("modal-fishType").value;
    const today = new Date().toISOString().split("T")[0];

    // 화면에 반영
    document.getElementById("selected-mainType").textContent = mainType === 'sea' ? '바다' : '민물';  // "바다" 또는 "민물" 표시

    const selectedSubTypeText = allSubTypes.find(item => item.value === subType)?.text || "전체";
    document.getElementById("selected-subType").textContent = selectedSubTypeText;

    document.getElementById("selected-date").textContent = date || today;
    document.getElementById("selected-people").textContent = people || 1;
    document.getElementById("selected-region").textContent = region || "전체";
    document.getElementById("selected-fish").textContent = fishType || "전체";

    // URL 구성 및 리디렉션
    const url = `/fishing/${mainType}?mainType=${mainType}&subType=${subType}&date=${date}&people=${people}&region=${region}&fishType=${fishType}`;
    window.location.href = url;
}

function updateSpanFromURL() {
    const params = new URLSearchParams(window.location.search);

    const subType = params.get("subType") || "전체";
    const date = params.get("date") || new Date().toISOString().split("T")[0];
    const people = params.get("people") || 1;
    const region = params.get("region") || "전체";
    const fishType = params.get("fishType") || "전체";
    const mainType = params.get("mainType") || "sea";

    // 화면 반영
    document.getElementById("selected-subType").textContent =
        allSubTypes.find(item => item.value === subType)?.text || "전체";
    document.getElementById("selected-date").textContent = date;
    document.getElementById("selected-people").textContent = people;
    document.getElementById("selected-region").textContent = region;
    document.getElementById("selected-fish").textContent = fishType;

    // 모달 초기값 설정
    document.getElementById("modal-subType").value = subType;
    document.getElementById("modal-date").value = date;
    document.getElementById("modal-people").value = people;
    document.getElementById("modal-region").value = region;
    document.getElementById("modal-fishType").value = fishType;

    document.getElementById(mainType).checked = true;
}


// 세부사항 변경 누를 시 화면에 선택된 옵션과 동일한 값을 가져옴
function syncModalValues() {
    // 현재 화면에서 선택된 값을 가져옵니다.
    let subType = document.getElementById("modal-subType").value.trim();
    const date = document.getElementById("modal-date").value.trim();
    const people = document.getElementById("modal-people").value.trim();
    const region = document.getElementById("modal-region").value.trim();
    const fishType = document.getElementById("modal-fishType").value.trim();
    const mainType = document.querySelector('input[name=waterType]:checked').value;

    // 라디오 버튼에 따른 선택 처리
    if (mainType === "freshwater") {
        document.getElementById("freshwater").checked = true;
    } else {
        document.getElementById("sea").checked = true;
    }

    // 모달에 값 설정
    document.getElementById("modal-subType").value = subType || "전체";  // 기본값 "전체"
    document.getElementById("modal-date").value = date || "";
    document.getElementById("modal-people").value = people || 1;
    document.getElementById("modal-region").value = region || "전체";
    document.getElementById("modal-fishType").value = fishType || "전체";

    // 화면에 반영
    document.getElementById("selected-mainType").textContent = mainType === "freshwater" ? "민물" : "바다";

    // subType을 한글로 업데이트
    const selectedSubTypeText = (subType === "전체" || !subType)
        ? "전체"
        : allSubTypes.find(item => item.value === subType)?.text || "전체";
    document.getElementById("selected-subType").textContent = selectedSubTypeText;

    document.getElementById("selected-date").textContent = date || "날짜 선택";
    document.getElementById("selected-people").textContent = people || 1;
    document.getElementById("selected-region").textContent = region || "전체";
    document.getElementById("selected-fish").textContent = fishType || "전체";

    console.log(`type: ${mainType === 'freshwater' ? '민물' : '바다'}, subType: ${subType}, date: ${date}, people: ${people}, region: ${region}, fishType: ${fishType}`);
}

// allSubTypes : 서버에서 직접 렌더링해서 뿌리자 (static 파일이면 직접 작성)
const allSubTypes = [
    { value: 'BREAK_WATER', text: '방파제', mainType: 'SEA' },
    { value: 'ROCKY_SHORE', text: '갯바위', mainType: 'SEA' },
    { value: 'ESTUARY', text: '하구', mainType: 'SEA' },
    { value: 'BOAT', text: '선상', mainType: 'SEA' },
    { value: 'MUD_FLAT', text: '갯벌', mainType: 'SEA' },
    { value: 'ARTIFICIAL', text: '인공낚시터', mainType: 'SEA' },
    { value: 'OPEN_SEA', text: '해상', mainType: 'SEA' },
    { value: 'BEACH', text: '해변', mainType: 'SEA' },
    { value: 'REEF', text: '암초', mainType: 'SEA' },
    { value: 'OTHER_SEA', text: '기타_바다', mainType: 'SEA' },

    { value: 'RIVER', text: '강', mainType: 'FRESHWATER' },
    { value: 'RESERVOIR', text: '저수지', mainType: 'FRESHWATER' },
    { value: 'DAM', text: '댐', mainType: 'FRESHWATER' },
    { value: 'POND', text: '연못', mainType: 'FRESHWATER' },
    { value: 'SMALL_LAKE', text: '소류지', mainType: 'FRESHWATER' },
    { value: 'CANAL', text: '수로', mainType: 'FRESHWATER' },
    { value: 'FLOATING_PLATFORM', text: '좌대', mainType: 'FRESHWATER' },
    { value: 'OPEN_AREA', text: '노지', mainType: 'FRESHWATER' },
    { value: 'OTHER_FRESHWATER', text: '기타_민물', mainType: 'FRESHWATER' }
];

// waterType 라디오 버튼 클릭 이벤트
document.addEventListener('DOMContentLoaded', function() {
    // 바다 선택 시
    document.getElementById('sea').addEventListener('click', function() {
        updateSubTypeOptions('sea');
        syncModalValues(); // 모달 값 동기화
    });

    // 민물 선택 시
    document.getElementById('freshwater').addEventListener('click', function() {
        updateSubTypeOptions('freshwater');
        syncModalValues(); // 모달 값 동기화
    });

    // 모달 열 때 기본값 설정 (바다) => 실제로 페이지가 로드되면서 상태를 반영
    const mainTypeFromUrl = new URLSearchParams(window.location.search).get('mainType');
    const initialMainType = mainTypeFromUrl || 'sea'; // 기본값 바다로 설정
    updateSubTypeOptions(initialMainType);
    syncModalValues(); // 모달 값 동기화
});

// 서브타입 옵션 업데이트
function updateSubTypeOptions(selectedWaterType) {
    const select = document.getElementById('modal-subType');
    select.innerHTML = '<option value="전체">전체</option>'; // 초기화

    const targetMainType = selectedWaterType.toUpperCase() === 'SEA' ? 'SEA' : 'FRESHWATER';

    allSubTypes.forEach(subType => {
        if (subType.mainType === targetMainType) {
            const option = document.createElement('option');
            option.value = subType.value;
            option.textContent = subType.text;
            select.appendChild(option);
        }
    });

    // 🔥 기존 URL 파라미터에서 subType 가져오기
    const params = new URLSearchParams(window.location.search);
    const currentSubType = params.get('subType') || "";

    // 🔥 옵션 중에 currentSubType이 있으면 선택
    const optionExists = Array.from(select.options).some(opt => opt.value === currentSubType);
    if (optionExists) {
        select.value = currentSubType;
        const selectedSubTypeText = allSubTypes.find(item => item.value === currentSubType)?.text || "전체";
        // document.getElementById("selected-subType").textContent = selectedSubTypeText;
    } else {
        // 없으면 전체
        select.value = "";
        document.getElementById("selected-subType").textContent = "전체";
    }
}
