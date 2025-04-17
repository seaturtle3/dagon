
    fetch('/api/mypage')  // 예시 API 엔드포인트
    .then(response => response.json())
    .then(data => {
    const userLevel = data.level;  // 예: 0, 1, 2, 3
    renderUserLevelBadge(userLevel);  // 사용자 레벨에 맞는 배지를 렌더링
})
    .catch(error => console.error('Error:', error));

    function renderUserLevelBadge(level) {
    const badgeContainer = document.getElementById('userLevelBadge');

    const badgeMap = {
    0: `<div class="flex items-center gap-2 bg-white/70 px-4 py-1 rounded-full shadow-sm">
                        <span class="grade-silver">🩶 실버</span>
                    </div>`,
    1: `<div class="flex items-center gap-2 bg-white/70 px-4 py-1 rounded-full shadow-sm">
                        <span class="grade-gold">🥇 골드</span>
                    </div>`,
    2: `<div class="flex items-center gap-2 bg-white/70 px-4 py-1 rounded-full shadow-sm">
                        <span class="grade-platinum flex items-center gap-1">
                            <svg class="w-6 h-6 text-emerald-500" xmlns="http://www.w3.org/2000/svg" fill="none"
                                viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round"
                                    d="M12 2l3 4.5h6l-4 5 4 5h-6l-3 4.5-3-4.5H3l4-5-4-5h6l3-4.5z"/>
                            </svg>
                            플래티넘
                        </span>
                    </div>`,
    3: `<div class="flex items-center gap-2 bg-white/70 px-4 py-1 rounded-full shadow-sm">
                        <span class="grade-diamond text-sky-500">💎 다이아</span>
                    </div>`
};

    badgeContainer.innerHTML = badgeMap[level] || "";  // level에 맞는 배지를 렌더링
}
