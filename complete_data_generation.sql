SET NAMES 'utf8mb4';
-- 완전한 대량 데이터 생성 스크립트
-- 모든 테이블에 샘플 데이터 생성

SET FOREIGN_KEY_CHECKS = 0;

-- 1. 기존 데이터 삭제 (외래키 순서 고려)
DELETE FROM fishing_report_comments;
DELETE FROM fishing_report_image;
DELETE FROM prod_fish_species_mapping;
DELETE FROM prod_fishing_gear_mapping;
DELETE FROM prod_facility_mapping;
DELETE FROM fishing_diary_comment;
DELETE FROM fishing_diary_image;
DELETE FROM free_board_comments;
DELETE FROM user_actions;
DELETE FROM notification;
DELETE FROM reservation;
DELETE FROM partner_applications;
DELETE FROM inquiries;
DELETE FROM fishing_report;
DELETE FROM fishing_diary;
DELETE FROM free_board;
DELETE FROM user_reports;
DELETE FROM question_answers;
DELETE FROM question;
DELETE FROM notice;
DELETE FROM faq;
DELETE FROM event;
DELETE FROM admin;
DELETE FROM prod_option;
DELETE FROM product;
DELETE FROM partners;
DELETE FROM users;
DELETE FROM tide_station;
DELETE FROM wave_station;

SET FOREIGN_KEY_CHECKS = 1;

-- 2. auto_increment 초기화
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE partners AUTO_INCREMENT = 1;
ALTER TABLE product AUTO_INCREMENT = 1;
ALTER TABLE prod_option AUTO_INCREMENT = 1;
ALTER TABLE reservation AUTO_INCREMENT = 1;
ALTER TABLE fishing_diary AUTO_INCREMENT = 1;
ALTER TABLE fishing_diary_comment AUTO_INCREMENT = 1;
ALTER TABLE fishing_diary_image AUTO_INCREMENT = 1;
ALTER TABLE fishing_report AUTO_INCREMENT = 1;
ALTER TABLE fishing_report_comments AUTO_INCREMENT = 1;
ALTER TABLE fishing_report_image AUTO_INCREMENT = 1;
ALTER TABLE free_board AUTO_INCREMENT = 1;
ALTER TABLE free_board_comments AUTO_INCREMENT = 1;
ALTER TABLE user_actions AUTO_INCREMENT = 1;
ALTER TABLE notification AUTO_INCREMENT = 1;
ALTER TABLE inquiries AUTO_INCREMENT = 1;
ALTER TABLE notice AUTO_INCREMENT = 1;
ALTER TABLE faq AUTO_INCREMENT = 1;
ALTER TABLE event AUTO_INCREMENT = 1;
ALTER TABLE user_reports AUTO_INCREMENT = 1;
ALTER TABLE question AUTO_INCREMENT = 1;
ALTER TABLE question_answers AUTO_INCREMENT = 1;
ALTER TABLE partner_applications AUTO_INCREMENT = 1;
ALTER TABLE faq_category AUTO_INCREMENT = 1;
ALTER TABLE admin AUTO_INCREMENT = 1;
ALTER TABLE prod_fish_species_mapping AUTO_INCREMENT = 1;
ALTER TABLE prod_fishing_gear_mapping AUTO_INCREMENT = 1;
ALTER TABLE prod_facility_mapping AUTO_INCREMENT = 1;



-- 조황정보 테이블 확장
ALTER TABLE fishing_report MODIFY content TEXT;
ALTER TABLE fishing_diary MODIFY content TEXT;
ALTER TABLE free_board MODIFY content TEXT;
ALTER TABLE event MODIFY content TEXT;
ALTER TABLE notice MODIFY content TEXT;
ALTER TABLE faq MODIFY answer TEXT;


-- 3. 관리자 데이터 생성 (11개)
INSERT INTO admin (aid, apw, aname, role, uno) VALUES
('admin', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '슈퍼관리자', 'SUPER_ADMIN', 1),
('dagon_admin_001', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '김관리자', 'ADMIN', 2),
('dagon_admin_002', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '이관리자', 'ADMIN', 3),
('dagon_admin_003', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '조관리자', 'ADMIN', 4),
('dagon_admin_004', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '박관리자', 'ADMIN', 5),
('dagon_admin_005', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '최관리자', 'ADMIN', 6),
('dagon_admin_006', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '정관리자', 'ADMIN', 7),
('dagon_admin_007', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '강관리자', 'ADMIN', 8),
('dagon_admin_008', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '윤관리자', 'ADMIN', 9),
('dagon_admin_009', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '임관리자', 'ADMIN', 10),
('dagon_admin_010', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '한관리자', 'ADMIN', 11);

-- 4. 사용자 데이터 생성 (500명) - 일반 사용자 400명 + 파트너 100명
INSERT INTO users (uid, upw, uname, nickname, email, profile_img, phone, points, level, level_point, last_login_at, role, login_type, is_active, version, created_at)
SELECT 
    CONCAT(
        CASE 
            WHEN n <= 400 THEN 'user'
            ELSE 'partner'
        END,
        LPAD(n, 3, '0')
    ) as uid,
    '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO' as upw,
    CONCAT(
        CASE 
            WHEN n <= 100 THEN '김'
            WHEN n <= 200 THEN '이'
            WHEN n <= 300 THEN '박'
            WHEN n <= 400 THEN '최'
            ELSE '정'
        END,
        CASE 
            WHEN n <= 50 THEN '철수'
            WHEN n <= 100 THEN '영희'
            WHEN n <= 150 THEN '민수'
            WHEN n <= 200 THEN '지영'
            WHEN n <= 250 THEN '현우'
            WHEN n <= 300 THEN '미영'
            WHEN n <= 350 THEN '성준'
            WHEN n <= 400 THEN '수진'
            WHEN n <= 450 THEN '동현'
            ELSE '은지'
        END
    ) as uname,
    CONCAT(
        CASE 
            WHEN n <= 100 THEN '낚시왕'
            WHEN n <= 200 THEN '물고기사랑'
            WHEN n <= 300 THEN '바다낚시러'
            WHEN n <= 400 THEN '민물낚시'
            ELSE '낚시마스터'
        END,
        n
    ) as nickname,
    CONCAT(
        CASE 
            WHEN n <= 400 THEN 'user'
            ELSE 'partner'
        END,
        LPAD(n, 3, '0'),
        '@example.com'
    ) as email,
    CONCAT(
        CASE 
            WHEN n <= 400 THEN 'profile'
            ELSE 'partner'
        END,
        n,
        '.jpg'
    ) as profile_img,
    CONCAT(
        '010-',
        LPAD(FLOOR(RAND() * 9999), 4, '0'),
        '-',
        LPAD(FLOOR(RAND() * 9999), 4, '0')
    ) as phone,
    CASE 
        WHEN n <= 400 THEN FLOOR(RAND() * 3000) + 100
        ELSE FLOOR(RAND() * 5000) + 2000
    END as points,
    CASE 
        WHEN n <= 400 THEN 
            CASE 
                WHEN RAND() > 0.7 THEN 'GOLD'
                WHEN RAND() > 0.5 THEN 'SILVER'
                WHEN RAND() > 0.3 THEN 'PLATINUM'
                ELSE 'DIAMOND'
            END
        ELSE 
            CASE 
                WHEN RAND() > 0.7 THEN 'DIAMOND'
                WHEN RAND() > 0.5 THEN 'PLATINUM'
                WHEN RAND() > 0.3 THEN 'GOLD'
                ELSE 'SILVER'
            END
    END as level,
    CASE 
        WHEN n <= 400 THEN FLOOR(RAND() * 3000) + 100
        ELSE FLOOR(RAND() * 5000) + 2000
    END as level_point,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY) as last_login_at,
    CASE 
        WHEN n <= 400 THEN 'USER'
        ELSE 'PARTNER'
    END as role,
    'LOCAL' as login_type,
    true as is_active,
    0 as version,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY) as created_at
