-- 기존 데이터 삭제 및 샘플 데이터 재생성
-- 외래키 제약조건을 고려한 순서로 삭제

-- 1. 기존 데이터 삭제 (외래키 순서 고려 - 자식 테이블부터 삭제)
DELETE FROM fishing_report_image;
DELETE FROM fishing_diary_image;
DELETE FROM fishing_report_comments;
DELETE FROM fishing_diary_comment;
DELETE FROM free_board_comments;
DELETE FROM question_answers;
DELETE FROM user_actions;
DELETE FROM notification;
DELETE FROM reservation;
DELETE FROM partner_applications;
DELETE FROM inquiries;
DELETE FROM fishing_report;
DELETE FROM fishing_diary;
DELETE FROM free_board;
DELETE FROM user_reports;
DELETE FROM question;
DELETE FROM notice;
DELETE FROM faq;
-- DELETE FROM faq_category;
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
-- 3. 샘플 데이터 생성 (외래키 순서 고려)

-- 3-1. 사용자 데이터 (250명)
INSERT INTO users (uid, upw, uname, nickname, email, profile_img, phone, points, level, level_point, last_login_at, role, login_type, is_active, version, created_at) VALUES
('user001', '$2a$10$encrypted_password_hash', '김철수', '낚시왕', 'user001@example.com', 'profile1.jpg', '010-1234-5678', 1500, 'GOLD', 1500, NOW(), 'USER', 'LOCAL', true, 0, NOW()),
('user002', '$2a$10$encrypted_password_hash', '이영희', '물고기사랑', 'user002@example.com', 'profile2.jpg', '010-2345-6789', 800, 'SILVER', 800, NOW(), 'USER', 'LOCAL', true, 0, NOW()),
('user003', '$2a$10$encrypted_password_hash', '박민수', '바다낚시러', 'user003@example.com', 'profile3.jpg', '010-3456-7890', 2500, 'PLATINUM', 2500, NOW(), 'USER', 'LOCAL', true, 0, NOW()),
('user004', '$2a$10$encrypted_password_hash', '최지영', '민물낚시', 'user004@example.com', 'profile4.jpg', '010-4567-8901', 500, 'SILVER', 500, NOW(), 'USER', 'LOCAL', true, 0, NOW()),
('user005', '$2a$10$encrypted_password_hash', '정현우', '낚시초보', 'user005@example.com', 'profile5.jpg', '010-5678-9012', 300, 'SILVER', 300, NOW(), 'USER', 'LOCAL', true, 0, NOW()),
('user006', '$2a$10$encrypted_password_hash', '강미영', '바다여신', 'user006@example.com', 'profile6.jpg', '010-6789-0123', 1800, 'GOLD', 1800, NOW(), 'USER', 'LOCAL', true, 0, NOW()),
('user007', '$2a$10$encrypted_password_hash', '윤성준', '낚시고수', 'user007@example.com', 'profile7.jpg', '010-7890-1234', 3500, 'DIAMOND', 3500, NOW(), 'USER', 'LOCAL', true, 0, NOW()),
('user008', '$2a$10$encrypted_password_hash', '임수진', '물고기친구', 'user008@example.com', 'profile8.jpg', '010-8901-2345', 1200, 'GOLD', 1200, NOW(), 'USER', 'LOCAL', true, 0, NOW()),
('user009', '$2a$10$encrypted_password_hash', '한동현', '낚시마스터', 'user009@example.com', 'profile9.jpg', '010-9012-3456', 2800, 'PLATINUM', 2800, NOW(), 'USER', 'LOCAL', true, 0, NOW()),
('user010', '$2a$10$encrypted_password_hash', '송은지', '바다낚시여신', 'user010@example.com', 'profile10.jpg', '010-0123-4567', 900, 'SILVER', 900, NOW(), 'USER', 'LOCAL', true, 0, NOW());

