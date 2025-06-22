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
    CONCAT(
        CASE 
            WHEN @rownum <= 200 THEN '해운대 방파제 낚시'
            WHEN @rownum <= 400 THEN '청평호수 민물낚시'
            WHEN @rownum <= 600 THEN '제주 해상낚시 투어'
            WHEN @rownum <= 800 THEN '한강 강낚시 체험'
            ELSE '화천호수 호수낚시'
        END,
        ' #', @rownum
    ) as prod_name,
    CASE 
        WHEN @rownum <= 200 THEN 'BUSAN'
        WHEN @rownum <= 400 THEN 'GYEONGGI'
        WHEN @rownum <= 600 THEN 'JEJU'
        WHEN @rownum <= 800 THEN 'SEOUL'
        ELSE 'GANGWON'
    END as prod_region,
    CASE 
        WHEN @rownum <= 200 THEN 'SEA'
        WHEN @rownum <= 400 THEN 'FRESHWATER'
        WHEN @rownum <= 600 THEN 'SEA'
        WHEN @rownum <= 800 THEN 'FRESHWATER'
        ELSE 'FRESHWATER'
    END as main_type,
    CASE 
        WHEN @rownum <= 200 THEN 'BREAK_WATER'
        WHEN @rownum <= 400 THEN 'RESERVOIR'
        WHEN @rownum <= 600 THEN 'BOAT'
        WHEN @rownum <= 800 THEN 'RIVER'
        ELSE 'RESERVOIR'
    END as sub_type,
    FLOOR(RAND() * 10) + 5 as max_person,
    FLOOR(RAND() * 3) + 1 as min_person,
    0.00 as weight,
    CONCAT(
        CASE 
            WHEN @rownum <= 200 THEN '부산광역시 해운대구 해운대해변로 '
            WHEN @rownum <= 400 THEN '경기도 가평군 청평면 청평로 '
            WHEN @rownum <= 600 THEN '제주특별자치도 제주시 애월읍 애월로 '
            WHEN @rownum <= 800 THEN '서울특별시 강남구 한강대로 '
            ELSE '강원도 화천군 화천읍 화천로 '
        END,
        @rownum
    ) as prod_address,
    CONCAT(
        CASE 
            WHEN @rownum <= 200 THEN '해운대 방파제에서 즐기는 바다낚시입니다. 도미, 농어, 감성돔 등 다양한 어종을 잡을 수 있습니다.'
            WHEN @rownum <= 400 THEN '청평호수에서 즐기는 민물낚시입니다. 잉어, 붕어, 메기 등 민물고기를 잡을 수 있습니다.'
            WHEN @rownum <= 600 THEN '제주 바다에서 즐기는 해상낚시 투어입니다. 고등어, 삼치, 참치 등 대형 어종을 잡을 수 있습니다.'
            WHEN @rownum <= 800 THEN '한강에서 즐기는 강낚시 체험입니다. 잉어, 붕어, 메기 등 다양한 민물고기를 잡을 수 있습니다.'
            ELSE '화천호수에서 즐기는 호수낚시입니다. 송어, 은어, 잉어 등 맛있는 민물고기를 잡을 수 있습니다.'
        END,
        ' #', @rownum
    ) as prod_description,
    CASE 
        WHEN RAND() > 0.7 THEN '신규 고객 20% 할인'
        WHEN RAND() > 0.5 THEN '주말 패키지 30% 할인'
        WHEN RAND() > 0.3 THEN '평일 할인 이벤트'
        ELSE '겨울 얼음낚시 특별가'
    END as prod_event,
    CASE 
        WHEN RAND() > 0.7 THEN '안전장비 착용 필수, 날씨에 따라 취소될 수 있습니다.'
        WHEN RAND() > 0.5 THEN '낚시도구 대여 가능, 생선회 제공'
        WHEN RAND() > 0.3 THEN '선박 안전교육 필수, 구명조끼 착용'
        ELSE '온수 샤워장 완비, 숙박시설 연계 가능'
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

-- 11. 조황정보 데이터 생성 (100개)
SET @rownum := 0;
INSERT INTO fishing_report (title, content, thumbnail_url, fishing_at, modify_at, views, uid, prod_id, created_at)
SELECT 
    CONCAT(
        CASE 
            WHEN @rownum <= 20 THEN '해운대 방파제 조황정보'
            WHEN @rownum <= 40 THEN '청평호수 조황정보'
            WHEN @rownum <= 60 THEN '제주 해상낚시 조황정보'
            WHEN @rownum <= 80 THEN '한강 조황정보'
            ELSE '화천호수 조황정보'
        END,
        ' ', DATE_FORMAT(DATE_SUB(NOW(), INTERVAL @rownum DAY), '%Y-%m-%d')
    ) as title,
    CONCAT(
        CASE 
            WHEN @rownum <= 20 THEN '해운대 방파제에서 도미, 농어, 고등어가 잘 잡히고 있습니다. 아침 일찍이나 저녁 시간대가 좋습니다.'
            WHEN @rownum <= 40 THEN '청평호수에서 잉어, 붕어, 메기 등 민물고기가 활발하게 활동하고 있습니다. 미끼는 옥수수나 빵이 효과적입니다.'
            WHEN @rownum <= 60 THEN '제주 해상에서 고등어, 전갱이, 갈치 등이 잘 잡히고 있습니다. 선상낚시는 예약 필수입니다.'
            WHEN @rownum <= 80 THEN '한강에서 붕어, 잉어, 메기가 잘 잡히고 있습니다. 도시 한가운데서 즐길 수 있는 낚시입니다.'
            ELSE '화천호수에서 송어, 은어, 잉어가 잘 잡히고 있습니다. 깨끗한 물에서 즐기는 프리미엄 낚시입니다.'
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
    DATE_SUB(NOW(), INTERVAL @rownum DAY) as fishing_at,
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
INSERT INTO fishing_report_comments (comment_content, uid, fr_id, modify_at)
SELECT 
    CONCAT('조황정보 댓글 ', @rownum) as comment_content,
    u.uno as uid,
    fr.fr_id,
    NULL as modify_at
FROM (
    SELECT @rownum := @rownum + 1 AS rownum FROM (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) a, (SELECT @rownum := 0) r
) numbers
CROSS JOIN (
    SELECT uno FROM users WHERE role = 'USER' LIMIT 100
) u
CROSS JOIN (
    SELECT fr_id FROM fishing_report LIMIT 100
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
INSERT INTO event (title, content, event_status, start_at, end_at, aid, created_at)
SELECT 
    CONCAT('이벤트 제목 ', @rownum) as title,
    CONCAT('이벤트 내용 ', @rownum) as content,
    'ONGOING' as event_status,
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

-- 26. FAQ 카테고리 데이터 생성 (3개)
INSERT IGNORE INTO faq_category (name, display_order) VALUES
('일반회원', 0),
('파트너', 1),
('관리자', 2);

-- 27. 상품-어종 매핑 데이터 생성 (10개)
INSERT INTO prod_fish_species_mapping (prod_id, fs_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 1), (7, 2), (8, 3), (9, 4), (10, 5);

-- 28. 상품-어구 매핑 데이터 생성 (10개)
INSERT INTO prod_fishing_gear_mapping (prod_id, fg_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 1), (7, 2), (8, 3), (9, 4), (10, 5);

-- 29. 상품-시설 매핑 데이터 생성 (10개)
INSERT INTO prod_facility_mapping (prod_id, fa_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 1), (7, 2), (8, 3), (9, 4), (10, 5); 