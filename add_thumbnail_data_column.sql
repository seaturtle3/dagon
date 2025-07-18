-- prod_image 테이블에 thumbnail_data 컬럼 추가
ALTER TABLE prod_image 
ADD COLUMN thumbnail_data LONGBLOB NULL 
COMMENT '썸네일 이미지 데이터 (Base64 인코딩)';

-- 기존 데이터에 대해 thumbnail_data를 image_data와 동일하게 설정 (임시)
UPDATE prod_image 
SET thumbnail_data = image_data 
WHERE image_data IS NOT NULL AND thumbnail_data IS NULL;

-- 컬럼 추가 확인
DESCRIBE prod_image; 