FROM (
    SELECT a.n + b.n * 10 + c.n * 100 as n
    FROM (SELECT 0 as n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a
    CROSS JOIN (SELECT 0 as n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b
    CROSS JOIN (SELECT 0 as n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) c
    WHERE a.n + b.n * 10 + c.n * 100 BETWEEN 1 AND 500
) numbers;

-- 5. 파트너 데이터 생성 (100개) - users의 uno와 매칭, 내용 랜덤 다양화
DELIMITER //
CREATE PROCEDURE generate_various_partners_100()
BEGIN
  DECLARE i INT DEFAULT 1;
  DECLARE uno_val INT;
  DECLARE ceo_names VARCHAR(100);
  DECLARE pnames VARCHAR(100);
  DECLARE paddress VARCHAR(200);
  DECLARE pinfo VARCHAR(200);
  WHILE i <= 100 DO
    SET uno_val = 80 + i;
    INSERT INTO partners (uno, version, pname, p_address, ceo_name, p_info, license, license_img, created_at)
    VALUES (
      uno_val,
      0,
      CONCAT(
        ELT(FLOOR(1 + (RAND() * 10)),
          '서울한강낚시', '부산해운대피싱', '인천송도낚시', '대구수성피싱', '광주무등산낚시',
          '강릉경포낚시', '제주바다피싱', '포항영일대낚시', '여수돌산낚시', '춘천소양강낚시'
        ),
        ' ',
        ELT(FLOOR(1 + (RAND() * 5)), '레저', '체험', '투어', '마스터', '월드'),
        i
      ),
      CONCAT(
        ELT(FLOOR(1 + (RAND() * 10)),
          '서울특별시 강남구 테헤란로 ', '부산광역시 해운대구 해운대로 ', '인천광역시 연수구 송도과학로 ',
          '대구광역시 수성구 동대구로 ', '광주광역시 북구 무등로 ', '강원도 강릉시 경강로 ',
          '제주특별자치도 제주시 첨단로 ', '경상북도 포항시 북구 영일대해수욕장로 ',
          '전라남도 여수시 돌산읍 돌산로 ', '강원도 춘천시 소양강로 '
        ),
        FLOOR(RAND()*100+1)
      ),
      ELT(FLOOR(1 + (RAND() * 10)),
        '김민수', '이지은', '박지훈', '최유리', '정우성', '한지민', '오세훈', '이준호', '박소연', '정현우'
      ),
      CONCAT(
        ELT(FLOOR(1 + (RAND() * 10)),
          '전국 최대 규모의 낚시터', '가족과 함께 즐기는 체험', '초보자도 환영하는 친절한 운영',
          '최신 장비 완비', '자연과 함께하는 힐링', '도심 속 레저', '전문 가이드 동행',
          '다양한 어종 포획 가능', '연중무휴 운영', '프리미엄 서비스 제공'
        ),
        ' - ', i
      ),
      CONCAT('L', LPAD(uno_val, 9, '0')),
      CONCAT('license', uno_val, '.jpg'),
      DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)
    );
    SET i = i + 1;
  END WHILE;
END //
DELIMITER ;

-- CALL generate_various_partners_100();

-- 6. 상품 데이터 생성 (1000개)
SET @rownum := 0;
INSERT INTO product (prod_name, prod_region, main_type, sub_type, max_person, min_person, weight, prod_price, prod_address, prod_description, prod_event, prod_notice, uno, deleted, available_date, prod_thumbnail, created_at)
SELECT 
    -- prod_name
    CASE FLOOR(RAND() * 10)
        WHEN 0 THEN '해운대 방파제 도미 대박 체험'
        WHEN 1 THEN '광안리 야간 농어 트롤링'
        WHEN 2 THEN '송정 갯바위 감성돔 원정'
        WHEN 3 THEN '다대포 가족 우럭낚시'
        WHEN 4 THEN '기장 참돔 선상낚시'
        WHEN 5 THEN '태종대 볼락 밤낚시'
        WHEN 6 THEN '오륙도 방파제 볼락 밤낚시'
        WHEN 7 THEN '영도 선상 우럭낚시'
        WHEN 8 THEN '송도 갯바위 농어 원정'
        ELSE '민락수변공원 가족 체험낚시'
    END as prod_name,
    -- prod_region
    CASE FLOOR(RAND() * 16)
        WHEN 0 THEN 'BUSAN'
        WHEN 1 THEN 'INCHEON'
        WHEN 2 THEN 'DAEGU'
        WHEN 3 THEN 'GWANGJU'
        WHEN 4 THEN 'DAEJEON'
        WHEN 5 THEN 'ULSAN'
        WHEN 6 THEN 'SEJONG'
        WHEN 7 THEN 'JEONBUK'
        WHEN 8 THEN 'JEONNAM'
        WHEN 9 THEN 'CHUNGBUK'
        WHEN 10 THEN 'CHUNGNAM'
        WHEN 11 THEN 'GYEONGBUK'
        WHEN 12 THEN 'GYEONGNAM'
        WHEN 13 THEN 'GANGWON'
        WHEN 14 THEN 'JEJU'
        ELSE 'SEOUL'
    END as prod_region,
    -- main_type
    CASE WHEN FLOOR(RAND() * 16) IN (0,1,5,8,11,12,14) THEN 'SEA' ELSE 'FRESHWATER' END as main_type,
    -- sub_type
    CASE FLOOR(RAND() * 10) % 4 WHEN 0 THEN 'BREAK_WATER' WHEN 1 THEN 'RESERVOIR' WHEN 2 THEN 'BOAT' ELSE 'RIVER' END as sub_type,
    FLOOR(RAND() * 10) + 5 as max_person,
    FLOOR(RAND() * 3) + 1 as min_person,
    0.00 as weight,
    -- prod_price (상품 기본 가격)
    CASE 
        WHEN FLOOR(RAND() * 10) % 3 = 0 THEN FLOOR(RAND() * 20000) + 30000  -- 3만원~5만원
        WHEN FLOOR(RAND() * 10) % 3 = 1 THEN FLOOR(RAND() * 30000) + 50000  -- 5만원~8만원
        ELSE FLOOR(RAND() * 50000) + 80000  -- 8만원~13만원
    END as prod_price,
    -- prod_address
    CASE FLOOR(RAND() * 16)
        WHEN 0 THEN '부산광역시 해운대구 해운대해변로'
        WHEN 1 THEN '인천광역시 연수구 컨벤시아대로'
        WHEN 2 THEN '대구광역시 중구 동성로'
        WHEN 3 THEN '광주광역시 서구 상무중앙로'
        WHEN 4 THEN '대전광역시 서구 둔산로'
        WHEN 5 THEN '울산광역시 남구 삼산로'
        WHEN 6 THEN '세종특별자치시 한누리대로'
        WHEN 7 THEN '전라북도 전주시 완산구 홍산로'
        WHEN 8 THEN '전라남도 목포시 영산로'
        WHEN 9 THEN '충청북도 청주시 상당구 상당로'
        WHEN 10 THEN '충청남도 천안시 서북구 불당로'
        WHEN 11 THEN '경상북도 포항시 남구 시청로'
        WHEN 12 THEN '경상남도 창원시 성산구 중앙대로'
        WHEN 13 THEN '강원도 춘천시 중앙로'
        WHEN 14 THEN '제주특별자치도 제주시 연동'
        ELSE '서울특별시 강남구 테헤란로'
    END as prod_address,
    -- prod_description
    CASE FLOOR(RAND() * 10)
        WHEN 0 THEN '해운대 방파제에서 도미와 농어를 동시에! 오늘은 대물의 날!'
        WHEN 1 THEN '광안리 야경과 함께하는 농어 트롤링, 짜릿한 손맛 보장!'
        WHEN 2 THEN '송정 갯바위에서 감성돔 원정대 모집!'
        WHEN 3 THEN '다대포에서 가족과 함께 우럭낚시, 추억을 만드세요!'
        WHEN 4 THEN '기장 앞바다에서 참돔 선상낚시, 대물 도전!'
        WHEN 5 THEN '태종대에서 볼락 밤낚시, 랜턴 필수!'
        WHEN 6 THEN '오륙도 방파제에서 볼락 밤낚시, 손맛 최고!'
        WHEN 7 THEN '영도 선상에서 우럭낚시, 바다의 짜릿함!'
        WHEN 8 THEN '송도 갯바위에서 농어 원정, 회거리 득템!'
        ELSE '민락수변공원에서 가족 체험낚시, 어린이 강추!'
    END as prod_description,
    -- prod_event, prod_notice 기존과 동일
    CASE 
        WHEN RAND() > 0.9 THEN 'SNS 인증시 루어 증정!'
        WHEN RAND() > 0.8 THEN '주말 한정 선상낚시 1만원 할인'
        WHEN RAND() > 0.7 THEN '가족 동반시 어린이 무료'
        WHEN RAND() > 0.6 THEN '신규 고객 20% 할인'
        WHEN RAND() > 0.5 THEN '주말 패키지 30% 할인'
        WHEN RAND() > 0.4 THEN '평일 할인 이벤트'
        WHEN RAND() > 0.3 THEN '선착순 예약시 쿨러 증정'
        WHEN RAND() > 0.2 THEN '단체 예약시 추가 할인'
        ELSE '참가자 전원 기념품 증정'
    END as prod_event,
    CASE 
        WHEN RAND() > 0.9 THEN '구명조끼 필수! 안전이 최우선입니다.'
        WHEN RAND() > 0.8 THEN '우천시 일정이 변경될 수 있습니다.'
        WHEN RAND() > 0.7 THEN '주차장 완비, 샤워실 무료!'
        WHEN RAND() > 0.6 THEN '낚시도구 대여 가능, 생선회 제공'
        WHEN RAND() > 0.5 THEN '선박 안전교육 필수, 구명조끼 착용'
        WHEN RAND() > 0.4 THEN '온수 샤워장 완비, 숙박시설 연계 가능'
        WHEN RAND() > 0.3 THEN '어린이 보호자 동반 필수'
        WHEN RAND() > 0.2 THEN '음주 낚시 금지, 안전수칙 준수'
        ELSE '현장 결제 가능, 카드 환영'
    END as prod_notice,
    p.uno,
    false as deleted,
    '2024-12-31' as available_date,
    CONCAT('product', @rownum, '.jpg') as prod_thumbnail,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum, p.* FROM partners p, (SELECT @rownum := 0) r
    LIMIT 1000
) p;