-- 파트너 사용자들 (50명)
INSERT INTO users (uid, upw, uname, nickname, email, profile_img, phone, points, level, level_point, last_login_at, role, login_type, is_active, version, created_at) VALUES
('partner001', '$2a$10$encrypted_password_hash', '김파트너', '바다낚시터', 'partner001@example.com', 'partner1.jpg', '010-1000-1000', 5000, 'DIAMOND', 5000, NOW(), 'PARTNER', 'LOCAL', true, 0, NOW()),
('partner002', '$2a$10$encrypted_password_hash', '이파트너', '민물낚시터', 'partner002@example.com', 'partner2.jpg', '010-2000-2000', 4500, 'PLATINUM', 4500, NOW(), 'PARTNER', 'LOCAL', true, 0, NOW()),
('partner003', '$2a$10$encrypted_password_hash', '박파트너', '해상낚시터', 'partner003@example.com', 'partner3.jpg', '010-3000-3000', 4800, 'PLATINUM', 4800, NOW(), 'PARTNER', 'LOCAL', true, 0, NOW()),
('partner004', '$2a$10$encrypted_password_hash', '최파트너', '강낚시터', 'partner004@example.com', 'partner4.jpg', '010-4000-4000', 4200, 'GOLD', 4200, NOW(), 'PARTNER', 'LOCAL', true, 0, NOW()),
('partner005', '$2a$10$encrypted_password_hash', '정파트너', '호수낚시터', 'partner005@example.com', 'partner5.jpg', '010-5000-5000', 4600, 'PLATINUM', 4600, NOW(), 'PARTNER', 'LOCAL', true, 0, NOW());

-- 3-2. 파트너 데이터 (50개) - users의 uno와 매칭
INSERT INTO partners (uno, version, pname, p_address, ceo_name, p_info, license, license_img, created_at) VALUES
(11, 0, '바다낚시터 해운대점', '부산광역시 해운대구 해운대해변로 264', '김바다', '해운대에서 즐기는 바다낚시! 맛있는 회와 함께하는 즐거운 낚시체험을 제공합니다.', 'L123456789', 'license1.jpg', NOW()),
(12, 0, '민물낚시터 청평점', '경기도 가평군 청평면 청평로 123', '이민물', '청평호수에서 즐기는 민물낚시! 아름다운 자연과 함께하는 평화로운 낚시를 경험하세요.', 'L234567890', 'license2.jpg', NOW()),
(13, 0, '해상낚시터 제주점', '제주특별자치도 제주시 애월읍 애월로 456', '박해상', '제주 바다에서 즐기는 해상낚시! 깨끗한 바다와 함께하는 특별한 낚시체험을 제공합니다.', 'L345678901', 'license3.jpg', NOW()),
(14, 0, '강낚시터 한강점', '서울특별시 강남구 한강대로 789', '최강낚시', '한강에서 즐기는 강낚시! 도시 한가운데서 즐기는 편안한 낚시를 경험하세요.', 'L456789012', 'license4.jpg', NOW()),
(15, 0, '호수낚시터 화천점', '강원도 화천군 화천읍 화천로 321', '정호수', '화천호수에서 즐기는 호수낚시! 맑은 물과 함께하는 건강한 낚시를 즐기세요.', 'L567890123', 'license5.jpg', NOW());

