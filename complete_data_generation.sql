-- 완전한 대량 데이터 생성 스크립트
-- 모든 테이블에 샘플 데이터 생성

-- 1. 기존 데이터 삭제 (외래키 순서 고려)
DELETE FROM prod_fish_species_mapping;
DELETE FROM prod_fishing_gear_mapping;
DELETE FROM prod_facility_mapping;
DELETE FROM fishing_report_comments;
DELETE FROM fishing_report_image;
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

-- 3. 관리자 데이터 생성 (10개)
INSERT INTO admin (aid, apw, aname, role, uno) VALUES
('admin', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '슈퍼관리자', 'SUPER_ADMIN', 1),
('dagon_admin_001', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '김관리자', 'ADMIN', 2),
('dagon_admin_002', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '이관리자', 'ADMIN', 3),
('dagon_super_admin', '$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO', '슈퍼관리자', 'SUPER_ADMIN', 4),
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

-- 5. 파트너 데이터 생성 (100개) - users의 uno와 매칭
SET @rownum := 0;
INSERT INTO partners (uno, version, pname, p_address, ceo_name, p_info, license, license_img, created_at)
SELECT 
    u.uno,
    0 as version,
    CONCAT(
        CASE 
            WHEN @rownum <= 20 THEN '바다낚시터'
            WHEN @rownum <= 40 THEN '민물낚시터'
            WHEN @rownum <= 60 THEN '해상낚시터'
            WHEN @rownum <= 80 THEN '강낚시터'
            ELSE '호수낚시터'
        END,
        ' ',
        CASE 
            WHEN @rownum <= 20 THEN '해운대점'
            WHEN @rownum <= 40 THEN '청평점'
            WHEN @rownum <= 60 THEN '제주점'
            WHEN @rownum <= 80 THEN '한강점'
            ELSE '화천점'
        END,
        ' #', @rownum
    ) as pname,
    CONCAT(
        CASE 
            WHEN @rownum <= 20 THEN '부산광역시 해운대구 해운대해변로 '
            WHEN @rownum <= 40 THEN '경기도 가평군 청평면 청평로 '
            WHEN @rownum <= 60 THEN '제주특별자치도 제주시 애월읍 애월로 '
            WHEN @rownum <= 80 THEN '서울특별시 강남구 한강대로 '
            ELSE '강원도 화천군 화천읍 화천로 '
        END,
        @rownum
    ) as p_address,
    CONCAT(
        CASE 
            WHEN @rownum <= 20 THEN '김바다'
            WHEN @rownum <= 40 THEN '이민물'
            WHEN @rownum <= 60 THEN '박해상'
            WHEN @rownum <= 80 THEN '최강낚시'
            ELSE '정호수'
        END,
        @rownum
    ) as ceo_name,
    CONCAT(
        CASE 
            WHEN @rownum <= 20 THEN '해운대에서 즐기는 바다낚시! 맛있는 회와 함께하는 즐거운 낚시체험을 제공합니다.'
            WHEN @rownum <= 40 THEN '청평호수에서 즐기는 민물낚시! 아름다운 자연과 함께하는 평화로운 낚시를 경험하세요.'
            WHEN @rownum <= 60 THEN '제주 바다에서 즐기는 해상낚시! 깨끗한 바다와 함께하는 특별한 낚시체험을 제공합니다.'
            WHEN @rownum <= 80 THEN '한강에서 즐기는 강낚시! 도시 한가운데서 즐기는 편안한 낚시를 경험하세요.'
            ELSE '화천호수에서 즐기는 호수낚시! 맑은 물과 함께하는 건강한 낚시를 즐기세요.'
        END,
        ' #', @rownum
    ) as p_info,
    CONCAT('L', LPAD(@rownum, 9, '0')) as license,
    CONCAT('license', @rownum, '.jpg') as license_img,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum, u.* FROM (SELECT * FROM users WHERE role = 'PARTNER' ORDER BY uno LIMIT 100) u
) u;