-- 6-0. 상품 어구 테이블 초기화
DELETE FROM prod_fishing_gear;
-- 6-1. 상품 어구 데이터 생성 (5개)
INSERT INTO prod_fishing_gear (fg_id, fg_name) VALUES
(1, '릴'),
(2, '낚싯대'),
(3, '바늘'),
(4, '찌'),
(5, '미끼');

-- 6-2. 상품 시설 테이블 초기화
DELETE FROM prod_facility;
-- 6-3. 상품 시설 데이터 생성 (5개)
INSERT INTO prod_facility (fa_id, fa_name) VALUES
(1, '화장실'),
(2, '매점'),
(3, '주차장'),
(4, '휴게실'),
(5, '샤워실');

-- 7. 상품 옵션 데이터 생성 (2000개) - 상품당 2개씩
SET @rownum := 0;
INSERT INTO prod_option (opt_name, opt_price, prod_id, opt_description, opt_time)
SELECT 
    CONCAT(
        CASE 
            WHEN @rownum % 2 = 1 THEN '기본 패키지'
            ELSE '프리미엄 패키지'
        END,
        ' #', @rownum
    ) as opt_name,
    CASE 
        WHEN @rownum % 2 = 1 THEN FLOOR(RAND() * 50000) + 20000
        ELSE FLOOR(RAND() * 100000) + 80000
    END as opt_price,
    prod_id,
    CONCAT(
        CASE 
            WHEN @rownum % 2 = 1 THEN '낚시대, 릴, 미끼 포함. 기본적인 낚시 체험을 제공합니다.'
            ELSE '기본 패키지 + 전문 가이드 동행, 생선회 제공, 낚시 도구 고급 세트'
        END,
        ' #', @rownum
    ) as opt_description,
    CASE 
        WHEN @rownum % 2 = 1 THEN FLOOR(RAND() * 3) + 3
        ELSE FLOOR(RAND() * 3) + 6
    END as opt_time
FROM (
    SELECT @rownum := @rownum + 1 AS rownum, p.prod_id FROM product p, (SELECT @rownum := 0) r
    LIMIT 1000
) p
CROSS JOIN (
    SELECT 1 as n UNION SELECT 2
) numbers;

-- 8. 예약 데이터 생성 (200개)
INSERT INTO reservation (uid, prod_id, opt_id, num_person, option_quantity, amount, fishing_at, paid_at, reservation_status, payment_method, created_at)
SELECT 
    u.uno as uid,
    p.prod_id,
    o.opt_id,
    FLOOR(RAND() * 5) + 1 as num_person,
    FLOOR(RAND() * 3) + 1 as option_quantity,
    ((p.prod_price * (FLOOR(RAND() * 5) + 1)) + (o.opt_price * (FLOOR(RAND() * 3) + 1))) as amount,
    DATE_ADD(NOW(), INTERVAL FLOOR(RAND() * 30) DAY) as fishing_at,
    CASE 
        WHEN RAND() > 0.3 THEN DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY)
        ELSE NULL
    END as paid_at,
    CASE 
        WHEN RAND() > 0.7 THEN 'PAID'
        WHEN RAND() > 0.5 THEN 'PENDING'
        ELSE 'CANCELED'
    END as reservation_status,
    CASE 
        WHEN RAND() > 0.7 THEN 'KAKAO_PAY'
        WHEN RAND() > 0.5 THEN 'CARD'
        ELSE 'BANK_TRANSFER'
    END as payment_method,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY) as created_at
FROM (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 200
) u
CROSS JOIN (
    SELECT prod_id, prod_price FROM product LIMIT 100
) p
CROSS JOIN (
    SELECT opt_id, prod_id, opt_price FROM prod_option LIMIT 200
) o
WHERE o.prod_id = p.prod_id
LIMIT 200;

