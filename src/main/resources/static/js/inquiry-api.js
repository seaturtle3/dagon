/**
 * 1:1 문의 API 클라이언트
 * 프론트엔드에서 백엔드 API와 통신하기 위한 JavaScript 모듈
 */

class InquiryApiClient {
    constructor() {
        this.baseUrl = '/api/inquiry';
        this.token = localStorage.getItem('authToken');
    }

    /**
     * HTTP 요청 헤더 설정
     */
    getHeaders() {
        const headers = {
            'Content-Type': 'application/json'
        };
        
        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }
        
        return headers;
    }

    /**
     * HTTP 요청 실행
     */
    async request(url, options = {}) {
        try {
            const response = await fetch(url, {
                ...options,
                headers: this.getHeaders()
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || '요청 처리 중 오류가 발생했습니다.');
            }

            return data;
        } catch (error) {
            console.error('API 요청 오류:', error);
            throw error;
        }
    }

    /**
     * 1:1 문의 생성
     * @param {Object} inquiryData - 문의 데이터
     * @returns {Promise<Object>} 생성된 문의 정보
     */
    async createInquiry(inquiryData) {
        const url = this.baseUrl;
        
        const requestData = {
            receiverType: inquiryData.receiverType || 'ADMIN',
            partnerId: inquiryData.partnerId || null,
            title: inquiryData.title,
            content: inquiryData.content,
            inquiryType: inquiryData.inquiryType,
            partnerName: inquiryData.partnerName || null,
            writerType: inquiryData.writerType || 'USER'
        };

        return await this.request(url, {
            method: 'POST',
            body: JSON.stringify(requestData)
        });
    }

    /**
     * 파트너 문의 생성 (상품ID 기반)
     * @param {Object} inquiryData - 문의 데이터
     * @returns {Promise<Object>} 생성된 문의 정보
     */
    async createPartnerInquiry(inquiryData) {
        const url = `${this.baseUrl}/partner`;
        
        const requestData = {
            productId: inquiryData.productId,
            title: inquiryData.title,
            content: inquiryData.content,
            inquiryType: inquiryData.inquiryType
        };

        return await this.request(url, {
            method: 'POST',
            body: JSON.stringify(requestData)
        });
    }

    /**
     * 문의 목록 조회 (관리자용)
     * @param {Object} params - 조회 파라미터
     * @returns {Promise<Object>} 문의 목록
     */
    async getAdminInquiries(params = {}) {
        const url = `${this.baseUrl}/admin`;
        const queryParams = new URLSearchParams();
        
        if (params.page !== undefined) queryParams.append('page', params.page);
        if (params.size !== undefined) queryParams.append('size', params.size);
        if (params.keyword) queryParams.append('keyword', params.keyword);
        if (params.status !== undefined) queryParams.append('status', params.status);

        const fullUrl = queryParams.toString() ? `${url}?${queryParams.toString()}` : url;
        return await this.request(fullUrl, { method: 'GET' });
    }

    /**
     * 문의 단건 조회
     * @param {number} inquiryId - 문의 ID
     * @returns {Promise<Object>} 문의 정보
     */
    async getInquiry(inquiryId) {
        const url = `${this.baseUrl}/${inquiryId}`;
        return await this.request(url, { method: 'GET' });
    }

    /**
     * 문의 수정
     * @param {number} inquiryId - 문의 ID
     * @param {Object} updateData - 수정할 데이터
     * @returns {Promise<Object>} 수정된 문의 정보
     */
    async updateInquiry(inquiryId, updateData) {
        const url = `${this.baseUrl}/${inquiryId}`;
        
        const requestData = {
            title: updateData.title,
            content: updateData.content,
            inquiryType: updateData.inquiryType,
            writerType: updateData.writerType || 'USER'
        };

        return await this.request(url, {
            method: 'PUT',
            body: JSON.stringify(requestData)
        });
    }

    /**
     * 문의 삭제
     * @param {number} inquiryId - 문의 ID
     * @returns {Promise<Object>} 삭제 결과
     */
    async deleteInquiry(inquiryId) {
        const url = `${this.baseUrl}/${inquiryId}`;
        return await this.request(url, { method: 'DELETE' });
    }

    /**
     * 문의 답변 작성
     * @param {number} inquiryId - 문의 ID
     * @param {string} answerContent - 답변 내용
     * @returns {Promise<Object>} 답변 결과
     */
    async answerInquiry(inquiryId, answerContent) {
        const url = `${this.baseUrl}/${inquiryId}/answer`;
        
        const requestData = {
            answerContent: answerContent
        };

        return await this.request(url, {
            method: 'POST',
            body: JSON.stringify(requestData)
        });
    }

    /**
     * 내 문의 목록 조회
     * @returns {Promise<Object>} 내 문의 목록
     */
    async getMyInquiries() {
        const url = `${this.baseUrl}/my-inquiries`;
        return await this.request(url, { method: 'GET' });
    }

    /**
     * 사용자-파트너 간 문의 조회
     * @param {number} partnerUno - 파트너 ID
     * @returns {Promise<Object>} 문의 목록
     */
    async getUserToPartnerInquiries(partnerUno) {
        const url = `${this.baseUrl}/user-to-partner?partnerUno=${partnerUno}`;
        return await this.request(url, { method: 'GET' });
    }

    /**
     * 파트너에게 온 문의 조회
     * @param {number} partnerUno - 파트너 ID
     * @returns {Promise<Object>} 문의 목록
     */
    async getInquiriesToPartner(partnerUno) {
        const url = `${this.baseUrl}/partner-inquiries?partnerUno=${partnerUno}`;
        return await this.request(url, { method: 'GET' });
    }
}