-- 6. 상품 데이터 생성 (1000개)
SET @rownum := 0;
INSERT INTO product (prod_name, prod_region, main_type, sub_type, max_person, min_person, weight, prod_address, prod_description, prod_event, prod_notice, uno, deleted, available_date, prod_thumbnail, created_at)
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
    -- prod_address
    CASE FLOOR(RAND() * 10)
        WHEN 0 THEN '부산광역시 해운대구 해운대해변로'
        WHEN 1 THEN '부산광역시 수영구 광안해변로'
        WHEN 2 THEN '부산광역시 해운대구 송정해변로'
        WHEN 3 THEN '부산광역시 사하구 다대포해변로'
        WHEN 4 THEN '부산광역시 기장군 기장해안로'
        WHEN 5 THEN '부산광역시 영도구 태종로'
        WHEN 6 THEN '부산광역시 남구 오륙도로'
        WHEN 7 THEN '부산광역시 영도구 절영로'
        WHEN 8 THEN '부산광역시 서구 송도해변로'
        ELSE '부산광역시 수영구 민락수변로'
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
INSERT INTO reservation (uid, prod_id, opt_id, num_person, fishing_at, paid_at, reservation_status, payment_method, created_at)
SELECT 
    u.uno as uid,
    p.prod_id,
    o.opt_id,
    FLOOR(RAND() * 5) + 1 as num_person,
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
    SELECT prod_id FROM product LIMIT 100
) p
CROSS JOIN (
    SELECT opt_id, prod_id FROM prod_option LIMIT 200
) o
WHERE o.prod_id = p.prod_id
LIMIT 200;

-- 9. 조행기 데이터 생성 (500개)
SET @rownum := 0;
INSERT INTO fishing_diary (title, content, thumbnail_url, fishing_at, modify_at, views, uid, prod_id, created_at)
SELECT 
    CONCAT(
        CASE 
            WHEN @rownum <= 100 THEN '해운대에서 잡은 도미'
            WHEN @rownum <= 200 THEN '청평호수 잉어낚시'
            WHEN @rownum <= 300 THEN '제주 해상낚시 투어'
            WHEN @rownum <= 400 THEN '한강 붕어낚시'
            ELSE '화천호수 송어낚시'
        END,
        ' #', @rownum
    ) as title,
    CONCAT(
        CASE 
            WHEN @rownum <= 100 THEN '오늘 해운대 방파제에서 도미를 잡았습니다! 날씨도 좋고 물도 맑아서 정말 즐거운 낚시였어요. 도미는 3마리 잡았는데 모두 30cm 이상이었습니다.'
            WHEN @rownum <= 200 THEN '청평호수에서 잉어를 잡았습니다. 2kg짜리 잉어를 잡아서 정말 기뻤어요. 민물낚시의 묘미를 제대로 느낄 수 있었습니다.'
            WHEN @rownum <= 300 THEN '제주 바다에서 고등어를 잡았습니다! 선상낚시는 처음이었는데 정말 재미있었어요. 바다의 넓이를 느끼며 낚시하는 기분이 정말 좋았습니다.'
            WHEN @rownum <= 400 THEN '한강에서 붕어를 잡았습니다. 도시 한가운데서 낚시하는 것도 나쁘지 않네요. 붕어는 5마리 잡았는데 모두 맛있게 먹었습니다.'
            ELSE '화천호수에서 송어를 잡았습니다! 송어는 민물고기 중에서도 가장 맛있는 고기 중 하나죠. 1.5kg짜리 송어를 잡아서 정말 만족스러웠습니다.'
        END,
        ' #', @rownum
    ) as content,
    CONCAT(
        '/uploads/',
        DATE_FORMAT(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), '%Y/%m/%d'),
        '/fishing_diary_',
        CASE 
            WHEN @rownum <= 100 THEN 'haewoondae'
            WHEN @rownum <= 200 THEN 'cheongpyeong'
            WHEN @rownum <= 300 THEN 'jeju'
            WHEN @rownum <= 400 THEN 'hangang'
            ELSE 'hwacheon'
        END,
        '_', @rownum, '.jpg'
    ) as thumbnail_url,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY) as fishing_at,
    NULL as modify_at,
    FLOOR(RAND() * 100) + 10 as views,
    u.uno as uid,
    p.prod_id,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY) as created_at
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

