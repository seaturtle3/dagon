-- 1. 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS dagon CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 2. root 비밀번호 설정 및 인증 방식 명시
ALTER USER 'root'@'localhost'
  IDENTIFIED WITH mysql_native_password BY 'edurootroot';

-- 3. admin 사용자 생성 (있으면 생략) + 비밀번호 및 인증 방식
CREATE USER IF NOT EXISTS 'root'@'%'
  IDENTIFIED WITH mysql_native_password BY 'edurootroot';
CREATE USER IF NOT EXISTS 'admin'@'%'
  IDENTIFIED WITH mysql_native_password BY 'edurootroot';

-- 4. admin 사용자에게 권한 부여
GRANT ALL PRIVILEGES ON dagon.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON dagon.* TO 'admin'@'%';

-- 5. 변경 사항 적용
FLUSH PRIVILEGES;

