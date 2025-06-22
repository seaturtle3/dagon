# Dagon 대량 데이터 생성 가이드

## 개요
Dagon 낚시 플랫폼의 테스트 및 개발을 위한 대량 샘플 데이터를 생성하는 스크립트들입니다.

## 생성되는 데이터
- **사용자**: 500명 (일반 사용자 400명 + 파트너 100명)
- **관리자**: 10명
- **상품**: 1,000개
- **상품 옵션**: 2,000개
- **예약**: 200개
- **조행기**: 500개
- **조행기 댓글**: 1,000개
- **조황정보**: 100개
- **조황정보 댓글**: 200개
- **공지사항**: 200개
- **FAQ**: 100개
- **이벤트**: 30개

## 실행 방법

### 1. 통합 실행 (권장)
모든 데이터를 한 번에 생성하려면 통합 스크립트를 사용하세요:

```bash
mysql -u [사용자명] -p [데이터베이스명] < run_all_data_generation.sql
```

예시:
```bash
mysql -u root -p dagon < run_all_data_generation.sql
```

### 2. 개별 실행
특정 데이터만 생성하려면 개별 스크립트를 사용하세요:

```bash
# 사용자 및 파트너 데이터 생성
mysql -u root -p dagon < user_data_generator.sql

# 상품 및 옵션 데이터 생성
mysql -u root -p dagon < mass_data_generator.sql

# 공지사항, FAQ, 이벤트 데이터 생성
mysql -u root -p dagon < notice_faq_event_generator.sql
```

### 3. 기존 데이터 삭제 후 재생성
기존 데이터를 모두 삭제하고 새로 생성하려면:

```bash
mysql -u root -p dagon < clean_and_sample_data.sql
```

## 파일 설명

### 주요 스크립트 파일
- `run_all_data_generation.sql`: 모든 데이터를 순서대로 생성하는 통합 스크립트
- `clean_and_sample_data.sql`: 기존 데이터 삭제 후 기본 샘플 데이터 생성
- `user_data_generator.sql`: 사용자, 파트너, 관리자 데이터 생성
- `mass_data_generator.sql`: 상품, 옵션, 조행기, 조황정보 데이터 생성
- `notice_faq_event_generator.sql`: 공지사항, FAQ, 이벤트 데이터 생성

### 데이터 특징
- **다양성**: 지역별, 낚시 유형별로 다양한 상품 생성
- **현실성**: 실제 낚시터 정보와 유사한 데이터 구조
- **연관성**: 외래키 관계를 고려한 데이터 생성
- **랜덤성**: 랜덤 함수를 활용한 자연스러운 데이터 분포

## 주의사항

### 1. 외래키 제약조건
- 삭제 순서가 중요합니다 (자식 테이블 → 부모 테이블)
- 매핑 테이블들(`prod_fish_species_mapping`, `prod_fishing_gear_mapping`, `prod_facility_mapping`)을 먼저 삭제해야 합니다

### 2. 데이터베이스 연결
- MySQL 데이터베이스에 연결되어 있어야 합니다
- 적절한 권한이 필요합니다 (INSERT, DELETE, ALTER 권한)

### 3. 실행 전 확인사항
- 데이터베이스 백업 권장
- 충분한 디스크 공간 확보
- 네트워크 연결 상태 확인

## 커스터마이징

### 데이터 수량 조정
각 스크립트 파일에서 `WHERE n <= [숫자]` 부분을 수정하여 생성할 데이터 수를 조정할 수 있습니다.

### 지역 및 낚시 유형 추가
`mass_data_generator.sql`의 regions와 product_types CROSS JOIN 부분을 수정하여 새로운 지역이나 낚시 유형을 추가할 수 있습니다.

### 비밀번호 설정
현재 모든 사용자의 비밀번호는 `$2a$10$encrypted_password_hash`로 설정되어 있습니다. 실제 사용을 위해서는 BCrypt로 암호화된 실제 비밀번호로 변경해야 합니다.

## 문제 해결

### 외래키 제약조건 오류
```
ERROR 1451 (23000): Cannot delete or update a parent row: a foreign key constraint fails
```
- 삭제 순서를 확인하세요
- 매핑 테이블들을 먼저 삭제해야 합니다

### 메모리 부족 오류
- 대량 데이터 생성 시 메모리 부족이 발생할 수 있습니다
- 데이터 수를 줄이거나 배치로 나누어 실행하세요

### 권한 오류
- 데이터베이스 사용자에게 적절한 권한이 있는지 확인하세요
- `GRANT ALL PRIVILEGES ON dagon.* TO 'username'@'localhost';`

## 실행 결과 확인

스크립트 실행 후 다음 쿼리로 생성된 데이터 수를 확인할 수 있습니다:

```sql
SELECT 
    '생성된 데이터 수:' as info,
    (SELECT COUNT(*) FROM users) as users,
    (SELECT COUNT(*) FROM partners) as partners,
    (SELECT COUNT(*) FROM product) as products,
    (SELECT COUNT(*) FROM prod_option) as product_options,
    (SELECT COUNT(*) FROM reservation) as reservations,
    (SELECT COUNT(*) FROM fishing_diary) as fishing_diaries,
    (SELECT COUNT(*) FROM fishing_diary_comment) as diary_comments,
    (SELECT COUNT(*) FROM fishing_report) as fishing_reports,
    (SELECT COUNT(*) FROM fishing_report_comments) as report_comments,
    (SELECT COUNT(*) FROM notice) as notices,
    (SELECT COUNT(*) FROM faq) as faqs,
    (SELECT COUNT(*) FROM event) as events;
```

## 지원
문제가 발생하거나 추가 기능이 필요한 경우 개발팀에 문의하세요. 