-- 11-1. 조황정보 이미지 데이터 생성 (각 조황정보별 1~3장 랜덤)
INSERT INTO fishing_report_image (image_url, is_thumbnail, order_index, fr_id)
SELECT
    CONCAT(
        '/uploads/',
        DATE_FORMAT(DATE_SUB(NOW(), INTERVAL fr.fr_id DAY), '%Y/%m/%d'),
        '/fishing_report_',
        CASE 
            WHEN fr.fr_id <= 20 THEN 'haewoondae'
            WHEN fr.fr_id <= 40 THEN 'cheongpyeong'
            WHEN fr.fr_id <= 60 THEN 'jeju'
            WHEN fr.fr_id <= 80 THEN 'hangang'
            ELSE 'hwacheon'
        END,
        '_', fr.fr_id, '_', img_idx.n, '.jpg'
    ) as image_url,
    (img_idx.n = 1) as is_thumbnail,
    (img_idx.n - 1) as order_index,
    fr.fr_id
FROM (
    SELECT fr_id, FLOOR(1 + RAND(fr_id) * 3) AS img_count
    FROM fishing_report
) fr
JOIN (
    SELECT 1 as n UNION ALL SELECT 2 UNION ALL SELECT 3
) img_idx
ON img_idx.n <= fr.img_count;

