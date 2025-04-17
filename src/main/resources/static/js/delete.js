document.getElementById('deleteAccountBtn').addEventListener('click', function() {
    const token = getAuthToken();

    if (!token) {
        alert("인증되지 않은 요청입니다. 로그인 후 다시 시도해주세요.");
        return;
    }


    if (confirm('정말로 회원을 탈퇴하시겠습니까? 탈퇴 후 모든 데이터는 복구할 수 없습니다.')) {
        fetch('/api/mypage/delete', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`// JWT 토큰 사용
            },
        })
            .then(response => response.json())
            .then(data => {
                if (data.message) {
                    alert(data.message);
                    window.location.href = '/login';  // 탈퇴 후 로그인 페이지로 이동
                    // 회원 탈퇴 화면 전환
                    document.getElementById('deleteAccountBtn').addEventListener('click', () => {
                        document.getElementById('profile').classList.add('hidden');
                        document.getElementById('deleteAccountSection').classList.remove('hidden');
                        document.getElementById('pointSection').classList.add('hidden');
                        document.getElementById('passwordChangeSection').classList.add('hidden');
                    });
                } else {
                    alert('회원 탈퇴에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버 오류가 발생했습니다.');
            });
    }
});

