<template>
  <div class="reservations">
    <h1>예약 목록</h1>
    <div class="reservation-tabs">
      <button :class="{active: activeTab==='member'}" @click="activeTab='member'">회원 예약 목록</button>
      <button :class="{active: activeTab==='guest'}" @click="activeTab='guest'">비회원 예약 목록</button>
    </div>
    <div class="search-bar">
      <input type="text" v-model="searchQuery" placeholder="예약번호, 회원명, 파트너명으로 검색">
      <input type="date" v-model="dateFilter">
      <select v-model="statusFilter">
        <option value="">전체 상태</option>
        <option value="예약대기">예약대기</option>
        <option value="예약확정">예약확정</option>
        <option value="예약취소">예약취소</option>
        <option value="이용완료">이용완료</option>
      </select>
      <button @click="searchReservations">검색</button>
      
    </div>
    
    <div v-if="loading" class="loading">
      예약 목록을 불러오는 중...
    </div>
    
    <table v-else-if="activeTab==='member'" class="reservations-table">
      <thead>
        <tr>
          <th>예약번호</th>
          <th>회원명</th>
          <th>파트너명</th>
          <th>예약일</th>
          <th>인원</th>
          <th>금액</th>
          <th>상태</th>
          <th>관리</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="reservation in memberReservations" :key="reservation.reservationId">
          <td>{{ reservation.reservationId }}</td>
          <td>{{ reservation.userName }}</td>
          <td>{{ reservation.partnerName || '정보 없음' }}</td>
          <td>{{ formatDate(reservation.fishingAt) }}</td>
          <td>{{ reservation.numPerson }}명</td>
          <td>{{ getReservationAmount(reservation) }}원</td>
          <td>
            <span :class="['status-badge', getStatusClass(reservation.reservationStatus)]">
              {{ getStatusText(reservation.reservationStatus) }}
            </span>
          </td>
          <td>
            <button @click="viewReservationDetails(reservation.reservationId)">상세</button>
            <button v-if="reservation.reservationStatus === 'PENDING'" @click="approveReservation(reservation.reservationId)">승인</button>
            <button v-if="reservation.reservationStatus === 'PENDING'" @click="rejectReservation(reservation.reservationId)">거절</button>
          </td>
        </tr>
      </tbody>
    </table>

    <table v-else-if="activeTab==='guest'" class="reservations-table">
      <thead>
        <tr>
          <th>예약번호</th>
          <th>예약자명</th>
          <th>이메일</th>
          <th>연락처</th>
          <th>파트너명</th>
          <th>예약일</th>
          <th>인원</th>
          <th>금액</th>
          <th>상태</th>
          <th>관리</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="reservation in guestReservations" :key="reservation.reservationId">
          <td>{{ reservation.reservationId }}</td>
          <td>{{ reservation.userName }}</td>
          <td>{{ reservation.email || '정보 없음' }}</td>
          <td>{{ reservation.phone || '정보 없음' }}</td>
          <td>{{ reservation.partnerName || '정보 없음' }}</td>
          <td>{{ formatDate(reservation.fishingAt) }}</td>
          <td>{{ reservation.numPerson }}명</td>
          <td>{{ getReservationAmount(reservation) }}원</td>
          <td>
            <span :class="['status-badge', getStatusClass(reservation.reservationStatus)]">
              {{ getStatusText(reservation.reservationStatus) }}
            </span>
          </td>
          <td>
            <button @click="viewReservationDetails(reservation.reservationId)">상세</button>
            <button v-if="reservation.reservationStatus === 'PENDING'" @click="approveReservation(reservation.reservationId)">승인</button>
            <button v-if="reservation.reservationStatus === 'PENDING'" @click="rejectReservation(reservation.reservationId)">거절</button>
          </td>
        </tr>
      </tbody>
    </table>
    
    <div v-if="!loading && allReservations.length === 0" class="no-data">
      예약 내역이 없습니다.
    </div>
    
    <div class="pagination" v-if="allReservations.length > 0">
      <button :disabled="currentPage === 1" @click="changePage(currentPage - 1)">
        <i class="fas fa-chevron-left"></i> 이전
      </button>
      <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
      <span class="total-info">총 {{ totalElements }}개 예약</span>
      <button :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">
        다음 <i class="fas fa-chevron-right"></i>
      </button>
    </div>

    <!-- 예약 상세 모달 -->
    <div v-if="showDetailModal" class="modal-overlay" @click="closeDetailModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>예약 상세 정보</h2>
          <button class="close-btn" @click="closeDetailModal">&times;</button>
        </div>
        <div class="modal-body" v-if="selectedReservation">
          <div class="detail-section">
            <h3>기본 정보</h3>
            <div class="detail-grid">
              <div class="detail-item">
                <label>예약번호:</label>
                <span>{{ selectedReservation.reservationId }}</span>
              </div>
              <div class="detail-item">
                <label>예약자:</label>
                <span>{{ selectedReservation.userName }}</span>
              </div>
              <div class="detail-item">
                <label>상품명:</label>
                <span>{{ selectedReservation.productName || '상품명 없음' }}</span>
              </div>
              <div class="detail-item">
                <label>예약일:</label>
                <span>{{ formatDate(selectedReservation.fishingAt) }}</span>
              </div>
              <div class="detail-item">
                <label>상태:</label>
                <span class="status-badge" :class="getStatusClass(selectedReservation.reservationStatus)">
                  {{ getStatusText(selectedReservation.reservationStatus) }}
                </span>
              </div>
              <div class="detail-item">
                <label>인원:</label>
                <span>{{ selectedReservation.numPerson }}명</span>
              </div>
              <div class="detail-item">
                <label>결제방법:</label>
                <span>{{ selectedReservation.paymentsMethod || '정보 없음' }}</span>
              </div>
              <div class="detail-item">
                <label>예약일시:</label>
                <span>{{ formatDateTime(selectedReservation.createdAt) }}</span>
              </div>
            </div>
          </div>

          <div class="detail-section">
            <h3>예약 관리</h3>
            <div class="action-buttons">
              <button
                v-if="selectedReservation.reservationStatus === 'PENDING'"
                class="btn-approve"
                @click="approveReservation(selectedReservation.reservationId)"
              >
                예약 승인
              </button>
              <button
                v-if="selectedReservation.reservationStatus === 'PENDING'"
                class="btn-reject"
                @click="rejectReservation(selectedReservation.reservationId)"
              >
                예약 거절
              </button>
              <button
                v-if="selectedReservation.reservationStatus === 'PAID'"
                class="btn-complete"
                @click="completeReservation(selectedReservation.reservationId)"
              >
                이용완료 처리
              </button>
              <button class="btn-cancel" @click="closeDetailModal">
                닫기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reservationApi } from '@/api/admin';