-- 9. 조행기 데이터 생성 (500개)
SET @rownum := 0;
INSERT INTO fishing_diary (title, content, thumbnail_url, fishing_at, modify_at, views, uid, prod_id, created_at)
SELECT 
    CONCAT(
        CASE FLOOR(RAND() * 10)
            WHEN 0 THEN '해운대' WHEN 1 THEN '서해안' WHEN 2 THEN '남해' WHEN 3 THEN '동해' WHEN 4 THEN '제주도'
            WHEN 5 THEN '가평' WHEN 6 THEN '양평' WHEN 7 THEN '한강' WHEN 8 THEN '낙동강' ELSE '금강'
        END,
        '에서 잡은 ',
        CASE FLOOR(RAND() * 12)
            WHEN 0 THEN '도미' WHEN 1 THEN '우럭' WHEN 2 THEN '광어' WHEN 3 THEN '농어' WHEN 4 THEN '감성돔'
            WHEN 5 THEN '참돔' WHEN 6 THEN '고등어' WHEN 7 THEN '갈치' WHEN 8 THEN '배스' WHEN 9 THEN '붕어'
            WHEN 10 THEN '잉어' ELSE '송어'
        END,
        ' ',
        CASE FLOOR(RAND() * 5)
            WHEN 0 THEN '대박 조행기!' WHEN 1 THEN '손맛 최고!' WHEN 2 THEN '인생고기 잡았어요' WHEN 3 THEN '짜릿한 하루' ELSE '가족과 함께 즐거운 시간'
        END
    ) as title,
    CONCAT(
        CASE FLOOR(RAND() * 5)
            WHEN 0 THEN '오늘은 정말 최고의 하루였습니다. '
            WHEN 1 THEN '오랜만에 떠난 낚시 여행, 정말 즐거웠어요. '
            WHEN 2 THEN '날씨도 좋고 모든 것이 완벽했던 날! '
            WHEN 3 THEN '힘들었지만 보람찬 낚시였습니다. '
            ELSE '잊지 못할 추억을 만들고 왔습니다. '
        END,
        '오늘 ',
        CASE FLOOR(RAND() * 10)
            WHEN 0 THEN '해운대' WHEN 1 THEN '서해안' WHEN 2 THEN '남해' WHEN 3 THEN '동해' WHEN 4 THEN '제주도'
            WHEN 5 THEN '가평' WHEN 6 THEN '양평' WHEN 7 THEN '한강' WHEN 8 THEN '낙동강' ELSE '금강'
        END,
        '에서 ',
        CASE FLOOR(RAND() * 12)
            WHEN 0 THEN '도미' WHEN 1 THEN '우럭' WHEN 2 THEN '광어' WHEN 3 THEN '농어' WHEN 4 THEN '감성돔'
            WHEN 5 THEN '참돔' WHEN 6 THEN '고등어' WHEN 7 THEN '갈치' WHEN 8 THEN '배스' WHEN 9 THEN '붕어'
            WHEN 10 THEN '잉어' ELSE '송어'
        END,
        '를 잡았어요. ',
        CASE FLOOR(RAND() * 5)
            WHEN 0 THEN '사이즈가 어마어마해서 끌어올리는데 한참 걸렸네요. '
            WHEN 1 THEN '마릿수는 많지 않았지만 손맛은 최고였습니다. '
            WHEN 2 THEN '함께 간 친구들과 나눠먹으니 더 맛있었어요. '
            WHEN 3 THEN '처음 가본 포인트인데, 앞으로 자주 가게 될 것 같습니다. '
            ELSE '가족들도 모두 좋아해서 뿌듯한 하루였습니다. '
        END,
        '#낚시 #조행기 #',
        CASE FLOOR(RAND() * 12)
            WHEN 0 THEN '도미' WHEN 1 THEN '우럭' WHEN 2 THEN '광어' WHEN 3 THEN '농어' WHEN 4 THEN '감성돔'
            WHEN 5 THEN '참돔' WHEN 6 THEN '고등어' WHEN 7 THEN '갈치' WHEN 8 THEN '배스' WHEN 9 THEN '붕어'
            WHEN 10 THEN '잉어' ELSE '송어'
        END
    ) as content,
    CONCAT(
        '/uploads/',
        DATE_FORMAT(DATE_ADD('2022-01-01', INTERVAL FLOOR(RAND() * DATEDIFF(NOW(), '2022-01-01')) DAY), '%Y/%m/%d'),
        '/fishing_diary_',
        @rownum, '.jpg'
    ) as thumbnail_url,
    DATE_ADD('2022-01-01', INTERVAL FLOOR(RAND() * DATEDIFF(NOW(), '2022-01-01')) DAY) as fishing_at,
    NULL as modify_at,
    FLOOR(RAND() * 300) + 10 as views,
    u.uno as uid,
    p.prod_id,
    DATE_ADD('2022-01-01', INTERVAL FLOOR(RAND() * DATEDIFF(NOW(), '2022-01-01')) DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum, u.uno FROM (SELECT uno FROM users WHERE role = 'USER' LIMIT 500) u, (SELECT @rownum := 0) r
) u
CROSS JOIN (
    SELECT prod_id FROM product LIMIT 100
) p
LIMIT 500;

-- 10. 조행기 댓글 데이터 생성 (1000개)
INSERT INTO fishing_diary_comment (comment_content, uid, fd_id, created_at)
SELECT 
    CONCAT(
        CASE 
            WHEN @rownum % 5 = 1 THEN '정말 멋진 도미네요! 축하합니다!'
            WHEN @rownum % 5 = 2 THEN '해운대 낚시 정말 좋죠! 저도 다음에 가보고 싶어요.'
            WHEN @rownum % 5 = 3 THEN '잉어 2kg면 정말 대단하네요! 축하합니다.'
            WHEN @rownum % 5 = 4 THEN '청평호수는 정말 낚시하기 좋은 곳이에요.'
            ELSE '제주 해상낚시 정말 멋지네요! 저도 한번 가보고 싶어요.'
        END,
        ' #', @rownum
    ) as comment_content,
    u.uno as uid,
    fd.fd_id,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 200
) u
CROSS JOIN (
    SELECT fd_id FROM fishing_diary LIMIT 100
) fd
LIMIT 1000;

-- 11. 조황정보 데이터 생성 (200개)
INSERT INTO fishing_report (title, content, thumbnail_url, fishing_at, modify_at, views, uid, prod_id, created_at)
SELECT
    CONCAT(
        p.prod_region, ' ',
        p.prod_name,
        ' 조황 - ',
        CASE FLOOR(RAND() * 5)
            WHEN 0 THEN '역대급 마릿수!' WHEN 1 THEN '씨알 좋은 녀석들 출현!' WHEN 2 THEN '초보도 손맛 가능!' WHEN 3 THEN '물 반 고기 반!' ELSE '짜릿한 파이팅!'
        END
    ) as title,
    CONCAT(
        '오늘의 조황 브리핑입니다! ',
        CASE FLOOR(RAND() * 5)
            WHEN 0 THEN '날씨가 좋아서인지 활성도가 매우 높았습니다. '
            WHEN 1 THEN '밑밥에 반응이 폭발적이었습니다. '
            WHEN 2 THEN '특정 포인트에서 입질이 집중되었습니다. '
            WHEN 3 THEN '오전 피딩 타임에 대박이 터졌네요. '
            ELSE '밤낚시에 의외의 대물이 올라왔습니다. '
        END,
        '주요 어종은 ',
        (SELECT fs_name from fish_species ORDER BY RAND() LIMIT 1),
        '이며, ',
        CASE FLOOR(RAND() * 5)
            WHEN 0 THEN '평균 씨알은 30cm급으로 준수했습니다. '
            WHEN 1 THEN '4짜, 5짜 대물도 간간히 보였습니다. '
            WHEN 2 THEN '가족 단위로 오신 분들도 쉽게 손맛을 보셨습니다. '
            WHEN 3 THEN '쿨러 채우신 분들이 많네요! '
            ELSE '내일도 비슷한 조황이 예상됩니다. '
        END,
        '#조황정보 #낚시 #',
        p.prod_name
    ) as content,
    NULL as thumbnail_url,
    DATE_ADD('2022-01-01', INTERVAL FLOOR(RAND() * DATEDIFF(NOW(), '2022-01-01')) DAY) as fishing_at,
    NULL as modify_at,
    FLOOR(RAND() * 500) + 50 as views,
    p.uno as uid,
    p.prod_id,
    DATE_ADD('2022-01-01', INTERVAL FLOOR(RAND() * DATEDIFF(NOW(), '2022-01-01')) DAY) as created_at
FROM (
    SELECT * FROM product WHERE deleted = false ORDER BY RAND() LIMIT 200
) p;

-- 11-1. 조황정보 이미지 데이터 생성 (각 조황정보별 1~3장 랜덤)
INSERT INTO fishing_report_image (image_url, is_thumbnail, order_index, fr_id)
SELECT
    CONCAT(
        '/uploads/fishing-report/',
        DATE_FORMAT(fr.created_at, '%Y/%m/%d'),
        '/report_',
        fr.fr_id,
        '_',
        img_idx.n,
        '.jpg'
    ) as image_url,
    (img_idx.n = 1) as is_thumbnail,
    (img_idx.n - 1) as order_index,
    fr.fr_id
FROM (
    SELECT fr_id, created_at, FLOOR(1 + RAND(fr_id) * 3) AS img_count
    FROM fishing_report
) fr
JOIN (
    SELECT 1 as n UNION ALL SELECT 2 UNION ALL SELECT 3
) img_idx
ON img_idx.n <= fr.img_count;

-- 11-2. 조황정보 썸네일 URL 업데이트
UPDATE fishing_report fr
JOIN (
    SELECT fr_id, image_url
    FROM fishing_report_image
    WHERE is_thumbnail = true
) fri ON fr.fr_id = fri.fr_id
SET fr.thumbnail_url = fri.image_url;

