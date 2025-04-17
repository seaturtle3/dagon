document.getElementById('pointCheckBtn').addEventListener('click', async () => {
    const token = getAuthToken();

    if (!token) {
        alert("인증되지 않은 요청입니다. 로그인 후 다시 시도해주세요.");
        return;
    }

    try {
        const response = await fetch("/api/mypage/point", {
            method: "GET",
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            // 값 채우기
            document.getElementById('pointAmount').textContent = data.point.toLocaleString();

            // 화면 전환
            document.getElementById('profile').classList.add('hidden');
            document.getElementById('passwordChangeSection').classList.add('hidden');
            document.getElementById('pointSection').classList.remove('hidden');
            document.getElementById('deleteAccountSection').classList.add('hidden');
        } else {
            alert("포인트 조회에 실패했습니다.");
        }
    } catch (error) {
        console.error("포인트 조회 오류:", error);
        alert("오류가 발생했습니다.");
    }
});

// 포인트 닫기 버튼
function hidePointSection() {
    document.getElementById('pointSection').classList.add('hidden');
    document.getElementById('profile').classList.remove('hidden');
}