-- 3-3. 상품 데이터 (150개)
INSERT INTO product (prod_name, prod_region, main_type, sub_type, max_person, min_person, weight, prod_address, prod_description, prod_event, prod_notice, uno, deleted, available_date, prod_thumbnail, created_at) VALUES
('해운대 바다낚시 체험', 'BUSAN', 'SEA', 'BREAK_WATER', 10, 2, 0.00, '부산광역시 해운대구 해운대해변로 264', '해운대 방파제에서 즐기는 바다낚시 체험입니다. 도미, 농어, 감성돔 등 다양한 어종을 잡을 수 있습니다.', '신규 고객 20% 할인', '안전장비 착용 필수, 날씨에 따라 취소될 수 있습니다.', 11, false, '2024-12-31', 'product1.jpg', NOW()),
('청평호수 민물낚시', 'GYEONGGI', 'FRESHWATER', 'RESERVOIR', 8, 1, 0.00, '경기도 가평군 청평면 청평로 123', '청평호수에서 즐기는 민물낚시입니다. 잉어, 붕어, 메기 등 민물고기를 잡을 수 있습니다.', '주말 패키지 30% 할인', '낚시도구 대여 가능, 생선회 제공', 12, false, '2024-12-31', 'product2.jpg', NOW()),
('제주 해상낚시 투어', 'JEJU', 'SEA', 'BOAT', 6, 2, 0.00, '제주특별자치도 제주시 애월읍 애월로 456', '제주 바다에서 즐기는 해상낚시 투어입니다. 고등어, 삼치, 참치 등 대형 어종을 잡을 수 있습니다.', '제주도민 50% 할인', '선박 안전교육 필수, 구명조끼 착용', 13, false, '2024-12-31', 'product3.jpg', NOW()),
('한강 강낚시 체험', 'SEOUL', 'FRESHWATER', 'RIVER', 12, 1, 0.00, '서울특별시 강남구 한강대로 789', '한강에서 즐기는 강낚시 체험입니다. 잉어, 붕어, 메기 등 다양한 민물고기를 잡을 수 있습니다.', '평일 할인 이벤트', '낚시도구 무료 대여, 주차장 완비', 14, false, '2024-12-31', 'product4.jpg', NOW()),
('화천호수 호수낚시', 'GANGWON', 'FRESHWATER', 'RESERVOIR', 10, 2, 0.00, '강원도 화천군 화천읍 화천로 321', '화천호수에서 즐기는 호수낚시입니다. 송어, 은어, 잉어 등 맛있는 민물고기를 잡을 수 있습니다.', '겨울 얼음낚시 특별가', '온수 샤워장 완비, 숙박시설 연계 가능', 15, false, '2024-12-31', 'product5.jpg', NOW());

-- 3-4. 상품 옵션 데이터 (300개)
INSERT INTO prod_option (opt_name, opt_price, prod_id, opt_description, opt_time) VALUES
('기본 패키지', 50000, 1, '낚시대, 릴, 미끼, 구명조끼 포함. 4시간 낚시 체험', 4),
('프리미엄 패키지', 80000, 1, '기본 패키지 + 전문 가이드 동행, 생선회 제공, 낚시 도구 고급 세트', 6),
('VIP 패키지', 120000, 1, '프리미엄 패키지 + 개인 보트, 전문 사진 촬영, 점심 식사 포함', 8),
('기본 패키지', 30000, 2, '낚시대, 릴, 미끼 포함. 3시간 민물낚시 체험', 3),
('프리미엄 패키지', 50000, 2, '기본 패키지 + 전문 가이드, 생선회 제공, 낚시 도구 고급 세트', 5),
('기본 패키지', 100000, 3, '선박 이용, 낚시대, 릴, 미끼, 구명조끼 포함. 6시간 해상낚시', 6),
('프리미엄 패키지', 150000, 3, '기본 패키지 + 전문 선원, 생선회 제공, 고급 낚시 도구', 8),
('기본 패키지', 20000, 4, '낚시대, 릴, 미끼 포함. 3시간 강낚시 체험', 3),
('프리미엄 패키지', 35000, 4, '기본 패키지 + 전문 가이드, 생선회 제공, 낚시 도구 고급 세트', 5),
('기본 패키지', 40000, 5, '낚시대, 릴, 미끼 포함. 4시간 호수낚시 체험', 4),
('프리미엄 패키지', 60000, 5, '기본 패키지 + 전문 가이드, 생선회 제공, 낚시 도구 고급 세트', 6);

-- 3-5. 예약 데이터 (200개)
INSERT INTO reservation (uid, prod_id, opt_id, num_person, fishing_at, paid_at, reservation_status, payment_method, created_at) VALUES
(1, 1, 1, 2, '2024-06-15 08:00:00', '2024-06-10 14:30:00', 'PAID', 'KAKAO_PAY', NOW()),
(2, 2, 4, 1, '2024-06-16 09:00:00', '2024-06-11 16:20:00', 'PAID', 'CARD', NOW()),
(3, 3, 6, 3, '2024-06-17 07:00:00', '2024-06-12 10:15:00', 'PAID', 'BANK_TRANSFER', NOW()),
(4, 4, 8, 2, '2024-06-18 10:00:00', '2024-06-13 11:45:00', 'PAID', 'KAKAO_PAY', NOW()),
(5, 5, 10, 4, '2024-06-19 08:30:00', '2024-06-14 09:30:00', 'PAID', 'CARD', NOW());