-- 11. 조황정보 데이터 생성 (100개)
SET @rownum := 0;
INSERT INTO fishing_report (title, content, thumbnail_url, fishing_at, modify_at, views, uid, prod_id, created_at)
SELECT 
    CONCAT(
        CASE 
            WHEN @rownum <= 20 THEN 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '🐟 해운대 방파제 대박 조황! 도미 폭탄!'
                    WHEN @rownum % 4 = 2 THEN '🌊 해운대에서 농어가 미쳤어요!'
                    WHEN @rownum % 4 = 3 THEN '🎣 해운대 방파제 오늘은 도미의 날!'
                    ELSE '⚡ 해운대에서 고등어 대량 출현!'
                END
            WHEN @rownum <= 40 THEN 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '🐠 청평호수 잉어 대폭발! 2kg급 연속!'
                    WHEN @rownum % 4 = 2 THEN '🌿 청평에서 붕어가 미쳤어요!'
                    WHEN @rownum % 4 = 3 THEN '🎯 청평호수 메기 잡기 좋은 날!'
                    ELSE '💎 청평에서 은어 대박 조황!'
                END
            WHEN @rownum <= 60 THEN 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '🦈 제주 해상에서 참치 출현!'
                    WHEN @rownum % 4 = 2 THEN '🐟 제주 고등어 대량 어획!'
                    WHEN @rownum % 4 = 3 THEN '🌊 제주 삼치 폭탄 조황!'
                    ELSE '⚡ 제주 해상 전갱이 대박!'
                END
            WHEN @rownum <= 80 THEN 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '🐠 한강 붕어 대폭발! 도시 낚시의 정석!'
                    WHEN @rownum % 4 = 2 THEN '🌊 한강 잉어 미쳤어요!'
                    WHEN @rownum % 4 = 3 THEN '🎣 한강 메기 잡기 좋은 날!'
                    ELSE '💎 한강에서 은어 대박!'
                END
            ELSE 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '🐟 화천호수 송어 대폭발! 1.5kg급 연속!'
                    WHEN @rownum % 4 = 2 THEN '🌿 화천에서 은어가 미쳤어요!'
                    WHEN @rownum % 4 = 3 THEN '🎯 화천호수 잉어 잡기 좋은 날!'
                    ELSE '💎 화천에서 송어 대박 조황!'
                END
        END,
        ' ', DATE_FORMAT(DATE_SUB(NOW(), INTERVAL @rownum DAY), '%Y-%m-%d')
    ) as title,
    CONCAT(
        CASE 
            WHEN @rownum <= 20 THEN 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '해운대 방파제에서 도미가 정말 미쳤어요! 오늘 아침부터 도미 5마리 연속으로 잡았습니다. 30cm 이상 대물들이 줄줄이 걸려서 정말 신났어요! 🎣'
                    WHEN @rownum % 4 = 2 THEN '해운대에서 농어가 대박이에요! 저녁 시간대에 농어가 활발하게 활동하고 있어서 연속으로 잡았습니다. 미끼는 오징어가 최고! 🐟'
                    WHEN @rownum % 4 = 3 THEN '해운대 방파제 오늘은 도미의 날이었어요! 아침 일찍 가서 도미 3마리 잡았는데 모두 25cm 이상이었습니다. 날씨도 완벽했어요! 🌊'
                    ELSE '해운대에서 고등어가 대량으로 출현하고 있어요! 오후 시간대에 고등어가 연속으로 걸려서 정말 즐거웠습니다. 회로 먹기 딱 좋은 사이즈! ⚡'
                END
            WHEN @rownum <= 40 THEN 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '청평호수에서 잉어가 정말 미쳤어요! 2kg급 잉어를 연속으로 잡아서 정말 신났습니다. 민물낚시의 묘미를 제대로 느낄 수 있었어요! 🐠'
                    WHEN @rownum % 4 = 2 THEN '청평에서 붕어가 대박이에요! 오전 시간대에 붕어가 활발하게 활동하고 있어서 연속으로 잡았습니다. 미끼는 옥수수가 최고! 🌿'
                    WHEN @rownum % 4 = 3 THEN '청평호수 메기 잡기 정말 좋은 날이었어요! 저녁 시간대에 메기가 연속으로 걸려서 정말 즐거웠습니다. 민물고기의 맛을 제대로 느낄 수 있었어요! 🎯'
                    ELSE '청평에서 은어가 대박이에요! 오후 시간대에 은어가 활발하게 활동하고 있어서 연속으로 잡았습니다. 회로 먹기 딱 좋은 사이즈! 💎'
                END
            WHEN @rownum <= 60 THEN 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '제주 해상에서 참치가 출현했어요! 선상낚시 중에 참치가 걸려서 정말 신났습니다. 대형 어종을 잡는 기분이 정말 특별했어요! 🦈'
                    WHEN @rownum % 4 = 2 THEN '제주에서 고등어가 대량으로 어획되고 있어요! 오전 시간대에 고등어가 연속으로 걸려서 정말 즐거웠습니다. 바다의 넓이를 느끼며 낚시하는 기분이 정말 좋았어요! 🐟'
                    WHEN @rownum % 4 = 3 THEN '제주 해상 삼치 폭탄 조황이에요! 오후 시간대에 삼치가 활발하게 활동하고 있어서 연속으로 잡았습니다. 선상낚시의 묘미를 제대로 느낄 수 있었어요! 🌊'
                    ELSE '제주 해상에서 전갱이가 대박이에요! 저녁 시간대에 전갱이가 연속으로 걸려서 정말 신났습니다. 바다낚시의 재미를 제대로 느낄 수 있었어요! ⚡'
                END
            WHEN @rownum <= 80 THEN 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '한강에서 붕어가 정말 미쳤어요! 도시 한가운데서 붕어를 연속으로 잡아서 정말 신났습니다. 도시 낚시의 정석을 제대로 느낄 수 있었어요! 🐠'
                    WHEN @rownum % 4 = 2 THEN '한강 잉어가 대박이에요! 오전 시간대에 잉어가 활발하게 활동하고 있어서 연속으로 잡았습니다. 도시에서 즐기는 낚시의 묘미를 느낄 수 있었어요! 🌊'
                    WHEN @rownum % 4 = 3 THEN '한강 메기 잡기 정말 좋은 날이었어요! 저녁 시간대에 메기가 연속으로 걸려서 정말 즐거웠습니다. 도시 한가운데서 즐기는 특별한 낚시였어요! 🎣'
                    ELSE '한강에서 은어가 대박이에요! 오후 시간대에 은어가 활발하게 활동하고 있어서 연속으로 잡았습니다. 도시에서 즐기는 프리미엄 낚시였어요! 💎'
                END
            ELSE 
                CASE 
                    WHEN @rownum % 4 = 1 THEN '화천호수에서 송어가 정말 미쳤어요! 1.5kg급 송어를 연속으로 잡아서 정말 신났습니다. 민물고기 중 최고의 맛을 느낄 수 있었어요! 🐟'
                    WHEN @rownum % 4 = 2 THEN '화천에서 은어가 대박이에요! 오전 시간대에 은어가 활발하게 활동하고 있어서 연속으로 잡았습니다. 깨끗한 물에서 즐기는 프리미엄 낚시였어요! 🌿'
                    WHEN @rownum % 4 = 3 THEN '화천호수 잉어 잡기 정말 좋은 날이었어요! 저녁 시간대에 잉어가 연속으로 걸려서 정말 즐거웠습니다. 자연 속에서 즐기는 평화로운 낚시였어요! 🎯'
                    ELSE '화천 송어가 대박이에요! 오후 시간대에 송어가 활발하게 활동하고 있어서 연속으로 잡았습니다. 맑은 물과 함께하는 건강한 낚시였어요! 💎'
                END
        END,
        ' #조황정보 #', @rownum
    ) as content,
    CONCAT(
        '/uploads/',
        DATE_FORMAT(DATE_SUB(NOW(), INTERVAL @rownum DAY), '%Y/%m/%d'),
        '/fishing_report_',
        CASE 
            WHEN @rownum <= 20 THEN 'haewoondae'
            WHEN @rownum <= 40 THEN 'cheongpyeong'
            WHEN @rownum <= 60 THEN 'jeju'
            WHEN @rownum <= 80 THEN 'hangang'
            ELSE 'hwacheon'
        END,
        '_', @rownum, '.jpg'
    ) as thumbnail_url,
    DATE_ADD('2021-01-01', INTERVAL FLOOR(RAND() * DATEDIFF(NOW(), '2021-01-01')) DAY) as fishing_at,
    NULL as modify_at,
    FLOOR(RAND() * 100) + 10 as views,
    u.uno as uid,
    p.prod_id,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 100
) u
CROSS JOIN (
    SELECT prod_id FROM product LIMIT 100
) p
LIMIT 100;

