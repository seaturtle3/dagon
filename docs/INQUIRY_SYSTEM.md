# 1:1 문의 시스템

## 개요

이 문서는 Dagon 프로젝트의 1:1 문의 시스템에 대한 상세한 설명을 제공합니다. 이 시스템은 사용자, 파트너, 관리자 간의 문의 및 답변을 관리하는 완전한 CRUD 기능을 제공합니다.

## 시스템 구조

### 1. 백엔드 구조

#### Controller Layer
- **파일**: `src/main/java/kroryi/dagon/controller/common/support/ApiInquiryController.java`
- **역할**: HTTP 요청 처리 및 응답 반환
- **주요 기능**:
  - 문의 생성 (POST `/api/inquiry`)
  - 문의 조회 (GET `/api/inquiry/{id}`)
  - 문의 수정 (PUT `/api/inquiry/{id}`)
  - 문의 삭제 (DELETE `/api/inquiry/{id}`)
  - 문의 목록 조회 (GET `/api/inquiry/admin`)
  - 문의 답변 (POST `/api/inquiry/{id}/answer`)
  - 내 문의 목록 (GET `/api/inquiry/my-inquiries`)

#### Service Layer
- **파일**: `src/main/java/kroryi/dagon/service/support/InquiryService.java`
- **역할**: 비즈니스 로직 처리
- **주요 기능**:
  - 문의 생성 및 검증
  - 권한 기반 문의 수정/삭제
  - 문의 상태 관리
  - 답변 처리

#### Repository Layer
- **파일**: `src/main/java/kroryi/dagon/repository/InquiryRepository.java`
- **역할**: 데이터베이스 접근
- **주요 기능**:
  - JPA 기반 데이터 조작
  - 커스텀 쿼리 메서드
  - 페이징 및 검색 지원

#### Entity
- **파일**: `src/main/java/kroryi/dagon/entity/Inquiry.java`
- **역할**: 데이터베이스 테이블 매핑
- **주요 필드**:
  - `id`: 문의 고유 ID
  - `user`: 문의 작성자
  - `partner`: 문의 대상 파트너 (선택적)
  - `title`: 문의 제목
  - `content`: 문의 내용
  - `inquiryType`: 문의 유형
  - `isAnswered`: 답변 여부
  - `answerContent`: 답변 내용
  - `createdAt`: 작성일
  - `answeredAt`: 답변일

### 2. 프론트엔드 구조

#### JavaScript API Client
- **파일**: `src/main/resources/static/js/inquiry-api.js`
- **역할**: 백엔드 API와의 통신
- **주요 클래스**:
  - `InquiryApiClient`: API 요청 처리
  - `InquiryUI`: 사용자 인터페이스 관리

#### HTML Template
- **파일**: `src/main/resources/templates/question/inquiry.html`
- **역할**: 문의 등록 및 목록 화면
- **주요 기능**:
  - 반응형 디자인
  - 실시간 문자 수 카운터
  - 동적 폼 필드 표시/숨김

## API 엔드포인트

### 1. 문의 생성
```http
POST /api/inquiry
Content-Type: application/json
Authorization: Bearer {token}

{
  "receiverType": "ADMIN|PARTNER",
  "partnerId": 123,
  "title": "문의 제목",
  "content": "문의 내용",
  "inquiryType": "PRODUCT|BUSINESS|SYSTEM|RESERVATION|CANCEL",
  "writerType": "USER"
}
```

### 2. 문의 조회
```http
GET /api/inquiry/{id}
Authorization: Bearer {token}
```

### 3. 문의 수정
```http
PUT /api/inquiry/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "title": "수정된 제목",
  "content": "수정된 내용",
  "inquiryType": "PRODUCT"
}
```

### 4. 문의 삭제
```http
DELETE /api/inquiry/{id}
Authorization: Bearer {token}
```

### 5. 문의 목록 조회 (관리자용)
```http
GET /api/inquiry/admin?page=0&size=10&keyword=검색어&status=true
Authorization: Bearer {token}
```

### 6. 문의 답변
```http
POST /api/inquiry/{id}/answer
Content-Type: application/json
Authorization: Bearer {token}

{
  "answerContent": "답변 내용"
}
```

### 7. 내 문의 목록
```http
GET /api/inquiry/my-inquiries
Authorization: Bearer {token}
```

## 사용자 역할별 권한

### 1. 일반 사용자 (USER)
- 자신의 문의 생성
- 자신의 문의 조회
- 자신의 문의 수정 (답변 전까지)
- 자신의 문의 삭제

### 2. 파트너 (PARTNER)
- 자신에게 온 문의 조회
- 자신에게 온 문의 삭제
- 자신에게 온 문의에 답변

