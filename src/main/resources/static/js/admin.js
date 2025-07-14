import api from '@/lib/axios';
import {BASE_URL} from "@/constants/baseUrl.js";
import { clearAuthData } from '@/utils/authUtils'


export const adminAuth = {
  // 관리자 로그인
  login: (loginData) => {
    return api.post('/api/admin/login', loginData);
  },
  
  // 관리자 로그아웃
  logout: () => {
    clearAuthData();
  },
  
  // 토큰 유효성 검사
  checkAuth: () => {
    return !!localStorage.getItem('token');
  }
};

// 예약 관리 관련 API
export const reservationApi = {
  // 예약 목록 조회 (검색/페이징 지원)
  getAllReservations: (params = {}) => {
    return api.get('/api/admin/reservations', { params });
  },

  // 예약 상세 조회
  getReservationDetail: (id) => {
    return api.get(`/api/admin/reservations/${id}`);
  },

  // 예약 승인
  approveReservation: (id) => {
    return api.put(`/api/admin/reservations/${id}/approve`);
  },

  // 예약 거절
  rejectReservation: (id) => {
    return api.put(`/api/admin/reservations/${id}/reject`);
  },

  // 이용완료 처리
  completeReservation: (id) => {
    return api.put(`/api/admin/reservations/${id}/complete`);
  },

  // 예약 통계
  getReservationStats: () => {
    return api.get('/api/admin/reservations/stats');
  }
};

// 파트너 신청 관련 API
export const partnerApplicationApi = {
  // 파트너 신청 목록 조회
  getApplications: (page = 0, size = 10, keyword = '', type = '', status = '') => {
    return api.get('/api/partners', {
      params: { page, size, keyword, type, status }
    });
  },

  // 파트너 신청 상세 조회
  getApplicationDetail: (id) => {
    return api.get(`/api/partners/${id}`);
  },

  // 파트너 신청 승인
  approveApplication: (id) => {
    return api.post(`/api/partners/${id}/approve`);
  },

  // 파트너 신청 반려
  rejectApplication: (id, reason) => {
    return api.post(`/api/partners/${id}/reject`, { reason });
  }
};

// 회원 관리 관련 API
export const memberApi = {
  // 회원 목록 조회
  getMembers: (page = 0, size = 10, search = '') => {
    return api.get('/api/admin/users', {
      params: { page, size, search }
    });
  },

  // 회원 상세 정보 조회
  getMemberDetail: (uid) => {
    return api.get(`/api/admin/user/${uid}`);
  },

  // 회원 정보 수정
  updateMember: (uno, memberData) => {
    return api.put(`/api/admin/user/${uno}`, memberData);
  },

  // 회원 삭제
  deleteMember: (uno) => {
    return api.delete(`/api/admin/user/${uno}`);
  },

  // 회원 비활성화
  deactivateMember: (uno) => {
    return api.put(`/api/users/${uno}/deactivate`);
  },

  // 회원 활성화
  reactivateMember: (uno) => {
    return api.put(`/api/users/${uno}/reactivate`);
  },

  sendAlarmToMembers({ receiverUids, title, content }) {
    // JWT 토큰에서 관리자 정보 추출
    const token = localStorage.getItem('token');
    let senderId = 'admin';
    
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        senderId = payload.sub || payload.aid || payload.id || 'admin';
      } catch (error) {
        console.error('토큰 디코딩 실패:', error);
      }
    }
    
    console.log('알림 전송 데이터:', {
      receiverUids,
      title,
      content,
      senderId,
      senderType: 'ADMIN'
    });
    
    // 백엔드에서 기대할 수 있는 여러 형태의 데이터 구조 시도
    const requestData = {
      receiverUids,
      title,
      content,
      senderId,
      senderType: 'ADMIN'
    };
    
    // sender 객체 형태로도 시도
    if (!requestData.senderId) {
      requestData.sender = {
        id: 'admin',
        type: 'ADMIN'
      };
    }
    
    return api.post('/api/notifications/bulk', requestData);
  }
};

// 알림 타입 상수
export const NOTIFICATION_TYPES = {
  NOTICE: 'NOTICE',
  RESERVATION: 'RESERVATION',
  ANSWER: 'ANSWER',
  SYSTEM: 'SYSTEM'
};

// 알림 관리 관련 API
export const notificationApi = {
  // 알림 목록 조회
  getNotifications: (page = 0, size = 10, uid = '', type = '') => {
    return api.get('/api/notifications', {
      params: { page, size, uid, type }
    });
  },

  // 알림 상세 조회
  getNotificationDetail: (id) => {
    return api.get(`/api/notifications/${id}`);
  },

  // 알림 읽음 처리
  markAsRead: (id) => {
    return api.put(`/api/notifications/${id}/read`);
  },

  // 알림 삭제
  deleteNotification: (id) => {
    return api.delete(`/api/notifications/${id}`);
  },

  // 단일 알림 발송
  sendSimpleNotification: (notificationData) => {
    return api.post('/api/notifications/admin-broadcast', notificationData);
  },

  // 전체 알림 발송
  sendBroadcastNotification: (notificationData) => {
    return api.post('/api/notifications/admin-broadcast', notificationData);
  }
};

// 파트너 관리 관련 API
export const partnerApi = {
  // 파트너 목록 조회 (검색/페이징 지원)
  getPartners: (params = {}) => {
    return api.get('/api/partner/all', { params });
  },

  // 파트너 상세 조회
  getPartnerDetail: (id) => {
    return api.get(`/api/partner/${id}`);
  },

  // 파트너 정보 수정
  updatePartner: (id, partnerData) => {
    return api.put(`/api/partner/update/${id}`, partnerData);
  },

  // 파트너 삭제
  deletePartner: (id) => {
    return api.delete(`/api/partner/my_page/${id}`);
  }
};

export default adminApi; 