/**
 * 문의 UI 관리 클래스
 */
class InquiryUI {
    constructor() {
        this.apiClient = new InquiryApiClient();
        this.initEventListeners();
    }

    /**
     * 이벤트 리스너 초기화
     */
    initEventListeners() {
        // 문의 등록 폼 제출
        const inquiryForm = document.getElementById('inquiryForm');
        if (inquiryForm) {
            inquiryForm.addEventListener('submit', (e) => this.handleInquirySubmit(e));
        }

        // 수신자 타입 변경 시 파트너명 필드 표시/숨김
        const receiverTypeSelect = document.getElementById('receiverType');
        if (receiverTypeSelect) {
            receiverTypeSelect.addEventListener('change', () => this.togglePartnerNameField());
        }

        // 문의 목록 로드
        this.loadInquiryList();
    }

    /**
     * 문의 등록 폼 제출 처리
     */
    async handleInquirySubmit(event) {
        event.preventDefault();
        
        try {
            const formData = new FormData(event.target);
            const inquiryData = {
                receiverType: formData.get('receiverType'),
                partnerName: formData.get('partnerName'),
                title: formData.get('title'),
                content: formData.get('content'),
                inquiryType: document.getElementById('inquiryType').value,
                writerType: 'USER'
            };

            // 파트너 문의인 경우 파트너 ID 설정
            if (inquiryData.receiverType === 'PARTNER' && inquiryData.partnerName) {
                // TODO: 파트너명으로 파트너 ID 조회 로직 구현
                // inquiryData.partnerId = await this.getPartnerIdByName(inquiryData.partnerName);
            }

            const result = await this.apiClient.createInquiry(inquiryData);
            
            if (result.success) {
                alert('문의가 성공적으로 등록되었습니다.');
                event.target.reset();
                this.togglePartnerNameField();
                this.loadInquiryList(); // 목록 새로고침
            } else {
                alert('문의 등록에 실패했습니다: ' + result.message);
            }
        } catch (error) {
            console.error('문의 등록 오류:', error);
            alert('문의 등록 중 오류가 발생했습니다: ' + error.message);
        }
    }

    /**
     * 수신자 타입에 따라 파트너명 필드 표시/숨김
     */
    togglePartnerNameField() {
        const receiverTypeSelect = document.getElementById('receiverType');
        const partnerNameGroup = document.getElementById('partnerNameGroup');
        const partnerNameInput = document.getElementById('partnerName');
        
        if (receiverTypeSelect && partnerNameGroup && partnerNameInput) {
            if (receiverTypeSelect.value === 'PARTNER') {
                partnerNameGroup.classList.remove('hidden');
                partnerNameInput.required = true;
            } else {
                partnerNameGroup.classList.add('hidden');
                partnerNameInput.required = false;
            }
        }
    }

