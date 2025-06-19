// 관리자 회원 수정 API 사용 예시

// 1. 이미지 업로드 포함 회원 수정 (FormData 사용)
async function updateUserWithImage(uno, userData, profileImage) {
    const formData = new FormData();
    
    // 기본 정보 추가
    formData.append('uname', userData.uname);
    formData.append('nickname', userData.nickname);
    formData.append('email', userData.email);
    formData.append('phone1', userData.phone1);
    formData.append('phone2', userData.phone2);
    formData.append('phone3', userData.phone3);
    formData.append('points', userData.points.toString());
    formData.append('level', userData.level.toString());
    formData.append('levelPoint', userData.levelPoint);
    formData.append('loginType', userData.loginType);
    formData.append('role', userData.role);
    formData.append('isActive', userData.isActive.toString());
    
    // 이미지가 있으면 추가
    if (profileImage) {
        formData.append('profileImage', profileImage);
    }
    
    try {
        const response = await fetch(`/api/admin/user/${uno}`, {
            method: 'PUT',
            body: formData
        });
        
        if (response.ok) {
            const updatedUser = await response.json();
            console.log('회원 수정 성공:', updatedUser);
            return updatedUser;
        } else {
            const errorMessage = await response.text();
            console.error('회원 수정 실패:', errorMessage);
            throw new Error(errorMessage);
        }
    } catch (error) {
        console.error('회원 수정 중 오류:', error);
        throw error;
    }
}

// 2. JSON 기반 회원 수정 (이미지 없이)
async function updateUserJson(uno, userData) {
    try {
        const response = await fetch(`/api/admin/user/${uno}/json`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        
        if (response.ok) {
            const updatedUser = await response.json();
            console.log('회원 수정 성공:', updatedUser);
            return updatedUser;
        } else {
            const errorMessage = await response.text();
            console.error('회원 수정 실패:', errorMessage);
            throw new Error(errorMessage);
        }
    } catch (error) {
        console.error('회원 수정 중 오류:', error);
        throw error;
    }
}

// 사용 예시
document.addEventListener('DOMContentLoaded', function() {
    // 이미지 업로드 포함 수정 예시
    const updateForm = document.getElementById('updateUserForm');
    if (updateForm) {
        updateForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const formData = new FormData(updateForm);
            const uno = formData.get('uno');
            const profileImage = formData.get('profileImage');
            
            const userData = {
                uname: formData.get('uname'),
                nickname: formData.get('nickname'),
                email: formData.get('email'),
                phone1: formData.get('phone1'),
                phone2: formData.get('phone2'),
                phone3: formData.get('phone3'),
                points: parseInt(formData.get('points')),
                level: parseInt(formData.get('level')),
                levelPoint: formData.get('levelPoint'),
                loginType: formData.get('loginType'),
                role: formData.get('role'),
                isActive: formData.get('isActive') === 'true'
            };
            
            try {
                await updateUserWithImage(uno, userData, profileImage);
                alert('회원 정보가 성공적으로 수정되었습니다.');
                // 페이지 새로고침 또는 목록으로 이동
                window.location.reload();
            } catch (error) {
                alert('회원 수정에 실패했습니다: ' + error.message);
            }
        });
    }
});

// 전화번호 자동 포맷팅
function formatPhoneNumber(input) {
    let value = input.value.replace(/\D/g, '');
    
    if (value.length <= 3) {
        input.value = value;
    } else if (value.length <= 7) {
        input.value = value.slice(0, 3) + '-' + value.slice(3);
    } else {
        input.value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11);
    }
} 