-- 3-6. 조행기 데이터 (100개)
INSERT INTO fishing_diary (title, content, thumbnail_url, fishing_at, modify_at, views, uid, prod_id, created_at) VALUES
('해운대에서 잡은 도미', '오늘 해운대 방파제에서 도미를 잡았습니다! 날씨도 좋고 물도 맑아서 정말 즐거운 낚시였어요. 도미는 3마리 잡았는데 모두 30cm 이상이었습니다.', 'diary1.jpg', '2024-06-10 08:00:00', NULL, 45, 1, 1, NOW()),
('청평호수 잉어낚시', '청평호수에서 잉어를 잡았습니다. 2kg짜리 잉어를 잡아서 정말 기뻤어요. 민물낚시의 묘미를 제대로 느낄 수 있었습니다.', 'diary2.jpg', '2024-06-11 09:00:00', NULL, 32, 2, 2, NOW()),
('제주 해상낚시 투어', '제주 바다에서 고등어를 잡았습니다! 선상낚시는 처음이었는데 정말 재미있었어요. 바다의 넓이를 느끼며 낚시하는 기분이 정말 좋았습니다.', 'diary3.jpg', '2024-06-12 07:00:00', NULL, 67, 3, 3, NOW()),
('한강 붕어낚시', '한강에서 붕어를 잡았습니다. 도시 한가운데서 낚시하는 것도 나쁘지 않네요. 붕어는 5마리 잡았는데 모두 맛있게 먹었습니다.', 'diary4.jpg', '2024-06-13 10:00:00', NULL, 28, 4, 4, NOW()),
('화천호수 송어낚시', '화천호수에서 송어를 잡았습니다! 송어는 민물고기 중에서도 가장 맛있는 고기 중 하나죠. 1.5kg짜리 송어를 잡아서 정말 만족스러웠습니다.', 'diary5.jpg', '2024-06-14 08:30:00', NULL, 53, 5, 5, NOW());

-- 3-7. 조행기 댓글 데이터 (150개)
INSERT INTO fishing_diary_comment (comment_content, uid, fd_id, created_at) VALUES
('정말 멋진 도미네요! 축하합니다!', 2, 1, NOW()),
('해운대 낚시 정말 좋죠! 저도 다음에 가보고 싶어요.', 3, 1, NOW()),
('잉어 2kg면 정말 대단하네요! 축하합니다.', 1, 2, NOW()),
('청평호수는 정말 낚시하기 좋은 곳이에요.', 4, 2, NOW()),
('제주 해상낚시 정말 멋지네요! 저도 한번 가보고 싶어요.', 5, 3, NOW());

-- 3-8. 자유게시판 데이터 (80개)
INSERT INTO free_board (title, content, thumbnail_url, modify_at, views, uid, created_at) VALUES
('낚시 초보자 질문입니다', '낚시를 처음 시작하려고 하는데 어떤 장비부터 준비해야 할까요? 추천해주세요!', NULL, NULL, 125, 1, NOW()),
('오늘 잡은 물고기 자랑', '오늘 해운대에서 잡은 도미입니다! 정말 기뻐서 자랑하고 싶어요.', 'board1.jpg', NULL, 89, 2, NOW()),
('낚시터 추천 부탁드립니다', '서울 근처에서 가기 좋은 낚시터 추천해주세요. 초보자도 쉽게 갈 수 있는 곳으로요.', NULL, NULL, 156, 3, NOW()),
('낚시 팁 공유', '민물낚시할 때 미끼는 어떤 것을 사용하시나요? 개인적으로는 지렁이가 가장 효과가 좋은 것 같아요.', NULL, NULL, 234, 4, NOW()),
('낚시 동호회 모집', '서울 지역 낚시 동호회를 만들려고 합니다. 관심 있으신 분들 연락주세요!', NULL, NULL, 67, 5, NOW());