export default {
  name: 'Reservations',
  data() {
    return {
      searchQuery: '',
      dateFilter: '',
      statusFilter: '',
      allReservations: [],
      currentPage: 1,
      totalPages: 1,
      totalElements: 0,
      itemsPerPage: 10,
      showDetailModal: false,
      selectedReservation: null,
      activeTab: 'member',
      loading: false,
    }
  },
  computed: {
    reservations() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.allReservations.slice(startIndex, endIndex);
    },
    memberReservations() {
      return this.reservations.filter(r => r.userId || r.userUno); // userId나 userUno가 있으면 회원
    },
    guestReservations() {
      return this.reservations.filter(r => !r.userId && !r.userUno); // userId와 userUno가 없으면 비회원
    }
  },
  methods: {
    async searchReservations() {
      this.loading = true;
      try {
        const params = {
          page: this.currentPage - 1,
          size: this.itemsPerPage
        };
        
        if (this.searchQuery) {
          params.search = this.searchQuery;
        }
        
        if (this.dateFilter) {
          params.date = this.dateFilter;
        }
        
        if (this.statusFilter) {
          params.status = this.statusFilter;
        }
        
        const response = await reservationApi.getAllReservations(params);
        console.log('관리자 예약 목록 API 응답:', response.data);
        
        this.allReservations = response.data.content || response.data || [];
        this.totalPages = response.data.totalPages || 1;
        this.totalElements = response.data.totalElements || this.allReservations.length;
        
        // 현재 페이지가 총 페이지 수를 초과하면 마지막 페이지로 설정
        if (this.currentPage > this.totalPages) {
          this.currentPage = this.totalPages || 1;
        }
      } catch (error) {
        console.error('예약 목록 조회 실패:', error);
        alert('예약 목록을 불러오는데 실패했습니다.');
        this.allReservations = [];
      } finally {
        this.loading = false;
      }
    },
    
    async viewReservationDetails(reservationId) {
      try {
        const response = await reservationApi.getReservationDetail(reservationId);
        this.selectedReservation = response.data.reservation;
        this.showDetailModal = true;
      } catch (error) {
        console.error('예약 상세 조회 실패:', error);
        alert('예약 상세 정보를 불러오는데 실패했습니다.');
      }
    },
    
    async approveReservation(reservationId) {
      try {
        await reservationApi.approveReservation(reservationId);
        alert('예약이 승인되었습니다.');
        
        // 해당 예약의 상태를 업데이트
        const reservation = this.allReservations.find(r => r.reservationId === reservationId);
        if (reservation) {
          reservation.reservationStatus = 'PAID';
        }
        
        this.closeDetailModal();
        this.searchReservations();
      } catch (error) {
        console.error('예약 승인 실패:', error);
        alert('예약 승인에 실패했습니다.');
      }
    },
    
    async rejectReservation(reservationId) {
      try {
        await reservationApi.rejectReservation(reservationId);
        alert('예약이 거절되었습니다.');
        
        // 해당 예약의 상태를 업데이트
        const reservation = this.allReservations.find(r => r.reservationId === reservationId);
        if (reservation) {
          reservation.reservationStatus = 'CANCELED';
        }
        
        this.closeDetailModal();
        this.searchReservations();
      } catch (error) {
        console.error('예약 거절 실패:', error);
        alert('예약 거절에 실패했습니다.');
      }
    },
    
    async completeReservation(reservationId) {
      try {
        await reservationApi.completeReservation(reservationId);
        alert('이용완료 처리가 완료되었습니다.');
        
        // 해당 예약의 상태를 업데이트
        const reservation = this.allReservations.find(r => r.reservationId === reservationId);
        if (reservation) {
          reservation.reservationStatus = 'COMPLETED';
        }
        
        this.closeDetailModal();
        this.searchReservations();
      } catch (error) {
        console.error('이용완료 처리 실패:', error);
        alert('이용완료 처리에 실패했습니다.');
      }
    },
    
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
        this.searchReservations();
      }
    },
    
    closeDetailModal() {
      this.showDetailModal = false;
      this.selectedReservation = null;
    },
    
    getStatusClass(status) {
      const statusMap = {
        'PENDING': 'status-pending',
        'PAID': 'status-confirmed',
        'CANCELED': 'status-cancelled',
        'COMPLETED': 'status-completed'
      };
      return statusMap[status] || 'status-other';
    },
    
    getStatusText(status) {
      const statusMap = {
        'PENDING': '예약대기',
        'PAID': '예약확정',
        'CANCELED': '예약취소',
        'COMPLETED': '이용완료'
      };
      return statusMap[status] || status;
    },
    
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    },
    
    formatDateTime(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    },
    
    getReservationAmount(reservation) {
      // 실제 금액 정보가 있으면 사용, 없으면 임시 계산
      if (reservation.amount) {
        return Math.floor(Number(reservation.amount)).toLocaleString();
      }
      // 임시로 인원수 * 50000원으로 계산
      return (reservation.numPerson * 50000).toLocaleString();
    }
  },
  mounted() {
    this.searchReservations();
  }
}
</script>