-- 12. 조황정보 댓글 데이터 생성 (400개)
INSERT INTO fishing_report_comments (comment_content, uid, fr_id, created_at, modify_at)
SELECT
    CASE FLOOR(RAND() * 20)
        WHEN 0 THEN '와, 조황 대박이네요! 부럽습니다!'
        WHEN 1 THEN '정보 감사합니다! 이번 주말에 출조합니다!'
        WHEN 2 THEN '이런 건 어디 가면 잡을 수 있나요? 포인트 공유 좀...'
        WHEN 3 THEN '씨알이 어마어마하네요. 손맛 좋으셨겠어요!'
        WHEN 4 THEN '역시 사장님 포인트는 믿고 갑니다.'
        WHEN 5 THEN '쿨러 조황 축하드립니다!'
        WHEN 6 THEN '사진만 봐도 힐링되네요.'
        WHEN 7 THEN '다음 주에도 좋은 조황 기대하겠습니다.'
        WHEN 8 THEN '사용하신 채비 정보 알 수 있을까요?'
        WHEN 9 THEN '물때가 언제가 좋았나요?'
        WHEN 10 THEN '저도 거기서 저번에 손맛 봤는데, 또 가고 싶네요.'
        WHEN 11 THEN '내일 예약했는데 기대됩니다!'
        WHEN 12 THEN '와... 저 큰 걸 어떻게 올리셨대요?'
        WHEN 13 THEN '회 맛이 끝내줬겠네요. 츄릅...'
        WHEN 14 THEN '안전 조행 하셨다니 다행입니다. 수고하셨어요!'
        WHEN 15 THEN '이런 정보 너무 소중해요. 감사합니다!'
        WHEN 16 THEN '조만간 저도 기록 깨러 가겠습니다. ㅎㅎ'
        WHEN 17 THEN '사장님 항상 친절하게 알려주셔서 감사해요.'
        WHEN 18 THEN '아이들과 같이 가도 괜찮을까요?'
        ELSE '장비 대여도 가능한가요?'
    END as comment_content,
    (SELECT uno FROM users WHERE role = 'USER' ORDER BY RAND() LIMIT 1) as uid,
    fr.fr_id,
    DATE_ADD(fr.created_at, INTERVAL FLOOR(RAND() * 3) DAY) as created_at,
    NULL as modify_at
FROM
    (SELECT fr_id, created_at FROM fishing_report ORDER BY RAND() LIMIT 200) fr,
    (SELECT 1 as n UNION ALL SELECT 2) i;

-- 13. 자유게시판 데이터 생성 (50개)
INSERT INTO free_board (title, content, thumbnail_url, modify_at, views, uid, created_at)
SELECT 
    CONCAT('자유게시판 제목 ', @rownum) as title,
    CONCAT('자유게시판 내용 ', @rownum) as content,
    CONCAT('board', @rownum, '.jpg') as thumbnail_url,
    NULL as modify_at,
    FLOOR(RAND() * 200) + 10 as views,
    u.uno as uid,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 50
) u
LIMIT 50;

-- 14. 자유게시판 댓글 데이터 생성 (100개)
INSERT INTO free_board_comments (comment_content, uid, fb_id, modify_at)
SELECT 
    CONCAT('자유게시판 댓글 ', @rownum) as comment_content,
    u.uno as uid,
    fb.fb_id,
    NULL as modify_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 50
) u
CROSS JOIN (
    SELECT fb_id FROM free_board LIMIT 50
) fb
LIMIT 100;

-- 15. 사용자 액션 데이터 생성 (50개)
INSERT INTO user_actions (action_type, uid, target_id, board_type)
SELECT 
    CASE WHEN @rownum % 2 = 0 THEN 'LIKE' ELSE 'WISH' END as action_type,
    u.uno as uid,
    @rownum as target_id,
    CASE WHEN @rownum % 2 = 0 THEN 'DIARY' ELSE 'PRODUCT' END as board_type
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 50
) u
LIMIT 50;

-- 16. 알림 데이터 생성 (20개)
INSERT INTO notification (title, content, sender_type, sender_id, receiver_id, is_read, created_at)
SELECT 
    CONCAT('알림 제목 ', @rownum) as title,
    CONCAT('알림 내용 ', @rownum) as content,
    'SYSTEM' as sender_type,
    1 as sender_id,
    u.uno as receiver_id,
    false as is_read,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 20
) u
LIMIT 20;

-- 17. 문의 데이터 생성 (10개)
INSERT INTO inquiries (title, content, inquiry_type, user_uno, is_answered, created_at, updated_at)
SELECT 
    CONCAT('문의 제목 ', @rownum) as title,
    CONCAT('문의 내용 ', @rownum) as content,
    'RESERVATION' as inquiry_type,
    u.uno as user_uno,
    false as is_answered,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as updated_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 10
) u
LIMIT 10;

-- 18. 공지사항 데이터 생성 (10개)
INSERT INTO notice (title, content, aid, created_at)
SELECT
    CONCAT('공지사항 제목 ', @rownum) as title,
    CONCAT('공지사항 내용 ', @rownum) as content,
    'dagon_admin_001' as aid,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
LIMIT 10;

-- 26. FAQ 카테고리 데이터 생성 (3개)
INSERT IGNORE INTO faq_category (name, display_order) VALUES
('일반회원', 0),
('파트너', 1),
('관리자', 2);