### 3. 관리자 (ADMIN)
- 모든 문의 조회
- 모든 문의 삭제
- 모든 문의에 답변
- 문의 통계 조회

## 문의 유형

- **PRODUCT**: 상품 관련
- **BUSINESS**: 제휴 관련
- **SYSTEM**: 시스템 관련
- **RESERVATION**: 예약 관련
- **CANCEL**: 예약 취소 관련

## 데이터베이스 스키마

```sql
CREATE TABLE inquiries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_uno BIGINT NOT NULL,
    receiver_type VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    partner_uno BIGINT,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    writer_type VARCHAR(20) NOT NULL,
    inquiry_type VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    answer_content LONGTEXT,
    is_answered BOOLEAN NOT NULL DEFAULT FALSE,
    answered_at DATETIME,
    
    FOREIGN KEY (user_uno) REFERENCES users(uno),
    FOREIGN KEY (partner_uno) REFERENCES partners(uno)
);
```

## 프론트엔드 사용법

### 1. 기본 사용법
```javascript
// API 클라이언트 생성
const apiClient = new InquiryApiClient();

// 문의 생성
const result = await apiClient.createInquiry({
    receiverType: 'ADMIN',
    title: '문의 제목',
    content: '문의 내용',
    inquiryType: 'PRODUCT'
});

// 문의 목록 조회
const inquiries = await apiClient.getMyInquiries();
```

### 2. UI 자동 초기화
```html
<!-- HTML에서 자동으로 UI가 초기화됩니다 -->
<script src="/js/inquiry-api.js"></script>
```

## 에러 처리

### 1. 백엔드 에러 응답 형식
```json
{
  "success": false,
  "message": "에러 메시지"
}
```

### 2. 성공 응답 형식
```json
{
  "success": true,
  "message": "성공 메시지",
  "data": {
    // 응답 데이터
  }
}
```

## 보안 고려사항

1. **인증**: JWT 토큰 기반 인증
2. **권한**: 역할 기반 접근 제어 (RBAC)
3. **입력 검증**: 서버 사이드 유효성 검사
4. **SQL 인젝션 방지**: JPA 사용으로 자동 방지
5. **XSS 방지**: 입력 데이터 이스케이프 처리

## 성능 최적화

1. **페이징**: 대용량 데이터 처리
2. **인덱싱**: 자주 조회되는 필드에 인덱스 적용
3. **캐싱**: Redis를 통한 응답 캐싱 (필요시)
4. **비동기 처리**: 답변 알림 전송

## 모니터링 및 로깅

1. **로깅**: Log4j2를 통한 상세 로깅
2. **에러 추적**: 예외 발생 시 스택 트레이스 기록
3. **성능 모니터링**: API 응답 시간 측정

## 확장 가능성

1. **파일 첨부**: 문의에 이미지/파일 첨부 기능
2. **이메일 알림**: 답변 시 이메일 알림
3. **FAQ 연동**: 자주 묻는 질문과 연동
4. **통계 대시보드**: 문의 통계 시각화

## 테스트

### 1. 단위 테스트
```java
@SpringBootTest
class InquiryServiceTest {
    @Test
    void testCreateInquiry() {
        // 테스트 코드
    }
}
```

### 2. 통합 테스트
```java
@WebMvcTest(ApiInquiryController.class)
class ApiInquiryControllerTest {
    @Test
    void testCreateInquiryEndpoint() {
        // 테스트 코드
    }
}
```

## 배포 및 운영

1. **환경 설정**: application.properties에서 데이터베이스 설정
2. **데이터베이스 마이그레이션**: Flyway 또는 Liquibase 사용
3. **로드 밸런싱**: 다중 인스턴스 배포 시 고려사항
4. **백업**: 정기적인 데이터베이스 백업

## 문제 해결

### 1. 일반적인 문제들
- **토큰 만료**: 재로그인 필요
- **권한 부족**: 관리자에게 문의
- **네트워크 오류**: 인터넷 연결 확인

### 2. 디버깅
- 브라우저 개발자 도구에서 네트워크 탭 확인
- 서버 로그에서 에러 메시지 확인
- 데이터베이스 연결 상태 확인

## 업데이트 히스토리

- **v1.0.0**: 초기 버전 - 기본 CRUD 기능
- **v1.1.0**: 권한 기반 접근 제어 추가
- **v1.2.0**: 프론트엔드 UI 개선
- **v1.3.0**: 에러 처리 및 로깅 강화

## 문의 및 지원

시스템 사용 중 문제가 발생하면 다음 연락처로 문의해주세요:
- 이메일: support@dagon.com
- 전화: 02-1234-5678
- 운영시간: 평일 09:00-18:00 