<style scoped>
.reservations {
  padding: 1rem;
}

.loading, .no-data {
  text-align: center;
  padding: 2rem;
  color: #666;
  font-size: 1.1rem;
}

.search-bar {
  margin-bottom: 1rem;
  display: flex;
  gap: 0.5rem;
}

.search-bar input,
.search-bar select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-bar input[type="text"] {
  flex: 1;
}

.search-bar button {
  padding: 0.5rem 1rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.reservations-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1rem;
}

.reservations-table th,
.reservations-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.reservations-table th {
  background-color: #f8f9fa;
  font-weight: 600;
}

.status-badge {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-pending {
  background-color: #fff3cd;
  color: #856404;
}

.status-confirmed {
  background-color: #d4edda;
  color: #155724;
}

.status-cancelled {
  background-color: #f8d7da;
  color: #721c24;
}

.status-completed {
  background-color: #cce5ff;
  color: #004085;
}

.status-other {
  background-color: #e2e3e5;
  color: #383d41;
}

.reservation-tabs {
  margin-bottom: 1rem;
  display: flex;
  gap: 0.5rem;
}

.reservation-tabs button {
  padding: 0.75rem 1.5rem;
  border: 1px solid #ddd;
  background-color: #f8f9fa;
  cursor: pointer;
  border-radius: 4px;
}

.reservation-tabs button.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 1rem;
}

.pagination button {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  background-color: white;
  cursor: pointer;
  border-radius: 4px;
}

.pagination button:disabled {
  background-color: #f8f9fa;
  color: #ccc;
  cursor: not-allowed;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #eee;
}

.modal-header h2 {
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.modal-body {
  padding: 1rem;
}

.detail-section {
  margin-bottom: 1.5rem;
}

.detail-section h3 {
  margin-bottom: 1rem;
  color: #333;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
}

.detail-item label {
  font-weight: 600;
  color: #666;
  margin-bottom: 0.25rem;
}

.action-buttons {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

.btn-approve, .btn-reject, .btn-complete, .btn-cancel {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-approve {
  background-color: #28a745;
  color: white;
}

.btn-reject {
  background-color: #dc3545;
  color: white;
}

.btn-complete {
  background-color: #17a2b8;
  color: white;
}

.btn-cancel {
  background-color: #6c757d;
  color: white;
}

@media (max-width: 768px) {
  .search-bar {
    flex-direction: column;
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
  
  .reservations-table {
    font-size: 0.875rem;
  }
  
  .reservations-table th,
  .reservations-table td {
    padding: 0.5rem;
  }
}
</style> 