    /**
     * 문의 목록 로드
     */
    async loadInquiryList() {
        try {
            const result = await this.apiClient.getMyInquiries();
            
            if (result.success) {
                this.displayInquiryList(result.data);
            } else {
                console.error('문의 목록 로드 실패:', result.message);
            }
        } catch (error) {
            console.error('문의 목록 로드 오류:', error);
        }
    }

    /**
     * 문의 목록 화면에 표시
     */
    displayInquiryList(inquiries) {
        const container = document.getElementById('inquiryList');
        if (!container) return;

        if (inquiries.length === 0) {
            container.innerHTML = '<p>등록된 문의가 없습니다.</p>';
            return;
        }

        const html = inquiries.map(inquiry => `
            <div class="inquiry-item" style="border: 1px solid #ddd; padding: 15px; margin-bottom: 10px; border-radius: 5px;">
                <div class="inquiry-header">
                    <h4>${inquiry.title}</h4>
                    <span class="inquiry-type">${inquiry.inquiryType}</span>
                    <span class="inquiry-status ${inquiry.isAnswered ? 'answered' : 'pending'}">
                        ${inquiry.isAnswered ? '답변완료' : '답변대기'}
                    </span>
                </div>
                <div class="inquiry-content">
                    <p><strong>내용:</strong> ${inquiry.content}</p>
                    <p><strong>작성일:</strong> ${new Date(inquiry.createdAt).toLocaleString()}</p>
                    ${inquiry.partnerName ? `<p><strong>파트너:</strong> ${inquiry.partnerName}</p>` : ''}
                </div>
                ${inquiry.isAnswered ? `
                    <div class="inquiry-answer">
                        <p><strong>답변:</strong> ${inquiry.answerContent}</p>
                        <p><strong>답변일:</strong> ${new Date(inquiry.answeredAt).toLocaleString()}</p>
                    </div>
                ` : ''}
                <div class="inquiry-actions">
                    <button onclick="inquiryUI.editInquiry(${inquiry.id})" class="btn-edit">수정</button>
                    <button onclick="inquiryUI.deleteInquiry(${inquiry.id})" class="btn-delete">삭제</button>
                </div>
            </div>
        `).join('');

        container.innerHTML = html;
    }

    /**
     * 문의 수정
     */
    async editInquiry(inquiryId) {
        try {
            const result = await this.apiClient.getInquiry(inquiryId);
            
            if (result.success) {
                this.showEditForm(result.data);
            } else {
                alert('문의 조회에 실패했습니다: ' + result.message);
            }
        } catch (error) {
            console.error('문의 수정 오류:', error);
            alert('문의 수정 중 오류가 발생했습니다: ' + error.message);
        }
    }

    /**
     * 문의 삭제
     */
    async deleteInquiry(inquiryId) {
        if (!confirm('정말로 이 문의를 삭제하시겠습니까?')) {
            return;
        }

        try {
            const result = await this.apiClient.deleteInquiry(inquiryId);
            
            if (result.success) {
                alert('문의가 삭제되었습니다.');
                this.loadInquiryList(); // 목록 새로고침
            } else {
                alert('문의 삭제에 실패했습니다: ' + result.message);
            }
        } catch (error) {
            console.error('문의 삭제 오류:', error);
            alert('문의 삭제 중 오류가 발생했습니다: ' + error.message);
        }
    }

    /**
     * 수정 폼 표시
     */
    showEditForm(inquiry) {
        // TODO: 모달 또는 별도 폼으로 수정 UI 구현
        console.log('수정할 문의:', inquiry);
    }
}

// 전역 인스턴스 생성
const inquiryUI = new InquiryUI();

// 전역 함수로 노출 (HTML에서 직접 호출 가능)
window.inquiryUI = inquiryUI;
window.InquiryApiClient = InquiryApiClient; 