-- 19. FAQ 데이터 생성 (50개)
SET @rownum := 0;
INSERT INTO faq (question, answer, category_id, aid, is_active, created_at)
SELECT
    -- question
    CASE (@rownum := @rownum + 1)
        -- 예약/결제 (1-10)
        WHEN 1 THEN '예약 취소는 어떻게 하나요?'
        WHEN 2 THEN '결제 수단에는 어떤 것들이 있나요?'
        WHEN 3 THEN '예약 변경은 가능한가요?'
        WHEN 4 THEN '환불 규정은 어떻게 되나요?'
        WHEN 5 THEN '예약 확인은 어디서 하나요?'
        WHEN 6 THEN '카카오페이 결제가 안 돼요.'
        WHEN 7 THEN '무통장 입금 시 입금 확인은 언제 되나요?'
        WHEN 8 THEN '현장 결제도 가능한가요?'
        WHEN 9 THEN '예약 시 인원 추가는 어떻게 하나요?'
        WHEN 10 THEN '결제 영수증은 어떻게 발급받나요?'
        -- 계정/회원정보 (11-20)
        WHEN 11 THEN '회원가입은 어떻게 하나요?'
        WHEN 12 THEN '아이디/비밀번호를 잊어버렸어요.'
        WHEN 13 THEN '회원 정보를 수정하고 싶어요.'
        WHEN 14 THEN '회원 탈퇴는 어떻게 하나요?'
        WHEN 15 THEN '닉네임 변경이 가능한가요?'
        WHEN 16 THEN '이메일 주소를 변경하고 싶어요.'
        WHEN 17 THEN 'SNS 계정으로 가입했는데 비밀번호를 모르겠어요.'
        WHEN 18 THEN '휴면 계정은 어떻게 풀 수 있나요?'
        WHEN 19 THEN '본인인증이 되지 않아요.'
        WHEN 20 THEN '다른 사람 명의로 가입할 수 있나요?'
        -- 파트너 관련 (21-30)
        WHEN 21 THEN '파트너 신청은 어떻게 하나요?'
        WHEN 22 THEN '상품 등록은 어떻게 하나요?'
        WHEN 23 THEN '파트너 수수료는 얼마인가요?'
        WHEN 24 THEN '정산은 언제 이루어지나요?'
        WHEN 25 THEN '예약 관리는 어떻게 하나요?'
        WHEN 26 THEN '고객 문의는 어떻게 확인하나요?'
        WHEN 27 THEN '파트너 정보를 수정하고 싶어요.'
        WHEN 28 THEN '광고를 진행하고 싶어요.'
        WHEN 29 THEN '등록한 상품의 노출 순서는 어떻게 결정되나요?'
        WHEN 30 THEN '파트너 등급은 무엇인가요?'
        -- 서비스 이용 (31-40)
        WHEN 31 THEN '조행기는 어떻게 작성하나요?'
        WHEN 32 THEN '커뮤니티 이용 규칙은 무엇인가요?'
        WHEN 33 THEN '물때 정보는 정확한가요?'
        WHEN 34 THEN '신고 기능은 어떻게 사용하나요?'
        WHEN 35 THEN '찜한 상품은 어디서 보나요?'
        WHEN 36 THEN '어떤 어종 정보를 제공하나요?'
        WHEN 37 THEN '알림 설정은 어떻게 변경하나요?'
        WHEN 38 THEN '검색이 제대로 되지 않아요.'
        WHEN 39 THEN '앱에서도 이용할 수 있나요?'
        WHEN 40 THEN '후기 작성은 어떻게 하나요?'
        -- 포인트 및 기타 (41-50)
        WHEN 41 THEN '포인트는 어떻게 적립되나요?'
        WHEN 42 THEN '포인트는 어떻게 사용하나요?'
        WHEN 43 THEN '쿠폰은 어디서 확인할 수 있나요?'
        WHEN 44 THEN '포인트 유효기간이 있나요?'
        WHEN 45 THEN '고객센터 운영시간은 어떻게 되나요?'
        WHEN 46 THEN '이벤트는 어디서 확인하나요?'
        WHEN 47 THEN '제휴 문의는 어떻게 하나요?'
        WHEN 48 THEN '오류가 발생했어요.'
        WHEN 49 THEN '친구 초대 이벤트는 없나요?'
        ELSE '탈퇴 시 포인트는 어떻게 되나요?'
    END as question,
    -- answer
    CASE @rownum
        -- 예약/결제 (1-10)
        WHEN 1 THEN '마이페이지 > 예약 내역에서 "예약 취소" 버튼을 통해 직접 취소하실 수 있습니다. 단, 파트너가 설정한 취소 수수료 정책에 따라 수수료가 부과될 수 있습니다.'
        WHEN 2 THEN '현재 카카오페이, 신용카드, 계좌이체 등 다양한 결제 수단을 지원하고 있습니다. 상품별로 지원하는 결제 수단이 다를 수 있습니다.'
        WHEN 3 THEN '예약 변경 기능은 현재 지원되지 않습니다. 번거로우시겠지만 기존 예약을 취소하신 후 새로 예약해주시기 바랍니다.'
        WHEN 4 THEN '환불은 각 파트너사의 규정에 따라 처리됩니다. 예약 페이지의 환불 규정을 반드시 확인해주세요.'
        WHEN 5 THEN '마이페이지 > 예약 내역에서 상세한 예약 정보를 확인하실 수 있습니다.'
        WHEN 6 THEN '카카오페이 앱 또는 네트워크 문제일 수 있습니다. 잠시 후 다시 시도해보시거나 다른 결제 수단을 이용해주세요.'
        WHEN 7 THEN '입금 확인은 자동화 시스템을 통해 10분 이내에 처리됩니다. 1시간 이상 확인이 되지 않을 경우 고객센터로 문의해주세요.'
        WHEN 8 THEN '현장 결제 가능 여부는 파트너가 등록한 상품 정보에 따라 다릅니다. 상품 상세 페이지를 확인해주세요.'
        WHEN 9 THEN '예약 완료 후에는 인원 추가가 불가능합니다. 예약을 취소하고 인원을 포함하여 새로 예약해주셔야 합니다.'
        WHEN 10 THEN '결제 영수증은 결제 대행사(PG)를 통해 발급되며, 마이페이지 > 예약 내역에서 출력할 수 있습니다.'
        -- 계정/회원정보 (11-20)
        WHEN 11 THEN '홈페이지 상단의 "회원가입" 메뉴를 통해 약관 동의 및 정보 입력을 하시면 가입이 완료됩니다. SNS 계정을 통한 간편가입도 가능합니다.'
        WHEN 12 THEN '로그인 페이지 하단의 "아이디/비밀번호 찾기" 기능을 이용해주세요. 가입 시 인증한 이메일 또는 휴대폰 번호로 정보를 보내드립니다.'
        WHEN 13 THEN '마이페이지 > 회원정보 수정 메뉴에서 비밀번호 재확인 후 정보를 수정할 수 있습니다.'
        WHEN 14 THEN '마이페이지 > 회원정보 수정 페이지 하단의 "회원 탈퇴" 버튼을 통해 진행할 수 있습니다. 탈퇴 시 모든 정보는 복구되지 않으니 신중하게 결정해주세요.'
        WHEN 15 THEN '네, 마이페이지 > 회원정보 수정에서 닉네임을 변경할 수 있습니다. 닉네임은 30일에 한 번만 변경 가능합니다.'
        WHEN 16 THEN '마이페이지 > 회원정보 수정에서 이메일 주소를 변경할 수 있습니다. 이메일 인증이 필요합니다.'
        WHEN 17 THEN 'SNS 간편가입 회원은 별도의 비밀번호가 없습니다. SNS 계정으로 로그인해주시기 바랍니다.'
        WHEN 18 THEN '1년 이상 로그인하지 않은 계정은 휴면 처리됩니다. 로그인 시 자동으로 휴면이 해제됩니다.'
        WHEN 19 THEN '본인인증 서비스 제공업체의 일시적인 오류일 수 있습니다. 잠시 후 다시 시도해보시거나 고객센터로 문의해주세요.'
        WHEN 20 THEN '가입은 반드시 본인 명의로 해야 합니다. 타인 명의 도용 시 법적 처벌을 받을 수 있습니다.'
        -- 파트너 관련 (21-30)
        WHEN 21 THEN '홈페이지 하단의 "파트너 센터"를 통해 신청할 수 있습니다. 사업자 정보 및 관련 서류 제출 후 관리자 승인을 거쳐 가입이 완료됩니다.'
        WHEN 22 THEN '파트너 센터 로그인 후, "상품 관리 > 새 상품 등록" 메뉴에서 상품 정보, 가격, 사진 등을 등록할 수 있습니다.'
        WHEN 23 THEN '파트너 수수료는 계약 조건에 따라 다르며, 파트너 센터에서 확인하거나 담당자에게 문의해주시기 바랍니다.'
        WHEN 24 THEN '정산은 매월 지정된 날짜에 이루어집니다. 자세한 정산일과 내역은 파트너 센터 > 정산 관리에서 확인하실 수 있습니다.'
        WHEN 25 THEN '파트너 센터의 "예약 관리" 메뉴에서 실시간 예약 현황을 확인하고, 예약 확정 및 취소 처리를 할 수 있습니다.'
        WHEN 26 THEN '고객이 상품에 대해 남긴 문의는 파트너 센터의 "고객 문의 관리" 메뉴에서 확인하고 답변할 수 있습니다.'
        WHEN 27 THEN '파트너 센터 > "계정 관리" 메뉴에서 업체 정보, 담당자 정보 등을 수정할 수 있습니다.'
        WHEN 28 THEN '다양한 광고 상품을 운영하고 있습니다. 파트너 센터의 "광고 관리" 메뉴를 참고하시거나 광고 담당자에게 문의해주세요.'
        WHEN 29 THEN '상품 노출 순서는 판매량, 후기, 평점 등 다양한 요소를 종합한 랭킹 알고리즘에 따라 결정됩니다.'
        WHEN 30 THEN '파트너 등급은 판매 실적, 고객 만족도 등을 기준으로 부여되며, 등급에 따라 다양한 혜택이 제공됩니다.'
        -- 서비스 이용 (31-40)
        WHEN 31 THEN '커뮤니티 > 조행기 게시판에서 "글쓰기" 버튼을 통해 작성할 수 있습니다. 풍부한 내용과 사진을 공유하면 다른 회원들에게 큰 도움이 됩니다.'
        WHEN 32 THEN '욕설, 비방, 광고 등 커뮤니티 분위기를 해치는 행위는 금지되며, 위반 시 서비스 이용이 제한될 수 있습니다. 자세한 내용은 공지사항을 확인해주세요.'
        WHEN 33 THEN '물때 정보는 국립해양조사원의 공공 데이터를 기반으로 제공되지만, 기상 상황에 따라 실제와 다소 차이가 있을 수 있습니다.'
        WHEN 34 THEN '부적절한 게시물이나 댓글 발견 시, 해당 게시물의 신고 버튼을 통해 신고할 수 있습니다. 신고된 내용은 관리자 검토 후 처리됩니다.'
        WHEN 35 THEN '마이페이지 > 찜 목록에서 찜한 상품들을 모아볼 수 있습니다.'
        WHEN 36 THEN '국내 주요 바다, 민물 어종에 대한 정보와 금어기, 주요 출몰 지역 등의 정보를 제공합니다.'
        WHEN 37 THEN '마이페이지 > 알림 설정에서 원하는 종류의 알림(예약, 마케팅 등)만 받도록 설정하거나 모든 알림을 끌 수 있습니다.'
        WHEN 38 THEN '검색어의 오타를 확인해보시거나, 필터 조건을 변경하여 다시 검색해보세요. 지속적으로 문제가 발생하면 고객센터로 문의해주세요.'
        WHEN 39 THEN '네, 모바일 웹 및 전용 앱(안드로이드/iOS)을 통해 모든 서비스를 동일하게 이용하실 수 있습니다.'
        WHEN 40 THEN '실제로 이용한 상품에 대해서만 후기를 작성할 수 있습니다. 마이페이지 > 예약 내역에서 "후기 작성" 버튼을 눌러주세요.'
        -- 포인트 및 기타 (41-50)
        WHEN 41 THEN '상품 구매 확정 시 결제 금액의 일부가 포인트로 적립됩니다. 또한 다양한 이벤트를 통해 추가 포인트를 얻을 수 있습니다.'
        WHEN 42 THEN '결제 페이지에서 보유 포인트를 사용하여 결제 금액을 할인받을 수 있습니다. 1000포인트 이상부터 사용 가능합니다.'
        WHEN 43 THEN '마이페이지 > 쿠폰함에서 보유한 쿠폰을 확인할 수 있습니다. 쿠폰마다 사용 조건과 유효기간이 다르니 확인 후 사용해주세요.'
        WHEN 44 THEN '네, 포인트의 유효기간은 적립일로부터 1년입니다. 기간 내에 사용하지 않은 포인트는 자동 소멸됩니다.'
        WHEN 45 THEN '고객센터는 평일 오전 9시부터 오후 6시까지 운영됩니다. (점심시간 12시~1시, 주말/공휴일 휴무)'
        WHEN 46 THEN '홈페이지 상단의 "이벤트" 메뉴에서 진행 중인 다양한 이벤트를 확인하고 참여할 수 있습니다.'
        WHEN 47 THEN '홈페이지 하단의 "제휴 문의" 링크를 통해 문의 내용을 남겨주시면 담당자가 검토 후 연락드립니다.'
        WHEN 48 THEN '서비스 이용 중 오류가 발생하면, 오류 화면 캡쳐와 함께 구체적인 상황을 고객센터로 알려주시면 신속하게 해결해드리겠습니다.'
        WHEN 49 THEN '현재 친구 초대 이벤트는 진행하고 있지 않지만, 앞으로 더 좋은 이벤트를 준비하겠습니다.'
        ELSE '회원 탈퇴 시 보유하고 있던 포인트와 쿠폰은 모두 소멸되며, 복구되지 않습니다.'
    END as answer,
    -- category_id
    CASE
        WHEN @rownum BETWEEN 21 AND 30 THEN 2 -- 파트너
        ELSE 1 -- 일반회원
    END as category_id,
    -- aid
    ELT(FLOOR(1 + RAND() * 10),
        'dagon_admin_001', 'dagon_admin_002', 'dagon_admin_003', 'dagon_admin_004', 'dagon_admin_005',
        'dagon_admin_006', 'dagon_admin_007', 'dagon_admin_008', 'dagon_admin_009', 'dagon_admin_010'
    ) as aid,
    true as is_active,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY) as created_at
