-- 기존 조행기와 조황정보의 사용자를 1번에서 다른 사용자로 변경하는 스크립트
-- 실제 사용자 ID로 변경해야 합니다 (예: 2, 3, 4 등)

-- 조행기 사용자 변경 (1번 → 2번 사용자로 변경 예시)
UPDATE fishing_diary SET uid = 2 WHERE uid = 1;

-- 조황정보 사용자 변경 (1번 → 2번 사용자로 변경 예시)
UPDATE fishing_report SET uid = 2 WHERE uid = 1;

-- 조행기 댓글 사용자 변경 (1번 → 2번 사용자로 변경 예시)
UPDATE fishing_diary_comment SET uid = 2 WHERE uid = 1;

-- 조황정보 댓글 사용자 변경 (1번 → 2번 사용자로 변경 예시)
UPDATE fishing_report_comments SET uid = 2 WHERE uid = 1;

-- 변경 결과 확인
SELECT 'fishing_diary' as table_name, fd_id, title, uid FROM fishing_diary WHERE uid = 2
UNION ALL
SELECT 'fishing_report' as table_name, fr_id, title, uid FROM fishing_report WHERE uid = 2; 