-- 12. 조황정보 댓글 데이터 생성 (200개)
INSERT INTO fishing_report_comments (comment_content, uid, fr_id, created_at, modify_at)
SELECT 
    CONCAT(
        CASE 
            WHEN @rownum % 20 = 1 THEN '와! 정말 대박이네요! 저도 다음에 가보고 싶어요! 🎣'
            WHEN @rownum % 20 = 2 THEN '도미 5마리라니... 정말 부럽습니다! 축하해요! 🐟'
            WHEN @rownum % 20 = 3 THEN '해운대 조황 정말 좋네요! 저도 이번 주말에 가볼게요! 🌊'
            WHEN @rownum % 20 = 4 THEN '농어가 미쳤다니... 정말 신기해요! 미끼는 뭘 썼나요? 🎯'
            WHEN @rownum % 20 = 5 THEN '고등어 대량 출현이라니! 회로 먹기 딱 좋겠네요! ⚡'
            WHEN @rownum % 20 = 6 THEN '잉어 2kg급이라니... 정말 대단해요! 민물낚시의 묘미를 느낄 수 있겠네요! 🐠'
            WHEN @rownum % 20 = 7 THEN '청평 붕어가 미쳤다니... 옥수수 미끼가 효과적이었나요? 🌿'
            WHEN @rownum % 20 = 8 THEN '메기 잡기 좋은 날이라니... 저녁 시간대가 핵심이었나요? 🎣'
            WHEN @rownum % 20 = 9 THEN '은어 대박 조황이라니... 회로 먹기 딱 좋겠네요! 💎'
            WHEN @rownum % 20 = 10 THEN '제주에서 참치라니... 정말 대박이네요! 선상낚시는 처음이었나요? 🦈'
            WHEN @rownum % 20 = 11 THEN '고등어 대량 어획이라니... 바다의 넓이를 느끼며 낚시하는 기분이 정말 좋았겠네요! 🐟'
            WHEN @rownum % 20 = 12 THEN '삼치 폭탄 조황이라니... 선상낚시의 묘미를 제대로 느낄 수 있었겠네요! 🌊'
            WHEN @rownum % 20 = 13 THEN '전갱이 대박이라니... 바다낚시의 재미를 제대로 느낄 수 있었겠네요! ⚡'
            WHEN @rownum % 20 = 14 THEN '한강 붕어 대폭발이라니... 도시 한가운데서 낚시하는 것도 나쁘지 않네요! 🐠'
            WHEN @rownum % 20 = 15 THEN '한강 잉어가 미쳤다니... 도시에서 즐기는 낚시의 묘미를 느낄 수 있었겠네요! 🌊'
            WHEN @rownum % 20 = 16 THEN '한강 메기 잡기 좋은 날이라니... 도시 한가운데서 즐기는 특별한 낚시였겠네요! 🎣'
            WHEN @rownum % 20 = 17 THEN '화천 송어 1.5kg급이라니... 민물고기 중 최고의 맛을 느낄 수 있었겠네요! 🐟'
            WHEN @rownum % 20 = 18 THEN '화천 은어가 미쳤다니... 깨끗한 물에서 즐기는 프리미엄 낚시였겠네요! 🌿'
            WHEN @rownum % 20 = 19 THEN '화천 잉어 잡기 좋은 날이라니... 자연 속에서 즐기는 평화로운 낚시였겠네요! 🎯'
            ELSE '화천 송어 대박 조황이라니... 맑은 물과 함께하는 건강한 낚시였겠네요! 💎'
        END,
        ' #', @rownum
    ) as comment_content,
    u.uno as uid,
    fr.fr_id,
    DATE_ADD('2021-01-01', INTERVAL FLOOR(RAND() * DATEDIFF(NOW(), '2021-01-01')) DAY) as created_at,
    DATE_ADD('2021-01-01', INTERVAL FLOOR(RAND() * DATEDIFF(NOW(), '2021-01-01')) DAY) as modify_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' ORDER BY RAND() LIMIT 200
) u
CROSS JOIN (
    SELECT fr_id FROM fishing_report ORDER BY RAND() LIMIT 200
) fr
LIMIT 200;

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