FROM (
    SELECT 1 as n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
) numbers;

-- 20. 이벤트 데이터 생성 (5개)
INSERT INTO event (title, content, start_at, end_at, aid, created_at)
SELECT
    CONCAT('이벤트 제목 ', @rownum) as title,
    CONCAT('이벤트 내용 ', @rownum) as content,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as start_at,
    DATE_ADD(NOW(), INTERVAL @rownum DAY) as end_at,
    'dagon_admin_001' as aid,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) a, (SELECT @rownum := 0) r
) numbers
LIMIT 5;

-- 21. 신고 데이터 생성 (10개)
INSERT INTO user_reports (reporter_id, reported_id, target_type, target_id, reason, created_at)
SELECT 
    u1.uno as reporter_id,
    u2.uno as reported_id,
    'FISHING_POST' as target_type,
    @rownum as target_id,
    CONCAT('신고 사유 ', @rownum) as reason,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 10
) u1
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 10
) u2
WHERE u1.uno != u2.uno
LIMIT 10;

-- 22. 질문 카테고리 데이터 생성 (3개)
INSERT INTO question_category (category_type, qc_name) VALUES
('USER', '일반회원 문의'),
('PARTNER', '파트너 문의'),
('ADMIN', '관리자 문의');

-- 23. 질문 데이터 생성 (10개)
INSERT INTO question (question_title, question_content, question_type, u_type, qc_id, created_at)
SELECT 
    CONCAT('질문 제목 ', @rownum) as question_title,
    CONCAT('질문 내용 ', @rownum) as question_content,
    'PRODUCT' as question_type,
    'USER' as u_type,
    1 as qc_id,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
LIMIT 10;

-- 24. 질문 답변 데이터 생성 (10개)
INSERT INTO question_answers (qa_content, question_id, created_at)
SELECT 
    CONCAT('질문 답변 ', @rownum) as qa_content,
    q.question_id,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT question_id FROM question LIMIT 10
) q
LIMIT 10;

-- 25. 파트너 신청 데이터 생성 (5개)
SET @rownum := 0;
INSERT INTO partner_applications (uno, pname, ceo_name, p_address, p_info, license, p_status, business_license_image, created_at)
SELECT 
    u.uno,
    CONCAT('파트너업체 ', @rownum) as pname,
    CONCAT('대표자 ', @rownum) as ceo_name,
    CONCAT('서울시 강남구 테헤란로 ', @rownum, '길') as p_address,
    CONCAT('파트너업체 정보 ', @rownum) as p_info,
    CONCAT('사업자등록번호: 123-45-6789', @rownum) as license,
    'PENDING' as p_status,
    CONCAT('license', @rownum, '.jpg') as business_license_image,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 5
) u
LIMIT 5;

-- 27. 상품-어구 매핑 데이터 생성 (10개)
INSERT INTO prod_fish_species_mapping (prod_id, fs_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 1), (7, 2), (8, 3), (9, 4), (10, 5);

-- 28. 상품-어구 매핑 데이터 생성 (10개)
INSERT INTO prod_fishing_gear_mapping (prod_id, fg_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 1), (7, 2), (8, 3), (9, 4), (10, 5);

-- 29. 상품-시설 매핑 데이터 생성 (10개)
INSERT INTO prod_facility_mapping (prod_id, fa_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 1), (7, 2), (8, 3), (9, 4), (10, 5);

-- WaveStation 데이터 추가 (바다누리 실시간 파고 데이터 기준)
INSERT INTO wave_station (station_code, station_name, latitude, longitude) VALUES
('TW_0088', '강전항', 35.052, 129.003),
('TW_0089', '경포진해수욕장', 37.808, 128.931),
('TW_0095', '고래불해수욕장', 36.58, 129.454),
('DT_0042', '교본소', 34.704, 128.306),
('TW_0091', '낙산해수욕장', 38.122, 128.65),
('KG_0025', '남애리', 34.222, 128.419),
('TW_0069', '대천해수욕장', 36.274, 126.457),
('KG_0024', '대한항', 34.919, 129.121),
('TW_0094', '망상해수욕장', 37.616, 129.103),
('TW_0079', '상왕등도', 35.652, 126.194),
('TW_0081', '생일도', 34.258, 126.96),
('TW_0083', '속초해수욕장', 38.198, 128.631),
('TW_0093', '송지호해수욕장', 38.198, 128.631),
('TW_0090', '송정해수욕장', 35.164, 129.194),
('IE_0061', '신안가거초', 33.941, 124.592),
('IE_0062', '신안소청초', 37.423, 124.738),
('DT_0039', '등외초', 36.719, 129.732),
('TW_0080', '우이도', 34.543, 125.802),
('KG_0101', '울릉도북동', 38.007, 131.552),
('KG_0102', '울릉도북서', 37.742, 130.601),
('IE_0060', '이어도', 32.122, 125.182),
('TW_0092', '임랑해수욕장', 35.302, 129.292),
('KG_0028', '제주협재', 33.7, 126.59),
('TW_0075', '중문색달해수욕장', 33.234, 126.409),
('TW_0062', '해운대해수욕장', 35.148, 129.17);

