// 전역에서 사용할 수 있도록 window에 등록
window.getAuthToken = function () {
    const token = localStorage.getItem('authToken');
    if (!token) {
        console.warn('인증 토큰이 존재하지 않습니다.');
    }
    return token;
};

window.getAuthHeaders = function () {
    const token = getAuthToken();
    console.log(token)
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
    };
};