-- 19. FAQ 데이터 생성 (10개)
INSERT INTO faq (question, answer, category_id, aid, is_active, created_at)
SELECT 
    CONCAT('FAQ 질문 ', @rownum) as question,
    CONCAT('FAQ 답변 ', @rownum) as answer,
    1 as category_id,
    'dagon_admin_001' as aid,
    true as is_active,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
LIMIT 10;

-- 20. 이벤트 데이터 생성 (5개)
INSERT INTO event (title, content, start_at, end_at, aid, created_at)
SELECT 
    CONCAT('이벤트 제목 ', @rownum) as title,
    CONCAT('이벤트 내용 ', @rownum) as content,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as start_at,
    DATE_ADD(NOW(), INTERVAL @rownum DAY) as end_at,
    'dagon_admin_001' as aid,
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as created_at
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

-- 27. 상품-어종 매핑 데이터 생성 (10개)
INSERT INTO prod_fish_species_mapping (prod_id, fs_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 1), (7, 2), (8, 3), (9, 4), (10, 5);

-- 28. 상품-어구 매핑 데이터 생성 (10개)
INSERT INTO prod_fishing_gear_mapping (prod_id, fg_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 1), (7, 2), (8, 3), (9, 4), (10, 5);

-- 29. 상품-시설 매핑 데이터 생성 (10개)
INSERT INTO prod_facility_mapping (prod_id, fa_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 1), (7, 2), (8, 3), (9, 4), (10, 5); 