// 관리자 파트너 페이지 접근 (토큰 위임) 기능
class AdminPartnerImpersonation {
    constructor() {
        this.init();
    }

    init() {
        this.bindEvents();
    }

    bindEvents() {
        // 파트너 목록에서 "파트너 페이지 접근" 버튼 클릭 이벤트
        $(document).on('click', '.btn-partner-access', (e) => {
            e.preventDefault();
            const partnerUno = $(e.target).data('partner-uno');
            const partnerName = $(e.target).data('partner-name');
            this.accessPartnerPage(partnerUno, partnerName);
        });
    }

    // 파트너 페이지 접근 (토큰 위임)
    async accessPartnerPage(partnerUno, partnerName) {
        try {
            // 확인 다이얼로그
            const confirmed = confirm(
                `파트너 "${partnerName}"의 페이지에 접근하시겠습니까?\n\n` +
                `• 파트너 권한으로 임시 로그인됩니다\n` +
                `• 파트너 페이지에서 모든 기능을 사용할 수 있습니다\n` +
                `• 브라우저를 새로고침하면 관리자 권한으로 돌아갑니다`
            );

            if (!confirmed) return;

            // 로딩 표시
            this.showLoading('파트너 페이지 접근 중...');

            const response = await fetch(`/api/admin/impersonate/partner/${partnerUno}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${this.getAdminToken()}`
                }
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            const data = await response.json();

            // 위임 토큰을 로컬 스토리지에 저장
            localStorage.setItem('impersonatedToken', data.impersonatedToken);
            localStorage.setItem('originalAdminToken', this.getAdminToken());
            localStorage.setItem('impersonationInfo', JSON.stringify({
                partnerUno: data.partnerUno,
                partnerName: data.partnerName,
                originalAdminUno: data.originalAdminUno,
                timestamp: new Date().toISOString()
            }));

            // 성공 메시지
            this.hideLoading();
            this.showSuccessMessage(
                `파트너 "${data.partnerName}" 페이지에 접근합니다.\n` +
                `파트너 권한으로 임시 로그인되었습니다.`
            );

            // 파트너 페이지로 리다이렉트 (예: 파트너 대시보드)
            setTimeout(() => {
                window.location.href = '/partner/dashboard';
            }, 1500);

        } catch (error) {
            this.hideLoading();
            this.showErrorMessage('파트너 페이지 접근 실패: ' + error.message);
            console.error('파트너 페이지 접근 오류:', error);
        }
    }

    // 관리자 토큰 가져오기
    getAdminToken() {
        return localStorage.getItem('adminToken') || 
               localStorage.getItem('token') || 
               sessionStorage.getItem('adminToken') || 
               sessionStorage.getItem('token');
    }

    // 로딩 표시
    showLoading(message) {
        // 로딩 스피너 표시
        if (!$('#loadingSpinner').length) {
            $('body').append(`
                <div id="loadingSpinner" style="
                    position: fixed; top: 0; left: 0; width: 100%; height: 100%; 
                    background: rgba(0,0,0,0.5); z-index: 9999; display: flex; 
                    align-items: center; justify-content: center;">
                    <div style="background: white; padding: 20px; border-radius: 8px; text-align: center;">
                        <div class="spinner-border text-primary" role="status"></div>
                        <div class="mt-2">${message}</div>
                    </div>
                </div>
            `);
        } else {
            $('#loadingSpinner').show();
        }
    }

    // 로딩 숨기기
    hideLoading() {
        $('#loadingSpinner').hide();
    }

    // 성공 메시지 표시
    showSuccessMessage(message) {
        if (typeof toastr !== 'undefined') {
            toastr.success(message);
        } else {
            alert(message);
        }
    }

    // 에러 메시지 표시
    showErrorMessage(message) {
        if (typeof toastr !== 'undefined') {
            toastr.error(message);
        } else {
            alert(message);
        }
    }
}

// 페이지 로드 시 초기화
$(document).ready(() => {
    new AdminPartnerImpersonation();
}); 