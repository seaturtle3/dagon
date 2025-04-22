let selectedWaterType = 'sea'; // 기본값을 바다로 설정

// 모달에서 옵션 선택 후 selected 화면에 출력되는 설정
function updateSpan() {
    const mainType = document.querySelector('input[name="waterType"]:checked').value;
    const subType = document.getElementById("modal-subType").value;
    const date = document.getElementById("modal-date").value;
    const people = document.getElementById("modal-people").value;
    const region = document.getElementById("modal-region").value;
    const fishType = document.getElementById("modal-fishType").value;
    const today = new Date().toISOString().split("T")[0];

    document.getElementById("selected-mainType").textContent = mainType === 'sea' ? '바다' : '민물';  // "바다" 또는 "민물" 표시

    const selectedSubTypeText = allSubTypes.find(item => item.value === subType)?.text || "전체";
    document.getElementById("selected-subType").textContent = selectedSubTypeText;

    document.getElementById("selected-date").textContent = date || today;
    document.getElementById("selected-people").textContent = people || 1;
    document.getElementById("selected-region").textContent = region || "전체";
    document.getElementById("selected-fish").textContent = fishType || "전체";

    console.log(`MainType: ${mainType}, SubType: ${subType}, date : ${date}, people : ${people}, region : ${region}, fishType : ${fishType}`);
}

// 검색하기 버튼 클릭 시 선택한 옵션값과 함께 바다/민물 페이지로 이동
document.getElementById("search-btn").addEventListener('click', function () {
    const today = new Date().toISOString().split("T")[0];
    const date = document.getElementById("modal-date").value || today;
    const people = document.getElementById("modal-people").value || 1;
    const region = document.getElementById("modal-region").value || "전체";
    const fishType = document.getElementById("modal-fishType").value || "전체";
    const mainType = document.querySelector('input[name="waterType"]:checked').value;
    const subType = document.getElementById("modal-subType").value || "전체";

    const url = mainType === "freshwater"
        ? `/fishing/freshwater?mainType=${mainType}&subType=${subType}&date=${date}&people=${people}&region=${region}&fishType=${fishType}`
        : `/fishing/sea?mainType=${mainType}&subType=${subType}&date=${date}&people=${people}&region=${region}&fishType=${fishType}`;

    window.location.href = url;
});

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

// 서브타입 옵션 업데이트
function updateSubTypeOptions(selectedWaterType) {
    const select = document.getElementById('modal-subType');
    select.innerHTML = '<option value="">전체</option>'; // 초기화

    const targetMainType = selectedWaterType.toUpperCase() === 'SEA' ? 'SEA' : 'FRESHWATER';

    allSubTypes.forEach(subType => {
        if (subType.mainType === targetMainType) {
            const option = document.createElement('option');
            option.value = subType.value;
            option.textContent = subType.text;
            select.appendChild(option);
        }
    });

    // 서브타입 선택 값 리셋
    const selectedSubType = document.getElementById('modal-subType').value;
    if (selectedSubType) {
        document.getElementById("selected-subType").textContent = selectedSubType;
    }
}

// waterType 라디오 버튼 클릭 이벤트
document.addEventListener('DOMContentLoaded', function() {
    // 라디오 버튼 클릭 시 waterType 값 업데이트
    document.getElementById('sea').addEventListener('click', function() {
        selectedWaterType = 'sea';
        updateSubTypeOptions('sea');
    });
    document.getElementById('freshwater').addEventListener('click', function() {
        selectedWaterType = 'freshwater';
        updateSubTypeOptions('freshwater');
    });

    // 모달 열 때 기본값 설정 (바다)
    updateSubTypeOptions(selectedWaterType);
});

// 페이지 새로고침 로딩
window.addEventListener("DOMContentLoaded", () => {
    updateSpan();
    console.log("index 페이지 로딩 완료")
});