-- TideStation 데이터 추가 (바다누리 조위관측소 정보 기준)
INSERT INTO tide_station (station_code, station_name, region, latitude, longitude, wave_station_code) VALUES
-- GYEONGGI / INCHEON
('DT_0032', '강화대교', 'INCHEON', 37.731, 126.522, null), -- 서해안은 파고 관측소 부족
('DT_0008', '안산', 'GYEONGGI', 37.192, 126.647, null),
('DT_0044', '영종대교', 'INCHEON', 37.545, 126.584, null),
('DT_0043', '영흥도', 'INCHEON', 37.238, 126.428, null),
('DT_0002', '평택', 'GYEONGGI', 36.966, 126.822, null),
('DT_0001', '인천', 'INCHEON', 37.451, 126.592, null),
('DT_0052', '인천송도', 'INCHEON', 37.338, 126.586, null),
('DT_0065', '덕적도', 'INCHEON', 37.226, 126.156, 'IE_0062'),
('DT_0093', '소무의도', 'INCHEON', 37.373, 126.44, null),
('IE_0062', '옹진소청초', 'INCHEON', 37.423, 124.738, 'IE_0062'),

-- GANGWON
('DT_0057', '동해항', 'GANGWON', 37.494, 129.143, 'TW_0094'),
('DT_0006', '묵호', 'GANGWON', 37.55, 129.116, 'TW_0094'),
('DT_0012', '속초', 'GANGWON', 38.207, 128.594, 'TW_0083'),

-- CHUNGNAM
('DT_0017', '대산', 'CHUNGNAM', 37.007, 126.352, 'TW_0069'),
('DT_0025', '보령', 'CHUNGNAM', 36.406, 126.486, 'TW_0069'),
('DT_0051', '서천마량', 'CHUNGNAM', 36.128, 126.495, 'TW_0069'),
('DT_0067', '안흥', 'CHUNGNAM', 36.674, 126.129, 'TW_0069'),
('DT_0024', '장항', 'CHUNGNAM', 36.006, 126.687, 'TW_0069'),
('DT_0050', '태안', 'CHUNGNAM', 36.913, 126.238, 'TW_0069'),

-- JEONBUK
('DT_0018', '군산', 'JEONBUK', 35.975, 126.563, 'TW_0079'),
('DT_0037', '어청도', 'JEONBUK', 36.117, 125.984, 'TW_0079'),
('DT_0068', '위도', 'JEONBUK', 35.618, 126.301, 'TW_0079'),
('DT_0066', '향화도', 'JEONBUK', 35.167, 126.359, 'TW_0079'),

-- JEONNAM
('DT_0031', '거문도', 'JEONNAM', 34.028, 127.308, 'TW_0081'),
('DT_0026', '고흥발포', 'JEONNAM', 34.481, 127.342, 'TW_0081'),
('DT_0049', '광양', 'JEONNAM', 34.903, 127.754, 'KG_0025'),
('DT_0007', '목포', 'JEONNAM', 34.779, 126.375, 'TW_0080'),
('DT_0094', '서거차도', 'JEONNAM', 34.251, 125.915, 'TW_0080'),
('DT_0016', '여수', 'JEONNAM', 34.747, 127.765, 'KG_0025'),
('DT_0092', '여호항', 'JEONNAM', 34.661, 127.469, 'TW_0081'),
('DT_0003', '영광', 'JEONNAM', 35.426, 126.42, 'TW_0079'),
('IE_0061', '신안가거초', 'JEONNAM', 33.941, 124.592, 'IE_0061'),
('DT_0027', '완도', 'JEONNAM', 34.315, 126.759, 'TW_0081'),
('DT_0028', '진도', 'JEONNAM', 34.377, 126.308, 'TW_0080'),
('DT_0035', '흑산도', 'JEONNAM', 34.684, 125.435, 'TW_0080'),

-- GYEONGBUK
('DT_0039', '왕돌초', 'GYEONGBUK', 36.719, 129.732, 'DT_0039'),
('DT_0013', '울릉도', 'GYEONGBUK', 37.491, 130.913, 'KG_0102'),
('DT_0091', '포항', 'GYEONGBUK', 36.051, 129.376, 'TW_0095'),
('DT_0902', '포항시청_냉천항만교(수위)', 'GYEONGBUK', 36.003, 129.413, 'TW_0095'),
('DT_0011', '후포', 'GYEONGBUK', 36.677, 129.453, 'TW_0095'),

-- GYEONGNAM
('DT_0029', '거제도', 'GYEONGNAM', 34.801, 128.699, 'DT_0042'),
('DT_0042', '교본초', 'GYEONGNAM', 34.704, 128.306, 'DT_0042'),
('DT_0062', '마산', 'GYEONGNAM', 35.197, 128.576, 'DT_0042'),
('DT_0061', '삼천포', 'GYEONGNAM', 34.924, 128.069, 'DT_0042'),
('DT_0014', '통영', 'GYEONGNAM', 34.827, 128.434, 'DT_0042'),

-- BUSAN
('DT_0063', '가덕도', 'BUSAN', 35.024, 128.81, 'TW_0088'),
('DT_0005', '부산', 'BUSAN', 35.096, 129.035, 'TW_0062'),
('DT_0056', '부산항신항', 'BUSAN', 35.077, 128.784, 'TW_0088'),

-- ULSAN
('DT_0020', '울산', 'ULSAN', 35.501, 129.387, 'TW_0092'),

-- JEJU
('DT_0023', '모슬포', 'JEJU', 33.214, 126.251, 'TW_0075'),
('DT_0010', '서귀포', 'JEJU', 33.24, 126.561, 'TW_0075'),
('DT_0022', '성산포', 'JEJU', 33.474, 126.927, 'TW_0075'),
('IE_0060', '이어도', 'JEJU', 32.122, 125.182, 'IE_0060'),
('DT_0004', '제주', 'JEJU', 33.527, 126.543, 'KG_0028'),
('DT_0021', '추자도', 'JEJU', 33.961, 126.3, 'KG_0028');

-- partners 랜덤 정보로 일괄 업데이트 프로시저
DELIMITER //
CREATE PROCEDURE generate_randomize_partners_all()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE v_uno BIGINT;
  DECLARE cur CURSOR FOR SELECT uno FROM partners;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN cur;
  read_loop: LOOP
    FETCH cur INTO v_uno;
    IF done THEN
      LEAVE read_loop;
    END IF;
    UPDATE partners SET
      ceo_name = ELT(FLOOR(1 + (RAND() * 10)),
        '김민수', '이지은', '박지훈', '최유리', '정우성', '한지민', '오세훈', '이준호', '박소연', '정현우'),
      license = CONCAT('L', LPAD(v_uno, 9, '0')),
      p_address = CONCAT(
        ELT(FLOOR(1 + (RAND() * 10)),
          '서울특별시 강남구 테헤란로 ', '부산광역시 해운대구 해운대로 ', '인천광역시 연수구 송도과학로 ',
          '대구광역시 수성구 동대구로 ', '광주광역시 북구 무등로 ', '강원도 강릉시 경강로 ',
          '제주특별자치도 제주시 첨단로 ', '경상북도 포항시 북구 영일대해수욕장로 ',
          '전라남도 여수시 돌산읍 돌산로 ', '강원도 춘천시 소양강로 '
        ),
        FLOOR(RAND()*100+1)
      ),
      p_info = CONCAT(
        ELT(FLOOR(1 + (RAND() * 10)),
          '전국 최대 규모의 낚시터', '가족과 함께 즐기는 체험', '초보자도 환영하는 친절한 운영',
          '최신 장비 완비', '자연과 함께하는 힐링', '도심 속 레저', '전문 가이드 동행',
          '다양한 어종 포획 가능', '연중무휴 운영', '프리미엄 서비스 제공'
        ),
        ' - ', v_uno
      ),
      pname = CONCAT(
        ELT(FLOOR(1 + (RAND() * 10)),
          '서울한강낚시', '부산해운대피싱', '인천송도낚시', '대구수성피싱', '광주무등산낚시',
          '강릉경포낚시', '제주바다피싱', '포항영일대낚시', '여수돌산낚시', '춘천소양강낚시'
        ),
        ' ',
        ELT(FLOOR(1 + (RAND() * 5)), '레저', '체험', '투어', '마스터', '월드'),
        v_uno
      ),
      version = FLOOR(RAND()*10)
    WHERE uno = v_uno;
  END LOOP;
  CLOSE cur;
END //
DELIMITER ;