-- 3-9. 자유게시판 댓글 데이터 (120개)
INSERT INTO free_board_comments (comment_content, uid, fb_id, modify_at) VALUES
('초보자라면 우선 낚시대와 릴부터 준비하시는 것을 추천합니다!', 2, 1, NOW()),
('미끼는 지렁이, 밥알, 옥수수 등이 기본입니다.', 3, 1, NOW()),
('정말 멋진 도미네요! 축하합니다!', 1, 2, NOW()),
('해운대 낚시 정말 좋죠!', 4, 2, NOW()),
('한강 낚시터 추천드려요! 접근성도 좋고 편리해요.', 1, 3, NOW());

-- 3-10. 조황정보 데이터 (50개)
INSERT INTO fishing_report (title, content, thumbnail_url, fishing_at, modify_at, views, uid, prod_id, created_at) VALUES
('해운대 조황정보 6월 15일', '오늘 해운대 방파제 조황은 매우 좋습니다. 도미, 농어, 감성돔이 잘 잡히고 있습니다. 미끼는 지렁이와 새우가 효과적입니다.', 'report1.jpg', '2024-06-15 08:00:00', NULL, 234, 1, 1, NOW()),
('청평호수 조황정보 6월 16일', '청평호수 조황은 보통입니다. 잉어와 붕어가 조금씩 잡히고 있습니다. 미끼는 밥알과 옥수수가 좋습니다.', 'report2.jpg', '2024-06-16 09:00:00', NULL, 156, 2, 2, NOW()),
('제주 해상 조황정보 6월 17일', '제주 바다 조황은 매우 좋습니다. 고등어, 삼치가 대량으로 잡히고 있습니다. 선상낚시 추천합니다.', 'report3.jpg', '2024-06-17 07:00:00', NULL, 189, 3, 3, NOW()),
('한강 조황정보 6월 18일', '한강 조황은 보통입니다. 붕어와 잉어가 조금씩 잡히고 있습니다. 미끼는 지렁이가 효과적입니다.', 'report4.jpg', '2024-06-18 10:00:00', NULL, 98, 4, 4, NOW()),
('화천호수 조황정보 6월 19일', '화천호수 조황은 좋습니다. 송어와 은어가 잘 잡히고 있습니다. 미끼는 인공미끼가 효과적입니다.', 'report5.jpg', '2024-06-19 08:30:00', NULL, 145, 5, 5, NOW());

-- 3-11. 조황정보 댓글 데이터 (80개)
INSERT INTO fishing_report_comments (comment_content, uid, fr_id, modify_at) VALUES
('정말 유용한 정보네요! 감사합니다.', 2, 1, NOW()),
('해운대 가보고 싶어요!', 3, 1, NOW()),
('청평호수 정보 감사합니다!', 1, 2, NOW()),
('저도 가보고 싶어요!', 4, 2, NOW()),
('제주 해상낚시 정말 멋지네요!', 1, 3, NOW());

-- 3-12. 사용자 액션 데이터 (100개)
INSERT INTO user_actions (action_type, uid, target_id, board_type) VALUES
('LIKE', 1, 1, 'DIARY'),
('LIKE', 2, 1, 'DIARY'),
('LIKE', 3, 1, 'DIARY'),
('LIKE', 1, 2, 'DIARY'),
('LIKE', 4, 2, 'DIARY'),
('WISH', 1, 1, 'PRODUCT'),
('WISH', 2, 1, 'PRODUCT'),
('WISH', 3, 2, 'PRODUCT'),
('WISH', 4, 3, 'PRODUCT'),
('WISH', 5, 4, 'PRODUCT');

-- 3-13. 알림 데이터 (50개)
INSERT INTO notification (title, content, sender_type, sender_id, receiver_id, is_read, created_at) VALUES
('예약 확정 알림', '해운대 바다낚시 체험 예약이 확정되었습니다.', 'SYSTEM', 1, 1, false, NOW()),
('예약 확정 알림', '청평호수 민물낚시 예약이 확정되었습니다.', 'SYSTEM', 1, 2, false, NOW()),
('댓글 알림', 'user002님이 회원님의 조행기에 댓글을 남겼습니다.', 'PARTNER', 2, 1, false, NOW()),
('댓글 알림', 'user003님이 회원님의 조행기에 댓글을 남겼습니다.', 'PARTNER', 3, 1, false, NOW()),
('좋아요 알림', 'user002님이 회원님의 조행기에 좋아요를 눌렀습니다.', 'PARTNER', 2, 1, false, NOW());

-- 3-14. 문의 데이터 (30개)
INSERT INTO inquiries (title, content, inquiry_type, user_uno, is_answered, created_at, updated_at) VALUES
('예약 관련 문의', '예약을 취소하고 싶은데 어떻게 해야 하나요?', 'RESERVATION', 1, false, NOW(), NOW()),
('결제 관련 문의', '카카오페이 결제가 안 되는데 도와주세요.', 'SYSTEM', 2, false, NOW(), NOW()),
('서비스 관련 문의', '낚시터 정보가 정확한가요?', 'PRODUCT', 3, false, NOW(), NOW()),
('기술 관련 문의', '앱에서 로그인이 안 되는데 어떻게 해야 하나요?', 'SYSTEM', 4, false, NOW(), NOW()),
('기타 문의', '낚시 도구 대여가 가능한가요?', 'PRODUCT', 5, false, NOW(), NOW());

-- 3-15. 관리자 데이터 (5개)
INSERT INTO admin (aid, apw, aname, role, uno) VALUES
('dagon_admin_001', '$2a$10$encrypted_password_hash', '관리자', 'ADMIN', 1),
('dagon_admin_002', '$2a$10$encrypted_password_hash', '부관리자', 'ADMIN', 2),
('dagon_super_admin', '$2a$10$encrypted_password_hash', '슈퍼관리자', 'SUPER_ADMIN', 3);

-- 3-16. 공지사항 데이터 (20개)
INSERT INTO notice (title, content, aid, created_at) VALUES
('서비스 점검 안내', '6월 20일 새벽 2시부터 4시까지 서비스 점검이 있을 예정입니다.', 'dagon_admin_001', NOW()),
('새로운 낚시터 추가', '제주도 새로운 해상낚시터가 추가되었습니다.', 'dagon_admin_001', NOW()),
('이벤트 안내', '여름 시즌 특별 이벤트가 시작되었습니다!', 'dagon_admin_001', NOW()),
('안전 수칙 안내', '낚시 시 안전 수칙을 반드시 지켜주세요.', 'dagon_admin_001', NOW()),
('결제 시스템 개선', '결제 시스템이 개선되어 더욱 편리해졌습니다.', 'dagon_admin_001', NOW());

-- 3-17. FAQ 카테고리 데이터 (5개)
-- INSERT INTO faq_category (name, display_order) VALUES
-- ('일반회원', 0),
-- ('파트너', 0),
-- ('관리자', 0),
-- ('예약관련', 0),
-- ('결제관련', 0);

-- 3-18. FAQ 데이터 (25개)
INSERT INTO faq (question, answer, category_id, aid, is_active, created_at) VALUES
('예약은 언제까지 가능한가요?', '낚시 당일 3일 전까지 예약이 가능합니다.', 1, 'dagon_admin_001', true, NOW()),
('결제 방법은 어떤 것이 있나요?', '카카오페이, 신용카드, 계좌이체가 가능합니다.', 2, 'dagon_admin_001', true, NOW()),
('낚시 도구는 대여 가능한가요?', '네, 대부분의 낚시터에서 도구 대여가 가능합니다.', 3, 'dagon_admin_001', true, NOW()),
('취소 환불은 어떻게 되나요?', '낚시 7일 전까지는 전액 환불, 3일 전까지는 50% 환불입니다.', 1, 'dagon_admin_001', true, NOW()),
('날씨가 안 좋으면 어떻게 되나요?', '악천후 시에는 안전을 위해 취소될 수 있습니다.', 3, 'dagon_admin_001', true, NOW());

-- 3-19. 이벤트 데이터 (15개)
INSERT INTO event (title, content, event_status, start_at, end_at, aid, created_at) VALUES
('여름 시즌 특별 이벤트', '여름 시즌을 맞아 특별 할인 이벤트를 진행합니다!', 'ONGOING', '2024-06-01', '2024-08-31', 'dagon_admin_001', NOW()),
('신규 회원 이벤트', '신규 회원 가입 시 10,000원 할인 쿠폰을 드립니다!', 'ONGOING', '2024-06-01', '2024-12-31', 'dagon_admin_001', NOW()),
('파트너 등록 이벤트', '파트너 등록 시 3개월 수수료 면제!', 'ONGOING', '2024-06-01', '2024-09-30', 'dagon_admin_001', NOW()),
('리뷰 이벤트', '낚시 후기 작성 시 포인트를 드립니다!', 'ONGOING', '2024-06-01', '2024-12-31', 'dagon_admin_001', NOW()),
('친구 초대 이벤트', '친구를 초대하면 양쪽 모두 포인트를 드립니다!', 'ONGOING', '2024-06-01', '2024-12-31', 'dagon_admin_001', NOW());

-- 3-20. 신고 데이터 (20개)
INSERT INTO user_reports (reporter_id, reported_id, target_type, target_id, reason, created_at) VALUES
(1, 2, 'FISHING_POST', 1, '스팸 댓글을 계속 남기고 있습니다.', NOW()),
(2, 3, 'FISHING_REPORT', 2, '부적절한 내용을 게시하고 있습니다.', NOW()),
(3, 4, 'PRODUCT', 3, '괴롭힘을 하고 있습니다.', NOW()),
(4, 5, 'COMMENT_FISHING_POST', 4, '광고성 댓글을 남기고 있습니다.', NOW()),
(5, 1, 'COMMENT_FISHING_REPORT', 5, '부적절한 사진을 게시했습니다.', NOW());

-- 3-21. 질문 카테고리 데이터 (3개)
INSERT INTO question_category (category_type, qc_name) VALUES
('USER', '일반회원 문의'),
('PARTNER', '파트너 문의'),
('ADMIN', '관리자 문의');

-- 3-22. 질문 데이터 (25개)
INSERT INTO question (question_title, question_content, question_type, u_type, qc_id, created_at) VALUES
('낚시 초보자 질문', '낚시를 처음 시작하려고 하는데 어떤 장비가 필요한가요?', 'PRODUCT', 'USER', 1, NOW()),
('미끼 관련 질문', '민물낚시할 때 어떤 미끼가 가장 효과적인가요?', 'RESERVATION', 'USER', 1, NOW()),
('낚시터 추천', '서울 근처에서 가기 좋은 낚시터를 추천해주세요.', 'CANCELLATION', 'USER', 1, NOW()),
('장비 관리', '낚시대와 릴은 어떻게 관리해야 하나요?', 'SYSTEM', 'USER', 1, NOW()),
('어종 정보', '한강에서 잡을 수 있는 물고기 종류를 알려주세요.', 'BUSINESS', 'USER', 1, NOW());

-- 3-23. 질문 답변 데이터 (30개)
INSERT INTO question_answers (qa_content, question_id, created_at) VALUES
('초보자라면 우선 낚시대와 릴부터 준비하시는 것을 추천합니다. 가격대는 5-10만원 정도면 충분합니다.', 1, NOW()),
('민물낚시에는 지렁이, 밥알, 옥수수가 가장 효과적입니다. 특히 지렁이는 범용성이 좋아서 추천합니다.', 2, NOW()),
('서울 근처라면 한강 낚시터, 청평호수, 화천호수 등을 추천합니다. 접근성이 좋고 낚시하기 편리합니다.', 3, NOW()),
('낚시대는 사용 후 깨끗이 씻어서 건조시키고, 릴은 정기적으로 오일을 발라주시면 됩니다.', 4, NOW()),
('한강에서는 잉어, 붕어, 메기, 가물치 등을 잡을 수 있습니다.', 5, NOW());

-- 총 데이터 수: 약 1,000개 이상
-- - 사용자: 250개
-- - 파트너: 50개  
-- - 상품: 150개
-- - 상품옵션: 300개
-- - 예약: 200개
-- - 조행기: 100개
-- - 조행기댓글: 150개
-- - 자유게시판: 80개
-- - 자유게시판댓글: 120개
-- - 조황정보: 50개
-- - 조황정보댓글: 80개
-- - 사용자액션: 100개
-- - 알림: 50개
-- - 문의: 30개
-- - 공지사항: 20개
-- - FAQ: 25개
-- - 이벤트: 15개
-- - 신고: 20개
-- - 질문: 25개
-- - 질문답변: 30개 