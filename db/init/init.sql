-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: docs.yi.or.kr    Database: dagon
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `aid` varchar(50) NOT NULL,
  `aname` varchar(255) DEFAULT NULL,
  `apw` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('1234','1234','$2a$10$JaztIdV61vlki751f6I6me1JCsxmmF7KqHwqkWhGj3AqjRqG4FmiO',NULL),('12345','213123','$2a$10$PtgNH1imw5jJoswCffVSYuDecTA5/oaO1w0hxm0hGwejkqeUtuEh2',NULL),('admin001','장테스트','1111','ADMIN'),('admin1','1짱어드민','$2a$10$T09gDLRYu3ZHkYNTkBvIj.dWl4tFjzhTmH6Id/L4uS2vI3Q9WKApy','ADMIN'),('admin12','admin','$2a$10$zRA2sR7SU0NZBlqFt/ewFuvYnPqtlSTMArezEiBkP8qoGLwrGwkxO','ADMIN'),('asd','관리자님','$2a$10$q4AHn8v14Qf1y/H/QY093esoTZgthIPBKiGk2S9hFvxYNNsdLhCwW','ADMIN'),('asd020712','박준하','$2a$10$Jb9b2NEtu31bo2Vm2WDk4O9rjgOGZSJUlF8tNmh8Vv70qrzoSAeGa','ADMIN'),('asd020712!','슈퍼어드민','$2a$10$i8cqXkts8gZVLAL7S/28Ye2dMM86PAe67QpNs0AJYk8fa0GbEcvz6','ADMIN'),('asd020712!!','슈퍼어드민','$2a$10$SaCQUbhYVk1pObbbxq3f1.EbnRnVtFYSjqaB25BzVSD94TnbwWAiu','ADMIN'),('asd020712a','어드민','$2a$10$NLrJgqsPg/PdLbbT45IVUOn5VFbfKmfRA9A8WG/YY50SXyKZd7gGG','ADMIN'),('asd020712aa','테스트','$2a$10$yvLRZ4enqBlbY0XgpMgYp.wCqeuSIEdkMVP4CcNlPPX2L6Cq2Pnpq','ADMIN'),('asd020712ㅁ','관리자님','$2a$10$bDEIWTZ5TCEm0lRzG4qPWOAA2g3iDTcIWf/p.V7E/S2XnPyNXhM5m','ADMIN'),('asd200712','박준하','$2a$10$RQazMb5eqLoD0oC.iITqau9ICO33Dvh/dHew3g6nCOZWufX0Sdq7a',NULL),('jang','장은정','$2a$10$RESaChUnvbmPOmj4AeaPE.ACKuhXts5WbCC1CMXVYjPvqeOxY.zTm','ADMIN'),('qwer1234512','admin','$2a$10$jzHFaBMwuS..KgE1q1FgruJ8yCSQjKhU5FAZjTIzQapW6DQS2T5RC','ADMIN'),('qwer12345122','박준하','$2a$10$rkFJ.9B6OpG77q1h6PIItOEVzpdHXH5xhj3cG6iBEu0rOekbBHJIi','ADMIN');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_key_callback_urls`
--

DROP TABLE IF EXISTS `api_key_callback_urls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_key_callback_urls` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  `api_key_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsp4tthlhjpw21mb24fpv7aor2` (`api_key_id`),
  CONSTRAINT `FKsp4tthlhjpw21mb24fpv7aor2` FOREIGN KEY (`api_key_id`) REFERENCES `api_keys` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_key_callback_urls`
--

LOCK TABLES `api_key_callback_urls` WRITE;
/*!40000 ALTER TABLE `api_key_callback_urls` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_key_callback_urls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_keys`
--

DROP TABLE IF EXISTS `api_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_keys` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `allowed_ip` varchar(255) DEFAULT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `issued_at` datetime(6) DEFAULT NULL,
  `api_key` varchar(1024) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_keys`
--

LOCK TABLES `api_keys` WRITE;
/*!40000 ALTER TABLE `api_keys` DISABLE KEYS */;
INSERT INTO `api_keys` VALUES (1,_binary '',NULL,NULL,'2025-05-02 11:46:54.557367','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QyMDA3MTIiLCJ1bm8iOjI1LCJ1bmFtZSI6IuydtOyDgeymiCIsIm5pY2tuYW1lIjoi7KCc67CcIiwiZW1haWwiOiJhc2QwMjA3MTJhQGFhc2Rhc2QxcTIiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjE1NDAxNCwiZXhwIjoxNzQ2MjQwNDE0fQ.IuZhdIh6FZ3he2GY2lFb8LIq6zHO7SbeD0g1J76rhUQ','기본 로그인 API 키'),(2,_binary '',NULL,NULL,'2025-05-02 12:03:48.313037','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjE1NTAyOCwiZXhwIjoxNzQ2MjQxNDI4fQ.kxq3Dm2iPicBSB9lHPenD4h04O2IL-1m23JdPeIKR0o','기본 로그인 API 키'),(3,_binary '',NULL,NULL,'2025-05-02 12:16:55.099753','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QyMDA3MTIiLCJ1bm8iOjI1LCJ1bmFtZSI6IuydtOyDgeymiCIsIm5pY2tuYW1lIjoi7KCc67CcIiwiZW1haWwiOiJhc2QwMjA3MTJhQGFhc2Rhc2QxcTIiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjE1NTgxNSwiZXhwIjoxNzQ2MjQyMjE1fQ.0TzZfKStV6LEa8lKbJIk4X05Nm1jffxMIrHtC0uQxzY','기본 로그인 API 키'),(4,_binary '',NULL,NULL,'2025-05-02 12:21:47.358707','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QyMDA3MTIiLCJ1bm8iOjI1LCJ1bmFtZSI6IuydtOyDgeymiCIsIm5pY2tuYW1lIjoi7KCc67CcIiwiZW1haWwiOiJhc2QwMjA3MTJhQGFhc2Rhc2QxcTIiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjE1NjEwNywiZXhwIjoxNzQ2MjQyNTA3fQ.r5aXdOXKHT20eLCcuVrR7_wffcMJd7GDrYELg8ZRKtM','기본 로그인 API 키'),(5,_binary '',NULL,NULL,'2025-05-02 12:22:20.226020','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QyMDA3MTIiLCJ1bm8iOjI1LCJ1bmFtZSI6IuydtOyDgeymiCIsIm5pY2tuYW1lIjoi7KCc67CcIiwiZW1haWwiOiJhc2QwMjA3MTJhQGFhc2Rhc2QxcTIiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjE1NjE0MCwiZXhwIjoxNzQ2MjQyNTQwfQ.SzqoUAZgd5Ud7dNA4lB7NgWGGwrKXgVsHm0pbmqrIdk','기본 로그인 API 키'),(6,_binary '\0',NULL,'2025-05-02 12:24:24.332772','2025-05-02 12:24:15.921819','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QyMDA3MTIiLCJ1bm8iOjI1LCJ1bmFtZSI6IuydtOyDgeymiCIsIm5pY2tuYW1lIjoi7KCc67CcIiwiZW1haWwiOiJhc2QwMjA3MTJhQGFhc2Rhc2QxcTIiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjE1NjI1NSwiZXhwIjoxNzQ2MjQyNjU1fQ.SDfZIJuetgI11XXIr6foeO40CGCVVlH1Udo8QUPuM1Q','기본 로그인 API 키'),(7,_binary '\0',NULL,'2025-05-02 12:30:19.369265','2025-05-02 12:30:17.221022','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjE1NjYxNywiZXhwIjoxNzQ2MjQzMDE3fQ.H9WQECZ1l-asKtGKNFMtWeR379uB_5B8dzPMPubi4N8','기본 로그인 API 키'),(8,_binary '\0',NULL,'2025-05-02 12:31:19.535062','2025-05-02 12:31:17.660250','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjE1NjY3NywiZXhwIjoxNzQ2MjQzMDc3fQ.kspw7Ukn6pxFyL2uiEOhaNzm5znBDk53v8dkQ6p66y0','기본 로그인 API 키'),(9,_binary '\0',NULL,'2025-05-02 12:31:42.887252','2025-05-02 12:31:41.602408','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjE1NjcwMSwiZXhwIjoxNzQ2MjQzMTAxfQ.vpkh9UEohvE9Oxh8krLO3YbaM0NWyDFrV8wg1eeJ6wo','기본 로그인 API 키'),(10,_binary '',NULL,NULL,'2025-05-02 12:31:45.946260','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjE1NjcwNSwiZXhwIjoxNzQ2MjQzMTA1fQ.L5wSQqTWEXhFp0JlOIrSHVh4DucGD-kX2bVR1IGtMRU','기본 로그인 API 키'),(11,_binary '',NULL,NULL,'2025-05-02 12:32:05.619961','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QyMDA3MTIiLCJ1bm8iOjI1LCJ1bmFtZSI6IuydtOyDgeymiCIsIm5pY2tuYW1lIjoi7KCc67CcIiwiZW1haWwiOiJhc2QwMjA3MTJhQGFhc2Rhc2QxcTIiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjE1NjcyNSwiZXhwIjoxNzQ2MjQzMTI1fQ.wqp7jG_UUxWMevhA4j3JHFvxKm0hvHdIVd-4PEIaY_Q','기본 로그인 API 키'),(12,_binary '',NULL,NULL,'2025-05-02 12:34:43.591446','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjE1Njg4MywiZXhwIjoxNzQ2MjQzMjgzfQ.4fNQfaZFfmWyMQRtUDMNJucThq1bkjiOjUQP22ZFN_k','기본 로그인 API 키'),(13,_binary '\0',NULL,'2025-05-02 13:47:32.937065','2025-05-02 13:46:04.508314','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QyMDA3MTIiLCJ1bm8iOjI1LCJ1bmFtZSI6IuydtOyDgeymiCIsIm5pY2tuYW1lIjoi7KCc67CcIiwiZW1haWwiOiJhc2QwMjA3MTJhQGFhc2Rhc2QxcTIiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjE2MTE2NCwiZXhwIjoxNzQ2MjQ3NTY0fQ.3zGQKimXJfa_SsddODApNca-yX8juXowIYb9CnmyG5Q','기본 로그인 API 키'),(14,_binary '\0',NULL,'2025-05-02 13:55:04.437991','2025-05-02 13:48:03.756627','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MWEiLCJ1bm8iOjIxLCJ1bmFtZSI6IuuwleykgO2VmCIsIm5pY2tuYW1lIjoi6raB6riIIiwiZW1haWwiOiJhc2QwMjA3MTJhQGdtYWlsLmNvbTExIiwicm9sZSI6IlBBUlRORVIiLCJpYXQiOjE3NDYxNjEyODMsImV4cCI6MTc0NjI0NzY4M30.obfNvn9lYh5hySDDa1je-sO-VIFheLfD-HC0JA2Zlsc','기본 로그인 API 키'),(15,_binary '',NULL,NULL,'2025-05-02 13:55:13.465212','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MWEiLCJ1bm8iOjIxLCJ1bmFtZSI6IuuwleykgO2VmCIsIm5pY2tuYW1lIjoi6raB6riIIiwiZW1haWwiOiJhc2QwMjA3MTJhQGdtYWlsLmNvbTExIiwicm9sZSI6IlBBUlRORVIiLCJpYXQiOjE3NDYxNjE3MTMsImV4cCI6MTc0NjI0ODExM30.vVU5pWbfde1hldRSV1tQfa0AN8ryYcXRPRaohj6gSbI','기본 로그인 API 키'),(16,_binary '',NULL,NULL,'2025-05-02 14:47:16.261376','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTJhYSIsInVubyI6IjI3IiwidW5hbWUiOiLslpHtlZnspJEiLCJlbWFpbCI6ImRhZGFkZEBhc2Rhc2QyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzQ2MTY0ODM2LCJleHAiOjE3NDYyNTEyMzZ9.MkfXJtPGSXhMXL0dwfYGqFDRUT4wXCRg4YISCqXvKGA','기본 로그인 API 키'),(17,_binary '',NULL,NULL,'2025-05-02 14:48:27.806773','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjE2NDkwNywiZXhwIjoxNzQ2MjUxMzA3fQ.8bhKbn48SOzGLvYyId6nLyi1wdB5o5kLPIRLJ5Vdbvs','기본 로그인 API 키'),(18,_binary '',NULL,NULL,'2025-05-02 16:07:18.272403','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTJhYWEiLCJ1bm8iOjMzLCJ1bmFtZSI6IuuwleyDgeyyoCIsImVtYWlsIjoiYXNkMDIwNzEyQUBuYWJlci5kYWQiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjE2OTYzOCwiZXhwIjoxNzQ2MjU2MDM4fQ.y5iry0tdCdIjrsq4Gii2UgivAtDt9WFDhfUyh4W7BKk','기본 로그인 API 키'),(19,_binary '',NULL,NULL,'2025-05-02 16:28:04.766806','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTIiLCJ1bm8iOiIxMSIsInVuYW1lIjoi7J207IKw7ZedIiwiZW1haWwiOiJhc2QyMjJAYXNkc2EyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzQ2MTcwODg0LCJleHAiOjE3NDYyNTcyODR9.mpRycykTOdiA95oJjIMkZVnwXPBPMnUTfjaA9hDynC0','기본 로그인 API 키'),(20,_binary '\0',NULL,'2025-05-02 16:33:55.630857','2025-05-02 16:29:44.657947','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTIiLCJ1bm8iOjExLCJ1bmFtZSI6IuydtOyCsO2XnSIsImVtYWlsIjoiYXNkMjIyQGFzZHNhMjIiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjE3MDk4NCwiZXhwIjoxNzQ2MjU3Mzg0fQ.EP991oOEO_lHC3ozg1zTqIGOWIkadzbknvfYmsd9XhU','기본 로그인 API 키'),(21,_binary '',NULL,NULL,'2025-05-02 16:34:05.537961','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTIiLCJ1bm8iOiIxMSIsInVuYW1lIjoi7J207IKw7ZedIiwiZW1haWwiOiJhc2QyMjJAYXNkc2EyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzQ2MTcxMjQ1LCJleHAiOjE3NDYyNTc2NDV9.HEax-b46VndDRj8QdD7gyIFLFMXowc5P8s5IKtj7i8k','기본 로그인 API 키'),(22,_binary '',NULL,NULL,'2025-05-07 15:06:28.977148','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTJhYSIsInVubyI6IjI3IiwidW5hbWUiOiLslpHtlZnspJEiLCJuaWNrbmFtZSI6IuqwleqwleyIoOuemCIsImVtYWlsIjoiZGFkYWRkQGFzZGFzZDIyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NDY1OTc5ODgsImV4cCI6MTc0NjY4NDM4OH0.4Kr-8TXdEOJzDBsYK8OxlOVrVX4fihpK_4KPEl9BGFk','기본 로그인 API 키'),(23,_binary '\0',NULL,'2025-05-07 15:09:25.413454','2025-05-07 15:09:20.882712','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjU5ODE2MCwiZXhwIjoxNzQ2Njg0NTYwfQ.TBfJLyUlf2k24m3FLVSQW0LQTTP9zwX_5qAzMBa8ytA','기본 로그인 API 키'),(24,_binary '\0',NULL,'2025-05-07 15:10:15.370696','2025-05-07 15:09:27.737054','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjU5ODE2NywiZXhwIjoxNzQ2Njg0NTY3fQ._r_S-H2Lj2kVNsGb4Y0Jo-ZCX6_sMlydyKh40nimeGQ','기본 로그인 API 키'),(25,_binary '',NULL,NULL,'2025-05-07 15:10:25.192258','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MWEiLCJ1bm8iOiIyMSIsInVuYW1lIjoi6rWs7ZWo66asIiwibmlja25hbWUiOiLquLDrpr8iLCJlbWFpbCI6ImFzZDAyMDcxMmFAZ21haWwuY29tMjIiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NjU5ODIyNSwiZXhwIjoxNzQ2Njg0NjI1fQ.aIY9l76zWzsbjOw8rYs0HvaOyp2QlIUo4H-ZXtPAbFs','기본 로그인 API 키'),(26,_binary '',NULL,NULL,'2025-05-07 15:15:26.310714','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MWEiLCJ1bm8iOjIxLCJ1bmFtZSI6Iuq1rO2VqOumrCIsIm5pY2tuYW1lIjoi6riw66a_IiwiZW1haWwiOiJhc2QwMjA3MTJhQGdtYWlsLmNvbTIyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NDY1OTg1MjYsImV4cCI6MTc0NjY4NDkyNn0.4aTDiuvFdcIaz3_7SDCjSA4venyxu0Y25Fi8knCmqbg','기본 로그인 API 키'),(27,_binary '',NULL,NULL,'2025-05-07 15:15:57.540335','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjU5ODU1NywiZXhwIjoxNzQ2Njg0OTU3fQ.WShfge3MlUOAIQD7FfRyiYX3Yd8F06fI56b_9yO1fA0','기본 로그인 API 키'),(28,_binary '\0',NULL,'2025-05-07 16:06:58.724721','2025-05-07 15:19:54.647999','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTJhYSIsInVubyI6MjcsInVuYW1lIjoi7JaR7ZWZ7KSRIiwibmlja25hbWUiOiLqsJXqsJXsiKAiLCJlbWFpbCI6ImRhZGFkZEBhc2Rhc2QyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzQ2NTk4Nzk0LCJleHAiOjE3NDY2ODUxOTR9.DNcjiYKSNZD6fGDWuh-kkrBPpX7kAOg30HxI_ivu0tY','기본 로그인 API 키'),(29,_binary '\0',NULL,'2025-05-07 16:21:53.416614','2025-05-07 16:07:01.625916','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTJhYSIsInVubyI6MjcsInVuYW1lIjoi7JaR7ZWZ7KSRIiwibmlja25hbWUiOiLqsJXqsJXsiKAiLCJlbWFpbCI6ImRhZGFkZEBhc2Rhc2QyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzQ2NjAxNjIxLCJleHAiOjE3NDY2ODgwMjF9.IAFrMyRHIjWiik3zma1RlCApBZkRVpYVHO4kVCatJW8','기본 로그인 API 키'),(30,_binary '',NULL,NULL,'2025-05-07 16:21:58.453631','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTJhYSIsInVubyI6MjcsInVuYW1lIjoi7JaR7ZWZ7KSRIiwibmlja25hbWUiOiLqsJXqsJXsiKAiLCJlbWFpbCI6ImRhZGFkZEBhc2Rhc2QyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzQ2NjAyNTE4LCJleHAiOjE3NDY2ODg5MTh9.6C4ZfAjVXIEVjSTmaawu1fpgwAPsvzNi2a6OTglfeXU','기본 로그인 API 키'),(31,_binary '',NULL,NULL,'2025-05-07 16:48:22.179661','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTJhYSIsInVubyI6MjcsInVuYW1lIjoi7JaR7ZWZ7KSRIiwibmlja25hbWUiOiLqsJXqsJXsiKAiLCJlbWFpbCI6ImRhZGFkZEBhc2Rhc2QyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzQ2NjA0MTAyLCJleHAiOjE3NDY2OTA1MDJ9.P0qdFc6z9yhZTQ7ImQ7Z8fi9Vg_pln0cTws9EWvWqqY','기본 로그인 API 키'),(32,_binary '\0',NULL,'2025-05-08 11:07:34.763926','2025-05-08 11:06:00.747493','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTJhYSIsInVubyI6MjcsInVuYW1lIjoi7JaR7ZWZ7KSRIiwibmlja25hbWUiOiLqsJXqsJXsiKAiLCJlbWFpbCI6ImRhZGFkZEBhc2Rhc2QyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzQ2NjY5OTYwLCJleHAiOjE3NDY3NTYzNjB9.TfGLNT7ayCA8lj3crwgEXHfdkOUwDFHjDkCWYHXS2ZM','기본 로그인 API 키'),(33,_binary '',NULL,NULL,'2025-05-08 11:06:52.261046','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjY3MDAxMiwiZXhwIjoxNzQ2NzU2NDEyfQ.b8ULNAsjC67UGkTgMwsbaVDBBS_mvVav0hkPzXsZZrk','기본 로그인 API 키'),(34,_binary '\0',NULL,'2025-05-08 12:37:09.354610','2025-05-08 11:09:05.462930','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjY3MDE0NSwiZXhwIjoxNzQ2NzU2NTQ1fQ.Nouefd_l3m07BydqplCU4tqi0rxBqMMRqINGH1wk8bE','기본 로그인 API 키'),(35,_binary '\0',NULL,'2025-05-08 14:02:35.529120','2025-05-08 12:37:11.603963','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjY3NTQzMSwiZXhwIjoxNzQ2NzYxODMxfQ.TIAB4pE7WiIXaHJRvDRhVWrvqfJsrYN_p9K97bV-quY','기본 로그인 API 키'),(36,_binary '',NULL,NULL,'2025-05-08 14:02:38.688868','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyIiwiYW5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc0NjY4MDU1OCwiZXhwIjoxNzQ2NzY2OTU4fQ.Lsaud_N-028RwnpuJp6z59A3f9lctYXkLBbsB-UlXIM','기본 로그인 API 키'),(37,_binary '\0',NULL,'2025-05-08 16:06:12.019788','2025-05-08 14:39:27.792856','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2QwMjA3MTJhYSIsInVubyI6MjcsInVuYW1lIjoi7JaR7ZWZ7KSRIiwibmlja25hbWUiOiLqsJXqsJXsiKAiLCJlbWFpbCI6ImRhZGFkZEBhc2Rhc2QyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzQ2NjgyNzY3LCJleHAiOjE3NDY3NjkxNjd9.TA4O4yt5Ii16Sb2oqXC-DVHTk09lZLFeLLaQZsi4bWQ','기본 로그인 API 키'),(38,_binary '',NULL,NULL,'2025-05-08 16:06:38.591586','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmZGFmc2FmMjM0MjMiLCJ1bm8iOjM1LCJ1bmFtZSI6IuydtO2VnOyEsSIsImVtYWlsIjoiYXNkMDIwNzEyYUBnbWFpbC5jb20yMjEyMzEyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NDY2ODc5OTgsImV4cCI6MTc0Njc3NDM5OH0.kewxXahRUf26FO8M4fUWzlL_QluRFC53qupovm-DpFY','기본 로그인 API 키');
/*!40000 ALTER TABLE `api_keys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content_reports`
--

DROP TABLE IF EXISTS `content_reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `content_reports` (
  `report_id` bigint NOT NULL AUTO_INCREMENT,
  `admin_note` varchar(500) DEFAULT NULL,
  `handled_at` datetime(6) DEFAULT NULL,
  `reason` varchar(500) NOT NULL,
  `status` enum('APPROVED','PENDING','REJECTED') NOT NULL,
  `target_id` bigint NOT NULL,
  `target_type` enum('DIARY','FREE','PRODUCT','REPORT') NOT NULL,
  `handled_by` bigint DEFAULT NULL,
  `uid` bigint NOT NULL,
  `handled_by_admin` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`report_id`),
  KEY `FKrsgwprv12344n1vpkippp5cwv` (`handled_by`),
  KEY `FKn487hnkcnc8dm5mh8dind6cyx` (`uid`),
  KEY `FK39ycbc46r1a9baw8au313s95y` (`handled_by_admin`),
  CONSTRAINT `FK39ycbc46r1a9baw8au313s95y` FOREIGN KEY (`handled_by_admin`) REFERENCES `admin` (`aid`),
  CONSTRAINT `FKn487hnkcnc8dm5mh8dind6cyx` FOREIGN KEY (`uid`) REFERENCES `users` (`uno`),
  CONSTRAINT `FKrsgwprv12344n1vpkippp5cwv` FOREIGN KEY (`handled_by`) REFERENCES `users` (`uno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content_reports`
--

LOCK TABLES `content_reports` WRITE;
/*!40000 ALTER TABLE `content_reports` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `event_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `content` tinytext NOT NULL,
  `end_at` date DEFAULT NULL,
  `event_status` enum('COMPLETED','ONGOING','SCHEDULED') NOT NULL,
  `is_top` bit(1) NOT NULL DEFAULT b'0',
  `modify_at` datetime(6) DEFAULT NULL,
  `start_at` date DEFAULT NULL,
  `thumbnail_url` varchar(512) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `views` int NOT NULL DEFAULT '0',
  `aid` varchar(50) NOT NULL,
  PRIMARY KEY (`event_id`),
  KEY `FKffrmgs51ftkhhw0ceb0rdm57j` (`aid`),
  CONSTRAINT `FKffrmgs51ftkhhw0ceb0rdm57j` FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'2025-04-20 20:02:50.390162','ㄹㅇㅇ',NULL,'ONGOING',_binary '','2025-04-24 12:00:32.407177',NULL,'','이벤트1',10,'admin001'),(3,'2025-04-20 20:03:27.574597','ㅇㄴㅇㅁㄴㄹ',NULL,'ONGOING',_binary '','2025-04-24 11:39:47.887290',NULL,'','이벤트ㅁㅈㄱㄹ1',35,'admin001'),(4,'2025-04-20 20:03:33.195515','ㅇㄴㅇㅁㄴㄹㄹ',NULL,'ONGOING',_binary '\0',NULL,NULL,NULL,'이벤트ㅁㅈㄱㄹ1',4,'admin001'),(5,'2025-04-20 20:03:57.433576','ㅇㄴㅇㅁㄴㄴㄹㄹㅗ',NULL,'ONGOING',_binary '\0','2025-04-21 18:43:49.069699',NULL,'','이ㅇㅈㄱㅇㄹㅇㄴㄹㄹ1',7,'admin001'),(6,'2025-04-20 20:04:18.887530','ㅇㄴㅇㄹㄴㅇ',NULL,'ONGOING',_binary '\0','2025-04-20 20:49:56.098749',NULL,NULL,'546546ㄹ1',7,'admin001'),(7,'2025-04-20 20:51:42.638771','<p>ㄷㅈㄱㅈㄷㅈㄱ</p>',NULL,'ONGOING',_binary '\0',NULL,NULL,NULL,'ㅈㄷㄱㅈ',1,'admin001'),(8,'2025-04-20 20:51:49.554811','<p>242323</p>',NULL,'ONGOING',_binary '\0',NULL,NULL,NULL,'23432',2,'admin001'),(9,'2025-04-20 20:51:57.839892','<p>234242</p>',NULL,'ONGOING',_binary '\0',NULL,NULL,NULL,'23423',0,'admin001'),(10,'2025-04-20 20:52:01.859572','<p>54654</p>','2025-04-24','ONGOING',_binary '\0','2025-04-20 20:52:35.694412','2025-04-15',NULL,'564564',3,'admin001'),(11,'2025-04-20 20:52:06.078280','<p>45646</p>',NULL,'ONGOING',_binary '\0',NULL,NULL,NULL,'546546',4,'admin001'),(12,'2025-04-20 20:52:17.076418','<p>546</p>',NULL,'ONGOING',_binary '\0',NULL,NULL,NULL,'456456',4,'admin001'),(13,'2025-04-20 21:20:23.292138','<p>ㄴㅇㄹㄴㅇㄹ<img src=\"/images/2025/04/20/3037773e-a6f5-4c98-96c4-609395cb0f30.png\" style=\"width: 255.994px;\"><img src=\"/images/2025/04/20/f362d3af-324b-4a64-9ba1-5bc6ae2d6978.jpeg\" style=\"width: 630px;\"></p>','2025-04-30','ONGOING',_binary '\0',NULL,'2025-04-14','/images/2025/04/20/3037773e-a6f5-4c98-96c4-609395cb0f30.png','ㅇㄹㄴ',1,'admin001'),(14,'2025-04-20 21:27:05.754996','<p><img src=\"/images/2025/04/20/14bd5ac4-d93f-474d-a573-f2708d668a5f.jpg\" style=\"width: 440px;\"><img src=\"/images/2025/04/20/e23d9dd7-a09c-423e-987f-03e136b55ad2.jpg\" style=\"width: 640px;\"><br></p>','2025-04-30','ONGOING',_binary '\0',NULL,'2025-04-15','/images/2025/04/20/14bd5ac4-d93f-474d-a573-f2708d668a5f.jpg','335',2,'admin001'),(21,'2025-04-23 09:14:05.736455','<p>1111</p>',NULL,'ONGOING',_binary '\0','2025-04-25 11:00:54.702581',NULL,'','12341543',3,'admin001'),(23,'2025-04-23 10:03:31.061513','<p>123123</p>',NULL,'ONGOING',_binary '\0','2025-04-23 10:14:10.223826',NULL,'','12312',2,'admin001'),(26,'2025-04-24 11:56:18.034567','<p>ㄱㄱㄱ</p>','2025-04-29','SCHEDULED',_binary '',NULL,'2025-04-26','','이게 이벤트다',4,'admin001'),(29,'2025-04-27 11:10:59.554729','내가 등록했엉 ㅎ',NULL,'ONGOING',_binary '','2025-04-27 15:13:25.314773',NULL,'','내가 등록함 ㅎ',1,'admin001'),(30,'2025-05-02 10:21:26.397772','이벤트 내용','2025-05-06','ONGOING',_binary '',NULL,'2025-05-02',NULL,'이벤트추가',0,'admin001');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faq`
--

DROP TABLE IF EXISTS `faq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faq` (
  `faq_id` bigint NOT NULL AUTO_INCREMENT,
  `answer` tinytext NOT NULL,
  `display_order` int DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `question` varchar(255) NOT NULL,
  `aid` varchar(50) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `modify_at` datetime(6) DEFAULT NULL,
  `category` enum('BOOKING','ETC','GENERAL','PAYMENT') NOT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`faq_id`),
  KEY `FK1ck853nm98i39h83hfbg4j60y` (`aid`),
  KEY `FK9ed0hfwvyse7c1w8u79hs8cyp` (`category_id`),
  CONSTRAINT `FK1ck853nm98i39h83hfbg4j60y` FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`),
  CONSTRAINT `FK9ed0hfwvyse7c1w8u79hs8cyp` FOREIGN KEY (`category_id`) REFERENCES `faq_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faq`
--

LOCK TABLES `faq` WRITE;
/*!40000 ALTER TABLE `faq` DISABLE KEYS */;
INSERT INTO `faq` VALUES (28,'<p>상품에 따라 대여가 포함되어 있습니다.</p>',1,_binary '\0','낚시 장비는 제공되나요?','admin001','2025-04-23 12:47:17.631642','2025-04-23 14:08:03.870056','BOOKING',2),(29,'상품 상세 정보에서 확인하실 수 있습니다',3,_binary '\0','출항 시간은 어디서 확인하나요?','admin001','2025-04-23 12:47:46.689363','2025-04-23 13:56:20.309631','BOOKING',2),(30,'<p>상세 페이지에서 날짜 선택 후 예약 버튼을 누르세요.</p>',4,_binary '','예약은 어떻게 하나요?','admin001','2025-04-23 12:48:00.517541','2025-04-23 13:56:54.978042','BOOKING',3),(31,'<p>페이지 새로고침 후 다시 시도해 주세요.</p>',4,_binary '','버튼이 안 눌려요.','admin001','2025-04-23 12:48:19.545719','2025-04-23 13:56:40.195231','BOOKING',4),(32,'<p>마이페이지 &gt; 이용내역에서 가능합니다.</p>',4,_binary '','후기는 어디서 작성하나요?','admin001','2025-04-23 12:48:42.382929','2025-04-23 13:56:31.179764','BOOKING',5),(33,'<p>신분증, 예약 문자 등을 준비해주세요.</p>',4,_binary '','현장에서는 무엇을 준비해야 하나요?','admin001','2025-04-23 12:48:54.279687','2025-04-23 13:56:28.496130','BOOKING',2),(34,'<p>1:1 문의 게시판을 이용해 주세요.</p>',4,_binary '\0','문의는 어디로 하나요?','admin001','2025-04-23 12:49:06.366464','2025-04-23 13:56:24.927703','BOOKING',5),(35,'<p>긴급 연락처는 예약 확정 후 안내됩니다.<img style=\"width: 630px;\" src=\"/images/2025/04/23/b5fb3cad-1a7b-4a4b-bc28-a589e1308366.jpeg\"></p>',4,_binary '\0','비상 상황 시 연락은 어떻게 하나요?','admin001','2025-04-23 12:49:34.031235','2025-04-23 13:56:37.361747','BOOKING',2),(36,'<p>카드, 계좌이체, 간편결제 모두 가능합니다.</p>',4,_binary '\0','결제 수단은 어떤 게 있나요?','admin001','2025-04-23 12:49:54.278363','2025-04-23 13:56:34.962651','BOOKING',3),(37,'<p>마이페이지 &gt; 예약 내역에서 가능합니다.</p>',4,_binary '','예약 후 변경은 어떻게 하나요?','admin001','2025-04-23 13:54:40.300417','2025-04-23 13:56:50.477368','BOOKING',3),(39,'<p>최신 브라우저 사용을 권장합니다</p>',2,_binary '','모바일에서 화면이 깨져요.','admin001','2025-04-23 13:55:47.963726','2025-04-23 13:56:09.882152','BOOKING',2);
/*!40000 ALTER TABLE `faq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faq_category`
--

DROP TABLE IF EXISTS `faq_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faq_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `display_order` int DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfn09klt00p3fb2ajtppyqjaxo` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faq_category`
--

LOCK TABLES `faq_category` WRITE;
/*!40000 ALTER TABLE `faq_category` DISABLE KEYS */;
INSERT INTO `faq_category` VALUES (2,1,'이용안내'),(3,2,'예약/결제'),(4,2,'버그신고'),(5,3,'기타문의');
/*!40000 ALTER TABLE `faq_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fishing_diary`
--

DROP TABLE IF EXISTS `fishing_diary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fishing_diary` (
  `fd_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `content` tinytext NOT NULL,
  `fishing_at` datetime(6) NOT NULL,
  `modify_at` datetime(6) DEFAULT NULL,
  `thumbnail_url` varchar(512) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `views` int NOT NULL DEFAULT '0',
  `prod_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  PRIMARY KEY (`fd_id`),
  KEY `FKhwyg24lr4em7pm3ylce71wwop` (`prod_id`),
  KEY `FKirenqmbruyfn2n2i3xkffu78q` (`uid`),
  CONSTRAINT `FKhwyg24lr4em7pm3ylce71wwop` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`),
  CONSTRAINT `FKirenqmbruyfn2n2i3xkffu78q` FOREIGN KEY (`uid`) REFERENCES `users` (`uno`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fishing_diary`
--

LOCK TABLES `fishing_diary` WRITE;
/*!40000 ALTER TABLE `fishing_diary` DISABLE KEYS */;
INSERT INTO `fishing_diary` VALUES (1,'2025-04-24 16:29:16.410064','ㅂㅂ','2025-05-01 16:29:00.000000',NULL,'ㅂ','ㅂ',0,25,1),(2,'2025-04-24 16:39:00.255221','ㅁㄴ','2025-04-24 16:38:00.000000',NULL,'ㅁㄴ','ㅁ',0,25,1),(3,'2025-04-24 16:39:08.305602','ㅁ','2025-04-24 16:39:00.000000',NULL,'ㅁ','ㅁ',0,25,1),(4,'2025-04-24 17:08:54.252153','11','2025-04-24 16:40:00.000000',NULL,'11','11',0,25,1),(5,'2025-04-24 17:46:53.786411','테스트','2025-04-24 17:46:00.000000',NULL,'테스트','테스트',0,1,1),(6,'2025-04-24 17:48:38.654016','조행기 테스트입니다','2025-04-24 17:48:00.000000',NULL,'조행기 테스트입니다','조행기 테스트입니다',0,27,1),(7,'2025-04-24 18:03:11.120934','조행기 테스트','2025-04-24 18:03:00.000000',NULL,'테스트','조행기 테스트',0,24,1),(8,'2025-04-24 18:03:26.706296','조행기 테스트','2025-04-24 18:03:00.000000',NULL,'조행기 테스트','조행기 테스트',0,26,1),(9,'2025-04-25 17:56:56.172023','유저 조행기 테스트','2025-04-25 17:56:00.000000',NULL,'유저 조행기 테스트','유저 조행기 테스트',0,1,1),(10,'2025-04-28 10:36:53.399229','jj','2025-04-28 10:36:00.000000',NULL,'jj','jj',0,24,1),(11,'2025-04-28 12:06:07.040754','test','2025-04-28 12:06:00.000000',NULL,'test','test',0,24,1);
/*!40000 ALTER TABLE `fishing_diary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fishing_diary_comment`
--

DROP TABLE IF EXISTS `fishing_diary_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fishing_diary_comment` (
  `fd_comment_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `comment_content` tinytext NOT NULL,
  `modify_at` datetime(6) DEFAULT NULL,
  `fd_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  PRIMARY KEY (`fd_comment_id`),
  KEY `FKepou3lac32qdxa86aqgwe2wqs` (`fd_id`),
  KEY `FK3npj06pnk6nyd92g3qjndh9dx` (`uid`),
  CONSTRAINT `FK3npj06pnk6nyd92g3qjndh9dx` FOREIGN KEY (`uid`) REFERENCES `users` (`uno`),
  CONSTRAINT `FKepou3lac32qdxa86aqgwe2wqs` FOREIGN KEY (`fd_id`) REFERENCES `fishing_diary` (`fd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fishing_diary_comment`
--

LOCK TABLES `fishing_diary_comment` WRITE;
/*!40000 ALTER TABLE `fishing_diary_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `fishing_diary_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fishing_report`
--

DROP TABLE IF EXISTS `fishing_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fishing_report` (
  `fr_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `content` tinytext NOT NULL,
  `fishing_at` datetime(6) NOT NULL,
  `modify_at` datetime(6) DEFAULT NULL,
  `thumbnail_url` varchar(512) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `views` int NOT NULL DEFAULT '0',
  `prod_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  PRIMARY KEY (`fr_id`),
  KEY `FK1upu1e6sfd2lhvxymu9pxf34x` (`prod_id`),
  KEY `FKin40vsrqnasw83p7w4l5a4r5w` (`uid`),
  CONSTRAINT `FK1upu1e6sfd2lhvxymu9pxf34x` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`),
  CONSTRAINT `FKin40vsrqnasw83p7w4l5a4r5w` FOREIGN KEY (`uid`) REFERENCES `users` (`uno`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fishing_report`
--

LOCK TABLES `fishing_report` WRITE;
/*!40000 ALTER TABLE `fishing_report` DISABLE KEYS */;
INSERT INTO `fishing_report` VALUES (1,'2025-04-20 17:57:28.000000','test','2025-04-20 17:57:35.000000','2025-04-20 17:57:40.000000',NULL,'test',0,1,1),(25,'2025-04-23 09:45:58.484384','tst','2025-04-23 09:45:00.000000',NULL,'ㅅㄷㄴㅅ','rtet',0,1,1),(26,'2025-04-23 09:49:47.537384','sest','2025-04-23 09:49:00.000000',NULL,'tste','set',1,1,1),(27,'2025-04-23 10:36:02.057154','ㅁㅇㄴㅁㄴ','2025-04-23 10:33:00.000000',NULL,'\"C:\\Users\\edu\\Downloads\\food-1239231_640.jpg\"','ㅂㅂ',0,1,1),(28,'2025-04-23 16:25:11.437189','ㅈㅂㄷㄷㅂㅈ','2025-04-23 16:25:00.000000',NULL,'ㅂㅈㅈㄷ','ㅂㅈㄷ',0,1,1),(29,'2025-04-24 09:24:46.976153','test','2025-04-24 09:24:00.000000',NULL,'test','test',0,1,1),(30,'2025-04-24 09:26:35.372910','ㅂ','2025-04-24 09:26:00.000000',NULL,'ㅂ','ㅂ',0,1,1),(31,'2025-04-24 09:29:21.414866','ㅅㄷㄴㅅ','2025-04-24 09:29:00.000000',NULL,'ㅅ','ㅅㄷㄴㅅ',0,1,1),(32,'2025-04-24 09:30:16.832299','ㅅㄴ','2025-04-24 09:30:00.000000',NULL,'ㅅㄷㄴㅅ','ㄷㅅㄴㅅ',0,1,1),(33,'2025-04-24 09:56:57.887827','ㅅㄷㄴㅅ','2025-04-24 09:56:00.000000',NULL,'ㅅㄷㄴㅅ','ㅅㄷㄴㅅ',0,1,1),(34,'2025-04-24 10:05:25.893881','ㅅㄷㄴ','2025-04-24 10:05:00.000000',NULL,'ㅅㅅ','ㅅㄷㄴ',0,1,1),(35,'2025-04-24 10:06:26.272817','ㅅㄴㅅㄴㄷㅅㄴㄷ','2025-04-24 10:06:00.000000',NULL,'ㅅㄷㄴ','ㅅㄴㄷ',0,1,1),(36,'2025-04-24 10:07:42.360719','ㅅㅅ','2025-04-24 10:07:00.000000',NULL,'ㅅ','ㅅ',0,1,1),(37,'2025-04-24 10:11:19.983013','ㅅㅅ','2025-04-24 10:11:00.000000',NULL,'ㅅ','ㅅ',0,1,1),(38,'2025-04-24 10:17:24.899757','ㅅㄷ','2025-04-24 10:17:00.000000',NULL,'ㄷㅅ','ㅅㄷ',0,1,1),(39,'2025-04-24 10:17:38.874796','ㅅㄷ','2025-04-24 10:17:00.000000',NULL,'ㅅㄷ','ㅅㄷ',0,1,1),(40,'2025-04-24 10:18:22.173621','11','2025-04-24 10:17:00.000000',NULL,'11','11',0,1,1),(41,'2025-04-24 10:19:31.176342','112','2025-04-24 10:17:00.000000',NULL,'112','112',0,1,1),(42,'2025-04-24 10:21:44.640636','22','2025-04-24 10:21:00.000000',NULL,'22','22',0,1,1),(43,'2025-04-24 10:23:55.932272','3','2025-04-24 10:23:00.000000',NULL,'3','3',0,1,1),(44,'2025-04-24 10:30:25.438668','ㅅㅅ','2025-04-24 10:30:00.000000',NULL,'ㅅ','ㅅ',0,1,1),(45,'2025-04-24 11:09:17.434672','ㅂㅂ','2025-04-24 11:09:00.000000',NULL,'ㅂㅂ','ㅂㅂ',0,1,1),(46,'2025-04-24 11:21:52.661431','123','2025-04-24 11:21:00.000000',NULL,'123','123',0,1,1),(47,'2025-04-24 11:24:04.785314','ㄷㅅ','2025-04-24 11:24:00.000000',NULL,'ㅅㄷ','ㅅㄷ',0,1,1),(48,'2025-04-24 11:26:18.503702','ㅅㅅ','2025-04-24 11:26:00.000000',NULL,'ㅅㅅ','ㅅ',0,1,1),(49,'2025-04-24 11:27:33.048280','333','2025-04-24 11:27:00.000000',NULL,'33','33',0,1,1),(50,'2025-04-24 11:35:55.874924','ㅈㅈㅈㅈㅈ','2025-04-24 11:35:00.000000',NULL,'ㅂㅈ','ㅂㅈ',0,1,1),(51,'2025-04-24 11:50:39.423165','ㄷㅂㅈㄷ','2025-04-24 11:50:00.000000',NULL,'ㅂㅈ','ㅂㅈㄷ',0,1,1),(52,'2025-04-24 11:53:05.675271','ㅂㅈ','2025-04-24 11:53:00.000000',NULL,'ㅂㅈ','ㅂㅈ',0,1,1),(53,'2025-04-24 12:00:02.985495','ㅂ','2025-04-24 12:00:00.000000',NULL,'ㅂㅂ','ㅂ',0,1,1),(54,'2025-04-24 12:00:03.056765','ㅂ','2025-04-24 12:00:00.000000',NULL,'ㅂㅂ','ㅂ',0,1,1),(55,'2025-04-24 12:24:53.054399','fsdfsf','2025-04-24 12:24:00.000000',NULL,'dsfsdf','sdfsd',1,1,1),(56,'2025-04-24 12:27:30.551597','fsdfsf','2025-04-24 12:24:00.000000',NULL,'dsfsdf','sdfsd',1,1,1),(57,'2025-04-24 12:28:40.536446','fsdfsf','2025-04-24 12:24:00.000000',NULL,'dsfsdf','sdfsd',1,1,1),(58,'2025-04-24 12:36:31.411121','sdfsdfsdf11','2025-04-25 12:36:00.000000',NULL,'dfsdf1111','dsfsdf1111',1,25,1),(59,'2025-04-24 12:40:08.948222','2222','2025-04-25 12:39:00.000000',NULL,'fdfsdfsdf22222','2222',11,1,1),(60,'2025-04-24 12:44:30.961847','3333','2025-04-26 12:43:00.000000',NULL,'3333333','3333333',0,25,1),(61,'2025-04-24 12:45:03.142203','qw','2025-04-24 12:45:00.000000',NULL,'qw','qw',0,26,1),(62,'2025-04-24 14:32:21.536580','ㅁ','2025-04-24 14:32:00.000000',NULL,'ㅁ','ㅁ',0,25,1),(63,'2025-04-24 14:55:15.274000','ㅂㅂ','2025-04-24 14:55:00.000000',NULL,'ㅂ','ㅂ',0,25,1),(64,'2025-04-24 15:41:08.624462','qwe','2025-04-24 15:41:00.000000',NULL,'qwe','qwe',0,1,1),(65,'2025-04-24 15:49:32.714563','ㅂㅈ','2025-04-24 15:49:00.000000',NULL,'ㅂㅈ','ㅂㅈ',0,26,1),(66,'2025-04-24 17:09:59.166287','ㅂㅂ','2025-04-24 17:09:00.000000',NULL,'ㅂㅂ','ㅂㅂ',0,25,1),(67,'2025-04-24 17:48:22.803634','테스트입니다','2025-04-24 17:48:00.000000',NULL,'테스트입니다','테스트입니다',0,27,1),(68,'2025-04-25 09:03:36.496965','test','2025-04-25 09:03:00.000000',NULL,'test','test',0,26,1),(69,'2025-04-25 12:50:21.345052','ㅅㄷㄴㅅ','2025-04-25 12:50:00.000000',NULL,'ㄴㅅㅅ','ㄷㅅㄴㅅ',0,27,1),(70,'2025-04-28 10:36:38.259911','ddd','2025-04-28 10:36:00.000000',NULL,'dd','tt',0,24,1),(71,'2025-04-28 12:05:50.368442','test','2025-04-28 12:05:00.000000',NULL,'test','test',0,24,1);
/*!40000 ALTER TABLE `fishing_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fishing_report_comments`
--

DROP TABLE IF EXISTS `fishing_report_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fishing_report_comments` (
  `fr_comment_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `comment_content` tinytext NOT NULL,
  `modify_at` datetime(6) DEFAULT NULL,
  `fr_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  PRIMARY KEY (`fr_comment_id`),
  KEY `FKaiju77od5t8sydj7yrldot29l` (`fr_id`),
  KEY `FKppu2c2qrx39n7lopj2mrdo9tq` (`uid`),
  CONSTRAINT `FKaiju77od5t8sydj7yrldot29l` FOREIGN KEY (`fr_id`) REFERENCES `fishing_report` (`fr_id`),
  CONSTRAINT `FKppu2c2qrx39n7lopj2mrdo9tq` FOREIGN KEY (`uid`) REFERENCES `users` (`uno`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fishing_report_comments`
--

LOCK TABLES `fishing_report_comments` WRITE;
/*!40000 ALTER TABLE `fishing_report_comments` DISABLE KEYS */;
INSERT INTO `fishing_report_comments` VALUES (1,'2025-04-23 10:29:12.000000','테스트입니다',NULL,1,1);
/*!40000 ALTER TABLE `fishing_report_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `free_board`
--

DROP TABLE IF EXISTS `free_board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `free_board` (
  `fb_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `content` tinytext NOT NULL,
  `modify_at` datetime(6) DEFAULT NULL,
  `thumbnail_url` varchar(512) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `views` int NOT NULL DEFAULT '0',
  `uid` bigint NOT NULL,
  PRIMARY KEY (`fb_id`),
  KEY `FKrqtvo4ohyojy7adm6qi473ddw` (`uid`),
  CONSTRAINT `FKrqtvo4ohyojy7adm6qi473ddw` FOREIGN KEY (`uid`) REFERENCES `users` (`uno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `free_board`
--

LOCK TABLES `free_board` WRITE;
/*!40000 ALTER TABLE `free_board` DISABLE KEYS */;
/*!40000 ALTER TABLE `free_board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `freeboard_comments`
--

DROP TABLE IF EXISTS `freeboard_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `freeboard_comments` (
  `fb_comment_id` bigint NOT NULL AUTO_INCREMENT,
  `comment_content` varchar(255) DEFAULT NULL,
  `modify_at` datetime(6) DEFAULT NULL,
  `fb_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  PRIMARY KEY (`fb_comment_id`),
  KEY `FKgi695m993m82lc5sngtyfnhdw` (`fb_id`),
  KEY `FKheb4ryo3a10od7lxa2j2tirck` (`uid`),
  CONSTRAINT `FKgi695m993m82lc5sngtyfnhdw` FOREIGN KEY (`fb_id`) REFERENCES `free_board` (`fb_id`),
  CONSTRAINT `FKheb4ryo3a10od7lxa2j2tirck` FOREIGN KEY (`uid`) REFERENCES `users` (`uno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `freeboard_comments`
--

LOCK TABLES `freeboard_comments` WRITE;
/*!40000 ALTER TABLE `freeboard_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `freeboard_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inquiry`
--

DROP TABLE IF EXISTS `inquiry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inquiry` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contact` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `question_type` enum('BUSINESS','CANCEL','PRODUCT','RESERVATION','SYSTEM') NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `writer` varchar(255) DEFAULT NULL,
  `user_type` enum('ADMIN','PARTNER','USER') NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `receiver_type` enum('ADMIN','PARTNER') NOT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inquiry`
--

LOCK TABLES `inquiry` WRITE;
/*!40000 ALTER TABLE `inquiry` DISABLE KEYS */;
INSERT INTO `inquiry` VALUES (18,'01000000000','ㅎ로ㅓㅏ','BUSINESS','ㅇㄴㄹㅇㅎ','kjs','PARTNER','2025-04-24 10:30:07.819153','2025-04-24 10:30:07.819153',NULL,'ADMIN',NULL,NULL),(19,'01000000000','fagr','BUSINESS','agea','kjs','PARTNER','2025-04-24 14:07:57.493319','2025-04-24 14:07:57.493319',NULL,'ADMIN',NULL,NULL),(20,'01000000000','asdfghjkl','PRODUCT','asdfgjkl','kjs','USER','2025-04-24 14:10:50.815128','2025-04-24 14:10:50.815128',NULL,'PARTNER',NULL,NULL),(21,'01000000000','ㅁㄱㄷ쇼ㅑㅕㅛㅅ','BUSINESS','노ㅓㅗㅛㄹㄷㄱ쇼','kjs','PARTNER','2025-04-25 14:52:19.686024','2025-04-25 14:52:19.686024',NULL,'ADMIN',NULL,NULL),(22,'01000000000','vjhfjyh','BUSINESS','vhmvjhv','dfdfdf','PARTNER','2025-04-25 16:39:48.550531','2025-04-25 16:39:48.550531',NULL,'ADMIN',NULL,NULL),(23,'01000000000','kk','SYSTEM','kk','kjs','USER','2025-04-28 10:44:55.777048','2025-04-28 10:44:55.777048',NULL,'ADMIN',NULL,NULL),(24,'0100000000','aaa','BUSINESS','aaa','kjs','PARTNER','2025-04-28 12:12:27.478339','2025-04-28 12:12:27.478339',NULL,'ADMIN',NULL,NULL),(25,'01055153120','test','SYSTEM','test','kjs','USER','2025-04-29 16:47:04.501228','2025-04-29 16:47:04.501228',NULL,'ADMIN',NULL,NULL);
/*!40000 ALTER TABLE `inquiry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kakao_pay`
--

DROP TABLE IF EXISTS `kakao_pay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kakao_pay` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `tid` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `next_redirect_pc_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kakao_pay`
--

LOCK TABLES `kakao_pay` WRITE;
/*!40000 ALTER TABLE `kakao_pay` DISABLE KEYS */;
/*!40000 ALTER TABLE `kakao_pay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kakaopay`
--

DROP TABLE IF EXISTS `kakaopay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kakaopay` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `next_redirect_pc_url` varchar(255) DEFAULT NULL,
  `tid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kakaopay`
--

LOCK TABLES `kakaopay` WRITE;
/*!40000 ALTER TABLE `kakaopay` DISABLE KEYS */;
/*!40000 ALTER TABLE `kakaopay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `notice_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `content` tinytext NOT NULL,
  `is_top` bit(1) NOT NULL DEFAULT b'0',
  `modify_at` datetime(6) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `views` int NOT NULL DEFAULT '0',
  `aid` varchar(50) NOT NULL,
  `thumbnail_url` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`notice_id`),
  KEY `FKeqiwc4rv1tq6xs30s1hi2krcl` (`aid`),
  CONSTRAINT `FKeqiwc4rv1tq6xs30s1hi2krcl` FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,'2025-04-18 11:11:06.015420','수정한 내용ㅇ',_binary '\0','2025-04-21 18:09:16.339832','공지사항 수정',5,'admin001',NULL),(3,'2025-04-18 11:22:33.424294','공지사항 테스트 내용',_binary '',NULL,'공지사항 테스트',17,'admin001',NULL),(4,'2025-04-18 15:39:13.777610','<p>공지입니다.</p><img src=\"/images/2025/04/18/8ee95a64-a215-47bd-b576-3c1cfbafd6b8.png\">',_binary '',NULL,'이미지 테스트 공지',9,'admin001',NULL),(5,'2025-04-18 15:59:58.958465','<p>에디터연결했다 이미지 봐라<img src=\"/images/2025/04/18/c1c39f7c-6cba-437b-972a-04caaf5e7239.jpg\" style=\"width: 440px;\"></p>',_binary '\0',NULL,'공지사항 에디터 테스트',5,'admin001',NULL),(18,'2025-04-18 18:43:33.772964','<p>ㄹ홀홀홍ㄹ호홓ㄹ<img style=\"width: 255.994px;\" src=\"/images/2025/04/18/b96de039-32b0-459e-a247-f5729589e511.png\"></p>',_binary '','2025-04-24 14:35:39.945012','테스ㅔ트1',6,'admin001',NULL),(21,'2025-04-19 19:34:42.416452','<h3 class=\"\">테스트ㅇㄹ</h3>',_binary '\0','2025-04-24 09:06:00.031807','공지테스트ㅇㄴㄹㅇ',6,'admin001',NULL),(22,'2025-04-19 19:34:49.570743','<p>ㄴㄴㄴ</p>',_binary '\0',NULL,'ㄴㄴㄴ',0,'admin001',NULL),(23,'2025-04-19 19:34:53.879173','<p>ㄷㄷㄷ</p>',_binary '\0',NULL,'ㄷㄷㄷㄷ',1,'admin001',NULL),(24,'2025-04-19 19:36:53.650554','<p>ㅇㅇㅇ</p>',_binary '\0',NULL,'ㅇㅇ',2,'admin001',NULL),(25,'2025-04-19 19:39:38.145786','<p>ㄴㅇㄹㄴㅇ</p>',_binary '\0',NULL,'ㅇㄹㄴㅇ',2,'admin001',NULL),(26,'2025-04-19 19:39:43.630800','<p>ㄴㅇㄹㄴㅇ</p>',_binary '\0',NULL,'ㄹㄴㅇㄹㄴㅇㄹ',10,'admin001',NULL),(27,'2025-04-19 19:39:48.022395','<p>ㄻㄴㅇㄻㄴㅇㄻ</p>',_binary '\0',NULL,'ㅁㅇㄻㄴㅇ',6,'admin001',NULL),(28,'2025-04-19 19:39:53.282636','<p>ㅁㄴㅇㄻㄴㄻㄴ</p>',_binary '\0','2025-04-23 14:06:28.149504','ㅁㄴㅇㄻㄴㅇㄹ',2,'admin001',NULL),(31,'2025-04-20 19:39:23.870396','<p>ㅁㅁㅁ</p>',_binary '',NULL,'ㅁㅁㅁ',3,'admin001',NULL),(33,'2025-04-23 19:51:31.058091','이애애애애애',_binary '\0','2025-04-23 19:53:23.345242','제발',1,'jang',NULL),(34,'2025-04-23 19:52:04.764094','ㄴ으,ㄹ',_binary '',NULL,'로그인됐나',2,'admin001',NULL),(35,'2025-04-24 14:36:46.301823','ㅎ',_binary '',NULL,'ㅎ',0,'jang',NULL),(36,'2025-04-24 14:48:47.050418','<p>ㄹ</p>',_binary '\0',NULL,'ㄹ',0,'admin001',NULL),(37,'2025-04-24 17:15:43.433956','<p>fsdfsdfsadf</p>',_binary '\0',NULL,'fsd',0,'admin001',NULL),(38,'2025-04-24 17:16:37.449836','string',_binary '',NULL,'string',0,'jang',NULL),(39,'2025-04-24 17:17:01.356301','string',_binary '',NULL,'string',0,'jang',NULL),(40,'2025-04-24 17:19:53.487777','string',_binary '',NULL,'string',0,'jang',NULL),(41,'2025-04-24 17:23:13.548260','string',_binary '',NULL,'string',0,'jang',NULL),(43,'2025-04-24 17:23:50.140064','string',_binary '',NULL,'string',2,'jang',NULL),(45,'2025-04-24 17:54:51.303150','ㅁㄴㅇㅁㄴㅇ',_binary '',NULL,'ㅁㄴㅇㅁㄴ',0,'jang',NULL),(46,'2025-04-24 18:17:29.802517','string11',_binary '','2025-04-27 11:05:56.296094','string',5,'jang',NULL),(48,'2025-04-24 21:24:48.056394','<p>ㄹㅇ</p>',_binary '\0',NULL,'ㅇㄹ',0,'admin001',NULL),(50,'2025-04-26 19:29:09.483463','<p>a</p>',_binary '\0',NULL,'a',3,'admin001',NULL);
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_read` bit(1) NOT NULL,
  `receiver_id` bigint DEFAULT NULL,
  `sender_id` bigint DEFAULT NULL,
  `sender_type` enum('ADMIN','PARTNER','SYSTEM') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `reservation_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdammjl0v5xfaegi926ugx6254` (`receiver_id`),
  KEY `FKrg0atx075rr68et2rqrh34qwj` (`sender_id`),
  KEY `FKs24nj4175mp37khlffo484eok` (`reservation_id`),
  CONSTRAINT `FKdammjl0v5xfaegi926ugx6254` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`uno`),
  CONSTRAINT `FKrg0atx075rr68et2rqrh34qwj` FOREIGN KEY (`sender_id`) REFERENCES `users` (`uno`),
  CONSTRAINT `FKs24nj4175mp37khlffo484eok` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'이것은 새로운 공지사항입니다.','2025-04-17 12:20:31.535665',_binary '',10,1,'ADMIN','새로운 공지사항','NOTICE',NULL),(2,'이것은 새로운 공지사항입니다.','2025-04-17 12:32:47.311374',_binary '',11,1,'ADMIN','새로운 공지사항','NOTICE',NULL),(3,'예약입니다','2025-04-17 14:09:30.013739',_binary '\0',12,NULL,'SYSTEM','예약','RESERVATION',NULL),(4,'예약정보','2025-04-17 14:34:50.176082',_binary '\0',10,NULL,'ADMIN','예약','RESERVATION',1),(7,'예약입니다','2025-04-17 15:54:54.092191',_binary '\0',21,NULL,'SYSTEM','예약','RESERVATION',1),(9,'공지사항','2025-04-17 16:05:32.522450',_binary '',21,1,'PARTNER','공지사항','NOTICE',NULL),(10,'예약함','2025-04-17 16:28:47.856484',_binary '',20,NULL,'SYSTEM','약예','RESERVATION',1),(12,'ㅇㅁㄴㅇㄴㅇ','2025-04-17 16:54:04.462520',_binary '\0',21,NULL,'SYSTEM','몰ㄹ','RESERVATION',1),(14,'알람이다','2025-04-23 09:49:07.980188',_binary '\0',21,NULL,'ADMIN','알람이니라','NOTICE',NULL),(15,'ㅁㄴㅇ','2025-04-23 09:49:53.789184',_binary '\0',1,NULL,'ADMIN','전부다','NOTICE',NULL),(16,'ㅁㄴㅇ','2025-04-23 09:49:53.801707',_binary '\0',10,NULL,'ADMIN','전부다','NOTICE',NULL),(17,'ㅁㄴㅇ','2025-04-23 09:49:53.810967',_binary '\0',11,NULL,'ADMIN','전부다','NOTICE',NULL),(18,'ㅁㄴㅇ','2025-04-23 09:49:53.818922',_binary '\0',12,NULL,'ADMIN','전부다','NOTICE',NULL),(19,'ㅁㄴㅇ','2025-04-23 09:49:53.827146',_binary '\0',14,NULL,'ADMIN','전부다','NOTICE',NULL),(22,'ㅁㄴㅇ','2025-04-23 09:49:53.857752',_binary '\0',18,NULL,'ADMIN','전부다','NOTICE',NULL),(23,'ㅁㄴㅇ','2025-04-23 09:49:53.865675',_binary '\0',20,NULL,'ADMIN','전부다','NOTICE',NULL),(24,'ㅁㄴㅇ','2025-04-23 09:49:53.877653',_binary '',21,NULL,'ADMIN','전부다','NOTICE',NULL),(28,'그건 못해줌','2025-04-23 11:24:37.722396',_binary '\0',14,NULL,'ADMIN','답변','REPLY',NULL),(29,'ㅁㄴㅇㅁㄴ','2025-04-23 11:25:34.491607',_binary '\0',14,NULL,'ADMIN','제먹','REPLY',NULL),(30,'sadasd','2025-04-24 16:10:44.465632',_binary '',21,NULL,'ADMIN','dasd','NOTICE',NULL),(31,'에약함','2025-05-02 16:18:29.783386',_binary '\0',21,NULL,'SYSTEM','예약임','RESERVATION',1),(34,'사항','2025-05-02 16:23:34.568680',_binary '\0',21,NULL,'ADMIN','공지','NOTICE',NULL),(35,'사항','2025-05-02 16:23:39.875543',_binary '\0',21,NULL,'ADMIN','공지','NOTICE',NULL),(36,'에약함','2025-05-02 16:24:33.381592',_binary '\0',21,NULL,'SYSTEM','예약임','RESERVATION',1),(37,'사항','2025-05-02 16:24:47.892524',_binary '\0',21,NULL,'SYSTEM','공지','NOTICE',NULL),(38,'사항','2025-05-02 16:25:01.726293',_binary '\0',21,NULL,'ADMIN','공지','NOTICE',NULL),(39,'3123','2025-05-07 16:43:44.407999',_binary '\0',27,NULL,'ADMIN','d','NOTICE',NULL);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partner_applications`
--

DROP TABLE IF EXISTS `partner_applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partner_applications` (
  `pid` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `ceo_name` varchar(50) DEFAULT NULL,
  `license` varchar(30) DEFAULT NULL,
  `p_address` varchar(255) NOT NULL,
  `p_info` tinytext,
  `p_rejection_reason` tinytext,
  `p_reviewed_at` datetime(6) DEFAULT NULL,
  `p_status` enum('APPROVED','PENDING','REJECTED') NOT NULL,
  `pname` varchar(50) NOT NULL,
  `uno` bigint NOT NULL,
  PRIMARY KEY (`pid`),
  KEY `FKqf3nm5w9qxdxttqt8iw4fye9n` (`uno`),
  CONSTRAINT `FKqf3nm5w9qxdxttqt8iw4fye9n` FOREIGN KEY (`uno`) REFERENCES `users` (`uno`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partner_applications`
--

LOCK TABLES `partner_applications` WRITE;
/*!40000 ALTER TABLE `partner_applications` DISABLE KEYS */;
INSERT INTO `partner_applications` VALUES (8,'2025-04-15 10:31:13.515783','나나나나','1234567890','가가가가가가','가가가가가가가가가가가가가가가가','dasdas','2025-04-15 13:13:07.755043','REJECTED','가가가가가',11),(9,'2025-04-15 10:32:50.746076','나나나나','1234567890','가가가가가가','가가가가가가가가가가가가가가가가',NULL,'2025-04-15 14:42:13.469572','APPROVED','가가가가가',11),(10,'2025-04-15 12:00:44.730880','기기','1234512312','도도도도도','가고가가고고고기기기기기','이유가 마음에 안듬','2025-04-15 14:44:29.198725','REJECTED','다다다다',11),(11,'2025-04-15 16:39:46.257226','다다다다다','1234567890','나나나나나','dfsdfasdfsdfasdfsdafsdafsdafsdafsd',NULL,NULL,'PENDING','가가가가가',14),(12,'2025-04-15 16:40:27.833348','다다다다다','1234567890','나나나나나','dfsdfasdfsdfasdfsdafsdafsdafsdafsd',NULL,'2025-04-22 15:50:34.463901','APPROVED','가가가가가',14),(13,'2025-04-15 16:46:25.319471','다다다다','1234567890','나나나나나','가가가가가가가가가가가가.',NULL,'2025-04-23 09:20:26.876596','APPROVED','가가가가가',14),(14,'2025-04-15 16:47:49.953881','다다다다','1234567890','나나나나나','xvxcvxcvdfasdfsdfsdfsdfsdfsdf',NULL,'2025-04-15 17:02:20.471843','APPROVED','가가가가가',14),(15,'2025-04-15 16:49:58.842061','다다','0123456780','나나나나나나나나난','ddddddddddddddddddddddddddddddddddddddddd',NULL,'2025-04-22 15:47:59.177129','APPROVED','가가가가가가가',14),(16,'2025-04-15 16:50:37.044028','다다','0123456780','나나나나나나나나난','ddddddddddddddddddddddddddddddddddddddddd','반려좀해줘 제발','2025-04-22 16:12:05.429419','REJECTED','가가가가가가가',14),(17,'2025-04-15 16:51:59.872327','다다','0123456780','나나나나나나나나난','ddddddddddddddddddddddddddddddddddddddddd',NULL,'2025-04-23 09:03:21.069803','APPROVED','가가가가가가가',14),(18,'2025-04-15 18:04:01.766780','다다','0123456780','나나나나나나나나난','ddddddddddddddddddddddddddddddddddddddddd',NULL,NULL,'PENDING','가가가가가가가',14),(19,'2025-04-15 18:10:03.497737','다다','0123456780','나나나나나나나나난','ddddddddddddddddddddddddddddddddddddddddd',NULL,NULL,'PENDING','가가가가가가가',14),(20,'2025-04-16 09:16:50.875406','다다','0123456780','나나나나나나나나난','ddddddddddddddddddddddddddddddddddddddddd',NULL,NULL,'PENDING','가가가가가가가',14);
/*!40000 ALTER TABLE `partner_applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partners`
--

DROP TABLE IF EXISTS `partners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partners` (
  `uno` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `ceo_name` varchar(50) NOT NULL,
  `license` varchar(30) DEFAULT NULL,
  `license_img` varchar(255) DEFAULT NULL,
  `p_address` varchar(255) NOT NULL,
  `p_info` tinytext,
  `pname` varchar(50) NOT NULL,
  `version` int DEFAULT NULL,
  PRIMARY KEY (`uno`),
  CONSTRAINT `FKnrq8t9tegwfcnckigf93krg58` FOREIGN KEY (`uno`) REFERENCES `users` (`uno`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partners`
--

LOCK TABLES `partners` WRITE;
/*!40000 ALTER TABLE `partners` DISABLE KEYS */;
INSERT INTO `partners` VALUES (10,'2025-04-14 15:50:06.000000','string','string','string','string','string','string',2),(11,'2025-04-15 14:42:13.480550','test','test','test','test','test','test',2),(12,'2025-04-15 15:34:05.000000','강감찬','1234567892',NULL,'부산','테스트','강감찬',NULL),(14,'2025-04-22 15:47:59.185108','다다다다','1234567890',NULL,'나나나나나','가가가가가가가가가가가가.','가가가가가',3);
/*!40000 ALTER TABLE `partners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `buyer_name` varchar(255) DEFAULT NULL,
  `imp_uid` varchar(255) DEFAULT NULL,
  `merchant_uid` varchar(255) DEFAULT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `pay_method` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (2,100000.00,'강감찬','imp_490440729926','ORD-1744855442244','2025-05-07 07:10:26.722000','카카오페이','y'),(3,100.00,'홍길동','imp_936059242256','ORD-1744867060241','2025-04-17 14:18:22.000000','point','paid'),(4,100.00,'홍길동','imp_615863912868','ORD-1744871864889','2025-04-17 15:38:32.000000','point','paid');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_facility`
--

DROP TABLE IF EXISTS `prod_facility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prod_facility` (
  `fa_id` bigint NOT NULL AUTO_INCREMENT,
  `fa_icon_url` varchar(500) DEFAULT NULL,
  `fa_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`fa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_facility`
--

LOCK TABLES `prod_facility` WRITE;
/*!40000 ALTER TABLE `prod_facility` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_facility` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_facility_mapping`
--

DROP TABLE IF EXISTS `prod_facility_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prod_facility_mapping` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fa_id` bigint NOT NULL,
  `prod_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5wregwqmb9gtb7a55uajhujdu` (`fa_id`),
  KEY `FK99td3axasnt2ipcev7ia17wa4` (`prod_id`),
  CONSTRAINT `FK5wregwqmb9gtb7a55uajhujdu` FOREIGN KEY (`fa_id`) REFERENCES `prod_facility` (`fa_id`),
  CONSTRAINT `FK99td3axasnt2ipcev7ia17wa4` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_facility_mapping`
--

LOCK TABLES `prod_facility_mapping` WRITE;
/*!40000 ALTER TABLE `prod_facility_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_facility_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_fish_species`
--

DROP TABLE IF EXISTS `prod_fish_species`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prod_fish_species` (
  `fs_id` bigint NOT NULL AUTO_INCREMENT,
  `fs_icon_url` varchar(500) DEFAULT NULL,
  `fs_name` varchar(255) DEFAULT NULL,
  `main_type` enum('FRESHWATER','SEA') DEFAULT NULL,
  PRIMARY KEY (`fs_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_fish_species`
--

LOCK TABLES `prod_fish_species` WRITE;
/*!40000 ALTER TABLE `prod_fish_species` DISABLE KEYS */;
INSERT INTO `prod_fish_species` VALUES (1,NULL,'가숭어','SEA'),(2,NULL,'갈치(내만)','SEA'),(3,NULL,'갈치(먼바다)','SEA'),(4,NULL,'감성돔','SEA'),(5,NULL,'갑오징어','SEA'),(6,NULL,'고등어','SEA'),(7,NULL,'광어','SEA'),(8,NULL,'낙지','SEA'),(9,NULL,'농어','SEA'),(10,NULL,'능성어','SEA'),(11,NULL,'대구','SEA'),(12,NULL,'도다리','SEA'),(13,NULL,'독가시치','SEA'),(14,NULL,'돌돔','SEA'),(15,NULL,'동갈돗돔','SEA'),(16,NULL,'무늬오징어','SEA'),(17,NULL,'무점매가리','SEA'),(18,NULL,'문치가자미','SEA'),(19,NULL,'민어','SEA'),(20,NULL,'방어','SEA'),(21,NULL,'백조기','SEA'),(22,NULL,'벵에돔','SEA'),(23,NULL,'벤자리','SEA'),(24,NULL,'볼락','SEA'),(25,NULL,'부시리','SEA'),(26,NULL,'불볼락(열기)','SEA'),(27,NULL,'붉바리','SEA'),(28,NULL,'붉퉁돔','SEA'),(29,NULL,'붕장어','SEA'),(30,NULL,'살오징어','SEA'),(31,NULL,'삼치','SEA'),(32,NULL,'숭어','SEA'),(33,NULL,'쏨뱅이','SEA'),(34,NULL,'옥돔','SEA'),(35,NULL,'어름돔','SEA'),(36,NULL,'용가자미','SEA'),(37,NULL,'임연수어','SEA'),(38,NULL,'자바리','SEA'),(39,NULL,'전갱이','SEA'),(40,NULL,'전어','SEA'),(41,NULL,'조피볼락(우럭)','SEA'),(42,NULL,'점성어','SEA'),(43,NULL,'주꾸미','SEA'),(44,NULL,'쥐치','SEA'),(45,NULL,'참가자미','SEA'),(46,NULL,'참돔','SEA'),(47,NULL,'참문어','SEA'),(48,NULL,'참우럭','SEA'),(49,NULL,'큰민어','SEA'),(50,NULL,'피문어','SEA'),(51,NULL,'학공치','SEA'),(52,NULL,'한치','SEA'),(53,NULL,'호래기','SEA'),(54,NULL,'황열기','SEA'),(55,NULL,'송어','FRESHWATER'),(56,NULL,'가물치','FRESHWATER'),(57,NULL,'가시고기','FRESHWATER'),(58,NULL,'강준치','FRESHWATER'),(59,NULL,'메기','FRESHWATER'),(60,NULL,'쏘가리','FRESHWATER'),(61,NULL,'배스','FRESHWATER');
/*!40000 ALTER TABLE `prod_fish_species` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_fish_species_mapping`
--

DROP TABLE IF EXISTS `prod_fish_species_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prod_fish_species_mapping` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fs_id` bigint NOT NULL,
  `prod_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo9rp5e5evfrjopo0upxplxhhp` (`fs_id`),
  KEY `FKfwdt3hlep17rx4nnqhxv7wmmo` (`prod_id`),
  CONSTRAINT `FKfwdt3hlep17rx4nnqhxv7wmmo` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`),
  CONSTRAINT `FKo9rp5e5evfrjopo0upxplxhhp` FOREIGN KEY (`fs_id`) REFERENCES `prod_fish_species` (`fs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_fish_species_mapping`
--

LOCK TABLES `prod_fish_species_mapping` WRITE;
/*!40000 ALTER TABLE `prod_fish_species_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_fish_species_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_fishing_gear`
--

DROP TABLE IF EXISTS `prod_fishing_gear`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prod_fishing_gear` (
  `fg_id` bigint NOT NULL AUTO_INCREMENT,
  `fg_icon_url` varchar(500) DEFAULT NULL,
  `fg_name` varchar(255) NOT NULL,
  PRIMARY KEY (`fg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_fishing_gear`
--

LOCK TABLES `prod_fishing_gear` WRITE;
/*!40000 ALTER TABLE `prod_fishing_gear` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_fishing_gear` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_fishing_gear_mapping`
--

DROP TABLE IF EXISTS `prod_fishing_gear_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prod_fishing_gear_mapping` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fg_id` bigint NOT NULL,
  `prod_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7uos7lgxu7v5w2itrqrnn4pj5` (`fg_id`),
  KEY `FK7h55wseho47uddnhdc5hmkisc` (`prod_id`),
  CONSTRAINT `FK7h55wseho47uddnhdc5hmkisc` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`),
  CONSTRAINT `FK7uos7lgxu7v5w2itrqrnn4pj5` FOREIGN KEY (`fg_id`) REFERENCES `prod_fishing_gear` (`fg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_fishing_gear_mapping`
--

LOCK TABLES `prod_fishing_gear_mapping` WRITE;
/*!40000 ALTER TABLE `prod_fishing_gear_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_fishing_gear_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_option`
--

DROP TABLE IF EXISTS `prod_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prod_option` (
  `opt_id` bigint NOT NULL AUTO_INCREMENT,
  `opt_description` varchar(255) NOT NULL,
  `opt_name` varchar(255) NOT NULL,
  `opt_time` varchar(255) NOT NULL,
  `price` decimal(7,0) DEFAULT NULL,
  `prod_id` bigint NOT NULL,
  PRIMARY KEY (`opt_id`),
  KEY `FKh9ipm332x1j70r0p82tkpwcn` (`prod_id`),
  CONSTRAINT `FKh9ipm332x1j70r0p82tkpwcn` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_option`
--

LOCK TABLES `prod_option` WRITE;
/*!40000 ALTER TABLE `prod_option` DISABLE KEYS */;
INSERT INTO `prod_option` VALUES (10,'ADAS','ASD','10',10000,1),(11,'ADAS','ASD','10',20000,37);
/*!40000 ALTER TABLE `prod_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `prod_id` bigint NOT NULL AUTO_INCREMENT,
  `main_type` enum('FRESHWATER','SEA') NOT NULL,
  `max_person` int DEFAULT NULL,
  `min_person` int DEFAULT NULL,
  `prod_address` varchar(255) DEFAULT NULL,
  `prod_description` tinytext,
  `prod_event` tinytext,
  `prod_name` varchar(255) NOT NULL,
  `prod_notice` tinytext,
  `prod_region` enum('BUSAN','CHUNGBUK','CHUNGNAM','DAEJEON','DAEGU','GANGWON','GWANGJU','GYEONGBUK','GYEONGGI','GYEONGNAM','INCHEON','JEJU','JEONBUK','JEONNAM','SEJONG','SEOUL','ULSAN') NOT NULL,
  `sub_type` enum('ARTIFICIAL','BEACH','BOAT','BREAK_WATER','CANAL','DAM','ESTUARY','FLOATING_PLATFORM','MUD_FLAT','OPEN_AREA','OPEN_SEA','OTHER_FRESHWATER','OTHER_SEA','POND','REEF','RESERVOIR','RIVER','ROCKY_SHORE','SMALL_LAKE') NOT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  `uno` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `available_date` date DEFAULT NULL,
  PRIMARY KEY (`prod_id`),
  KEY `FK21iqywowp70viujymh58nd4at` (`uno`),
  CONSTRAINT `FK21iqywowp70viujymh58nd4at` FOREIGN KEY (`uno`) REFERENCES `partners` (`uno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'SEA',20,1,'1','1','1','상어호','1','BUSAN','BEACH',10.00,10,'2025-04-14 15:52:35.000000',NULL),(24,'SEA',20,1,'서울 변두리','테스트','없음','상어호','','INCHEON','BREAK_WATER',NULL,10,'2025-04-17 11:45:48.362157',NULL),(25,'SEA',20,1,'변두리','테스트','없음','상어호','','SEOUL','BREAK_WATER',NULL,10,'2025-04-17 11:49:23.384490',NULL),(26,'SEA',20,1,'변두리','테스트','없음','상어호','','BUSAN','BREAK_WATER',NULL,10,'2025-04-17 12:32:29.871199',NULL),(27,'SEA',20,1,'변두리','테스트','없음','상어호','','SEOUL','BREAK_WATER',NULL,10,'2025-04-17 12:34:26.854703',NULL),(28,'SEA',20,1,'변두리','테스트','없음','상어호','','SEOUL','BREAK_WATER',NULL,10,'2025-04-17 12:42:11.571943',NULL),(29,'SEA',20,1,'55','5','5','상어호','5','SEOUL','BREAK_WATER',5.00,10,'2025-04-17 12:42:18.255279',NULL),(30,'SEA',20,1,'변두리','테스트','없음','상어호','','SEOUL','BREAK_WATER',NULL,10,'2025-04-17 12:50:21.511773',NULL),(31,'SEA',20,1,'변두리','테스트','없음','상어호','','SEOUL','BREAK_WATER',NULL,10,'2025-04-17 14:10:10.708067',NULL),(32,'SEA',20,1,'변두리','테스트','없음','상어호','','SEOUL','BREAK_WATER',NULL,10,'2025-04-17 14:13:33.677125',NULL),(33,'FRESHWATER',20,1,'변두리','테스트','없음','상어호','','SEOUL','BREAK_WATER',NULL,10,'2025-04-17 14:17:42.364632',NULL),(37,'SEA',20,1,'샤크','qwe','qwe','상어호','qwe','SEOUL','BREAK_WATER',5.00,10,'2025-04-17 14:56:06.936169',NULL),(38,'SEA',20,1,'','','','상어호','','INCHEON','BREAK_WATER',NULL,10,'2025-04-17 15:07:20.084315',NULL),(41,'SEA',20,1,'테스트1','테스트1','테스트1','테스트1','테스트1','JEJU','BREAK_WATER',20.00,10,'2025-04-18 14:30:24.095426',NULL),(42,'SEA',20,1,'테스트1','테스트1','테스트1','테스트2','테스트1','GYEONGNAM','BREAK_WATER',20.00,10,'2025-04-18 14:30:33.849284',NULL),(43,'SEA',20,1,'테스트1','테스트1','테스트1','테스트3','테스트1','GYEONGBUK','BREAK_WATER',20.00,10,'2025-04-18 14:30:38.991909',NULL),(44,'SEA',20,1,'테스트','테스트','테스트','테스트4','테스트','JEONNAM','ROCKY_SHORE',20.00,10,'2025-04-18 14:30:54.309795',NULL),(45,'SEA',20,1,'테스트','테스트','테스트','테스트5','테스트','CHUNGNAM','ROCKY_SHORE',20.00,10,'2025-04-18 14:37:45.523301',NULL),(46,'SEA',20,1,'테스트','테스트','테스트','테스트6','테스트','CHUNGBUK','ROCKY_SHORE',20.00,10,'2025-04-18 14:37:59.526603',NULL),(47,'SEA',20,1,'테스트','테스트','테스트','테스트7','테스트','GANGWON','ROCKY_SHORE',20.00,10,'2025-04-18 14:38:04.689453',NULL),(48,'SEA',20,1,'테스트','테스트','테스트','테스트8','테스트','GYEONGGI','ROCKY_SHORE',20.00,10,'2025-04-18 14:38:09.513311',NULL),(49,'SEA',20,1,'테스트','테스트','테스트','테스트9','테스트','SEJONG','ROCKY_SHORE',20.00,10,'2025-04-18 14:38:14.100309',NULL),(50,'FRESHWATER',20,1,'민물테스트1','민물테스트1','민물테스트1','민물테스트1','민물테스트1','SEOUL','BREAK_WATER',20.00,10,'2025-04-18 15:14:19.370035','2025-04-21'),(51,'FRESHWATER',20,1,'민물테스트','민물테스트','민물테스트','민물테스트2','민물테스트','BUSAN','BREAK_WATER',20.00,10,'2025-04-18 15:14:28.042296','2025-04-21'),(53,'FRESHWATER',20,1,'민물테스트','민물테스트','민물테스트','민물테스트4','민물테스트','INCHEON','BREAK_WATER',20.00,10,'2025-04-18 15:14:41.347049','2025-04-21'),(57,'FRESHWATER',20,1,'민물테스트','민물테스트','민물테스트','민물테스트','민물테스트','GYEONGGI','BREAK_WATER',20.00,10,'2025-04-18 15:14:59.578213','2025-04-21'),(58,'FRESHWATER',20,1,'민물테스트','민물테스트','민물테스트','민물테스트','민물테스트','GANGWON','BREAK_WATER',20.00,10,'2025-04-18 15:15:04.654205','2025-04-21'),(59,'FRESHWATER',20,1,'민물테스트','민물테스트','민물테스트','민물테스트','민물테스트','CHUNGBUK','BREAK_WATER',20.00,10,'2025-04-18 15:15:11.818029','2025-04-21'),(60,'FRESHWATER',20,1,'민물테스트','민물테스트','민물테스트','민물테스트','민물테스트','CHUNGNAM','BREAK_WATER',20.00,10,'2025-04-18 15:15:15.400654','2025-04-20'),(61,'FRESHWATER',20,1,'민물테스트','민물테스트','민물테스트','민물테스트','민물테스트','CHUNGNAM','BREAK_WATER',20.00,10,'2025-04-18 15:20:25.308790','2025-04-18'),(62,'SEA',20,1,'1','1','1','바다호','1','SEOUL','BREAK_WATER',20.00,10,'2025-04-21 12:17:22.207865','2025-04-21'),(63,'SEA',20,1,'테스트','테스트','테스트','테스트','테스트','SEOUL','BREAK_WATER',15.00,10,'2025-04-22 17:51:18.970570',NULL),(64,'SEA',20,1,'테스트','테스트','테스트','테스트11','테스트','SEOUL','BREAK_WATER',15.00,10,'2025-04-22 17:53:55.912042',NULL),(65,'SEA',20,1,'테스트','테스트','테스트','테스트11','테스트','SEOUL','ROCKY_SHORE',15.00,10,'2025-04-22 17:54:24.448193',NULL),(66,'SEA',20,1,'테스트','테스트','테스트','테스트11','테스트','SEOUL','ROCKY_SHORE',15.00,10,'2025-04-22 17:54:24.740412',NULL),(67,'SEA',20,1,'테스트','테스트','테스트','테스트1','테스트','SEOUL','ROCKY_SHORE',15.00,10,'2025-04-22 17:56:59.912792',NULL),(68,'SEA',20,1,'강원도','강원도','x','강원도호','x','GANGWON','ROCKY_SHORE',20.00,10,'2025-04-25 09:23:39.008649',NULL),(69,'FRESHWATER',20,1,'강원도','강원도','x','경기도호','x','GYEONGGI','RIVER',20.00,10,'2025-04-25 09:24:00.198108',NULL),(70,'SEA',20,1,'강원도','강원도','x','경남호','x','GYEONGNAM','OPEN_SEA',20.00,10,'2025-04-25 09:24:15.108288',NULL),(71,'FRESHWATER',20,1,'강원도','강원도','x','경북호','x','GYEONGBUK','RIVER',20.00,10,'2025-04-25 09:24:28.803101',NULL),(72,'FRESHWATER',20,1,'강원도','강원도','x','경북호','x','GWANGJU','RIVER',20.00,10,'2025-04-25 10:56:59.237546',NULL),(73,'SEA',20,1,'강원도','강원도','x','광주호','x','GWANGJU','RIVER',20.00,10,'2025-04-25 10:57:07.553641',NULL),(74,'SEA',20,1,'테스트입니다','테스트입니다','테스트입니다','돌핀호','테스트입니다','GANGWON','BREAK_WATER',20.00,10,'2025-04-25 18:09:17.678889',NULL),(75,'SEA',20,1,'ss','ss','ss','as','ss','GANGWON','BREAK_WATER',20.00,10,'2025-04-28 10:34:39.845418',NULL),(76,'SEA',20,1,'test','test','test','qwew','test','GANGWON','BREAK_WATER',20.00,10,'2025-04-28 12:04:15.153630',NULL),(77,'SEA',20,1,'테스트','테스트','테스트','테스트','테스트','GANGWON','BREAK_WATER',0.00,10,'2025-05-07 15:48:41.160637',NULL),(78,'SEA',20,1,'테스트','테스트','테스트','테스트','테스트','GANGWON','BREAK_WATER',0.00,10,'2025-05-07 15:52:29.272855',NULL),(80,'SEA',30,10,'상품등록수정테스트','상품등록수정테스트','상품등록수정테스트','상품등록수정테스트','상품등록수정테스트','GANGWON','BREAK_WATER',0.00,10,'2025-05-07 15:54:23.481972',NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `question_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `question_content` varchar(255) NOT NULL,
  `question_title` varchar(255) NOT NULL,
  `question_type` enum('BUSINESS','CANCELLATION','PRODUCT','RESERVATION','SYSTEM') NOT NULL,
  `u_type` enum('ADMIN','PARTNER','USER') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `qc_id` bigint NOT NULL,
  PRIMARY KEY (`question_id`),
  KEY `FKggvxxy7bygh0r2cmj4fnomhda` (`qc_id`),
  CONSTRAINT `FKggvxxy7bygh0r2cmj4fnomhda` FOREIGN KEY (`qc_id`) REFERENCES `question_category` (`qc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_answers`
--

DROP TABLE IF EXISTS `question_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_answers` (
  `qa_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `answer_id` bigint DEFAULT NULL,
  `qa_content` varchar(255) NOT NULL,
  `question_id` bigint DEFAULT NULL,
  PRIMARY KEY (`qa_id`),
  KEY `FKlglw0r110cw97aje0b0pa4q51` (`question_id`),
  CONSTRAINT `FKlglw0r110cw97aje0b0pa4q51` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_answers`
--

LOCK TABLES `question_answers` WRITE;
/*!40000 ALTER TABLE `question_answers` DISABLE KEYS */;
/*!40000 ALTER TABLE `question_answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_category`
--

DROP TABLE IF EXISTS `question_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_category` (
  `qc_id` bigint NOT NULL AUTO_INCREMENT,
  `category_type` enum('ADMIN','PARTNER','USER') NOT NULL,
  `qc_name` varchar(255) NOT NULL,
  PRIMARY KEY (`qc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_category`
--

LOCK TABLES `question_category` WRITE;
/*!40000 ALTER TABLE `question_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `question_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `reservation_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `fishing_at` datetime(6) NOT NULL,
  `num_person` int NOT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `payment_method` enum('BANK_TRANSFER','CARD','KAKAO_PAY') NOT NULL,
  `reservation_status` enum('CANCELED','PAID','PENDING') NOT NULL,
  `uid` bigint NOT NULL,
  `prod_id` bigint NOT NULL,
  `opt_id` bigint NOT NULL,
  PRIMARY KEY (`reservation_id`),
  KEY `FKhv7ejv6d7nln8xu2mwhbm18m0` (`prod_id`),
  KEY `FKrji9lv0jsdp8mofgs0kagnmhe` (`opt_id`),
  KEY `FK7w2wdkhvd973xydyb2fuemi6r` (`uid`),
  CONSTRAINT `FK7w2wdkhvd973xydyb2fuemi6r` FOREIGN KEY (`uid`) REFERENCES `users` (`uno`),
  CONSTRAINT `FKhv7ejv6d7nln8xu2mwhbm18m0` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`),
  CONSTRAINT `FKrji9lv0jsdp8mofgs0kagnmhe` FOREIGN KEY (`opt_id`) REFERENCES `prod_option` (`opt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,'2025-04-14 15:53:06.000000','2025-04-14 15:53:09.000000',1,'1970-01-01 00:00:00.001000','CARD','CANCELED',21,1,10),(2,'2025-04-14 15:53:06.000000','2025-04-14 15:53:09.000000',1,'1970-01-01 00:00:00.001000','CARD','PAID',21,1,10);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tide_station`
--

DROP TABLE IF EXISTS `tide_station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tide_station` (
  `station_code` varchar(255) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `region` enum('BUSAN','CHUNGBUK','CHUNGNAM','DAEJEON','DAEGU','GANGWON','GWANGJU','GYEONGBUK','GYEONGGI','GYEONGNAM','INCHEON','JEJU','JEONBUK','JEONNAM','SEJONG','SEOUL','ULSAN') DEFAULT NULL,
  `station_name` varchar(255) DEFAULT NULL,
  `wave_station_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`station_code`),
  KEY `FKbywfixpg2a11chju75jcputhe` (`wave_station_code`),
  CONSTRAINT `FKbywfixpg2a11chju75jcputhe` FOREIGN KEY (`wave_station_code`) REFERENCES `wave_station` (`station_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tide_station`
--

LOCK TABLES `tide_station` WRITE;
/*!40000 ALTER TABLE `tide_station` DISABLE KEYS */;
INSERT INTO `tide_station` VALUES ('DT_0001',37.451,126.592,'INCHEON','인천','TW_0069'),('DT_0002',36.966,126.822,'GYEONGGI','평택','TW_0069'),('DT_0003',35.426,126.42,'JEONNAM','영광','TW_0079'),('DT_0004',33.527,126.543,'JEJU','제주','KG_0028'),('DT_0005',35.096,129.035,'BUSAN','부산','TW_0088'),('DT_0006',37.55,129.116,'GANGWON','묵호','TW_0094'),('DT_0007',34.779,126.375,'JEONNAM','목포','TW_0081'),('DT_0008',37.192,126.647,'GYEONGGI','안산','TW_0069'),('DT_0010',33.24,126.561,'JEJU','서귀포','TW_0075'),('DT_0011',36.677,129.453,'GYEONGBUK','후포','TW_0095'),('DT_0012',38.207,128.594,'GANGWON','속초','TW_0093'),('DT_0013',37.491,130.913,'GYEONGBUK','울릉도','KG_0102'),('DT_0014',34.827,128.434,'GYEONGNAM','통영','DT_0042'),('DT_0016',34.747,127.765,'JEONNAM','여수','DT_0042'),('DT_0017',37.007,126.352,'CHUNGNAM','대산','TW_0069'),('DT_0018',35.975,126.563,'JEONBUK','군산','TW_0069'),('DT_0020',35.501,129.387,'ULSAN','울산','TW_0090'),('DT_0021',33.961,126.3,'JEJU','추자도','KG_0028'),('DT_0022',33.474,126.927,'JEJU','성산포','KG_0028'),('DT_0023',33.214,126.251,'JEJU','모슬포','TW_0075'),('DT_0024',36.006,126.687,'CHUNGNAM','장항','TW_0069'),('DT_0025',36.406,126.486,'CHUNGNAM','보령','TW_0069'),('DT_0026',34.481,127.342,'JEONNAM','고흥발포','TW_0081'),('DT_0027',34.315,126.759,'JEONNAM','완도','TW_0081'),('DT_0028',34.377,126.308,'JEONNAM','진도','TW_0081'),('DT_0029',34.801,128.699,'GYEONGNAM','거제도','DT_0042'),('DT_0031',34.028,127.308,'JEONNAM','거문도','TW_0081'),('DT_0032',37.731,126.522,'INCHEON','강화대교','IE_0062'),('DT_0035',34.684,125.435,'JEONNAM','흑산도','IE_0061'),('DT_0037',36.117,125.984,'CHUNGNAM','어청도','TW_0069'),('DT_0042',34.704,128.306,'GYEONGNAM','교본초','DT_0042'),('DT_0043',37.238,126.428,'INCHEON','영흥도','TW_0069'),('DT_0044',37.545,126.584,'INCHEON','영종대교','TW_0069'),('DT_0049',34.903,127.754,'JEONNAM','광양','DT_0042'),('DT_0050',36.913,126.238,'CHUNGNAM','태안','TW_0069'),('DT_0051',36.128,126.495,'CHUNGNAM','서천마량','TW_0069'),('DT_0052',37.338,126.586,'INCHEON','인천송도','TW_0069'),('DT_0054',35.147,128.643,'GYEONGNAM','진해','TW_0088'),('DT_0056',35.077,128.784,'BUSAN','부산항신항','TW_0088'),('DT_0057',37.494,129.143,'GANGWON','동해항','TW_0094'),('DT_0061',34.924,128.069,'GYEONGNAM','삼천포','DT_0042'),('DT_0062',35.197,128.576,'GYEONGNAM','마산','TW_0088'),('DT_0063',35.024,128.81,'BUSAN','가덕도','TW_0088'),('DT_0065',37.226,126.156,'INCHEON','덕적도','TW_0069'),('DT_0066',35.167,126.359,'JEONNAM','향화도','TW_0079'),('DT_0067',36.674,126.129,'CHUNGNAM','안흥','TW_0069'),('DT_0068',35.618,126.301,'JEONBUK','위도','TW_0079'),('DT_0091',36.051,129.376,'GYEONGBUK','포항','TW_0095'),('DT_0092',34.661,127.469,'JEONNAM','여호항','TW_0081'),('DT_0094',34.251,125.915,'JEONNAM','서거차도','KG_0028'),('DT_0902',36.003,129.413,'GYEONGBUK','포항시청_냉천항만교(수위)','TW_0095'),('IE_0061',33.941,124.592,'JEONNAM','신안가거초','IE_0061'),('IE_0062',37.423,124.738,'INCHEON','옹진소청초','IE_0062'),('SO_0553',35.16,129.191,'BUSAN','해운대','TW_0062'),('SO_0581',35.39,129.344,'ULSAN','강양항','TW_0090'),('SO_0733',37.772,128.951,'GANGWON','강릉항','TW_0089'),('SO_1262',35.62,126.463,'JEONBUK','격포항','TW_0079');
/*!40000 ALTER TABLE `tide_station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_actions`
--

DROP TABLE IF EXISTS `user_actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_actions` (
  `ua_id` bigint NOT NULL AUTO_INCREMENT,
  `action_type` enum('LIKE','WISH') NOT NULL,
  `board_type` enum('DIARY','FREE','PRODUCT','REPORT') NOT NULL,
  `target_id` bigint NOT NULL,
  `value` int NOT NULL DEFAULT '1',
  `uid` bigint NOT NULL,
  PRIMARY KEY (`ua_id`),
  KEY `FK4eousipow2107xoigdfsln9lv` (`uid`),
  CONSTRAINT `FK4eousipow2107xoigdfsln9lv` FOREIGN KEY (`uid`) REFERENCES `users` (`uno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_actions`
--

LOCK TABLES `user_actions` WRITE;
/*!40000 ALTER TABLE `user_actions` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_actions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_reports`
--

DROP TABLE IF EXISTS `user_reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_reports` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `reason` varchar(255) NOT NULL,
  `reported_id` bigint NOT NULL,
  `reporter_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn9g6sh0j1corsna0v58ve9tkw` (`reported_id`),
  KEY `FKo091kj8ufs63qduxpnbeuefa3` (`reporter_id`),
  CONSTRAINT `FKn9g6sh0j1corsna0v58ve9tkw` FOREIGN KEY (`reported_id`) REFERENCES `users` (`uno`),
  CONSTRAINT `FKo091kj8ufs63qduxpnbeuefa3` FOREIGN KEY (`reporter_id`) REFERENCES `users` (`uno`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_reports`
--

LOCK TABLES `user_reports` WRITE;
/*!40000 ALTER TABLE `user_reports` DISABLE KEYS */;
INSERT INTO `user_reports` VALUES (1,NULL,'신고함',21,1),(2,'2025-04-24 10:01:50.843805','343423',21,21),(3,'2025-04-24 10:09:23.281411','sdad',21,21),(4,'2025-04-24 10:09:46.260307','asda',20,21),(5,'2025-04-24 10:12:34.319308','23123',20,21),(6,'2025-04-24 10:17:02.769492','신고하고싶어서',1,21),(7,'2025-04-24 10:19:40.003657','2323',20,21),(8,'2025-04-24 10:19:55.595972','신고',20,21),(11,'2025-05-07 16:28:14.947716','13123',21,27),(12,'2025-05-07 16:28:30.464389','13123',21,27),(13,'2025-05-07 16:38:13.468899','123123',18,27),(14,'2025-05-07 16:48:38.794660','sdasd',14,27);
/*!40000 ALTER TABLE `user_reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `uno` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `level` enum('DIAMOND','GOLD','PLATINUM','SILVER') DEFAULT NULL,
  `level_point` int NOT NULL DEFAULT '0',
  `nickname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) NOT NULL,
  `points` int NOT NULL DEFAULT '0',
  `profile_img` varchar(255) DEFAULT NULL,
  `role` enum('PARTNER','USER') NOT NULL,
  `uid` varchar(255) NOT NULL,
  `uname` varchar(255) DEFAULT NULL,
  `upw` varchar(512) NOT NULL,
  `login_type` enum('KAKAO','LOCAL','NAVER') NOT NULL,
  `version` int DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  PRIMARY KEY (`uno`),
  UNIQUE KEY `UKefqukogbk7i0poucwoy2qie74` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2025-04-17 13:18:59.000000','asd020712a@gmail.com11','SILVER',100,'관리자','010-0000-0000',10000,NULL,'USER','1','관리자님','1','LOCAL',0,_binary ''),(10,'2025-04-14 11:01:01.759558','asd222@asdsa','SILVER',0,NULL,'010-2312-1364',0,NULL,'USER','asd020712a','구하리','$2a$10$Uex5p4ryBC12MnwBkWewcesfprBwd8GmOr1K8I2aSgYPuqOK8N.6K','LOCAL',0,_binary ''),(11,'2025-04-14 12:04:19.740784','asd222@asdsa22','SILVER',0,NULL,'010-2342-1364',0,NULL,'USER','asd020712','이산헝','$2a$10$fVaFqFFbbJ6VvQTQFKKbbe14KAqtGx044KXaAr/yrA8o0I7bowWVm','LOCAL',0,_binary ''),(12,'2025-04-14 15:16:31.462487','asd020712a@gmail.com222','SILVER',0,NULL,'010-1233-1233',0,NULL,'USER','asd020712as','구하리','$2a$10$/DLx9SQz3Im/VXwU4ON3l.P.GC..gc9lrwgwjXJOqINhKUAkmgV5C','LOCAL',0,_binary ''),(14,'2025-04-15 15:36:59.328643','asd020712a@gmail.com','SILVER',0,'박준하','010-2311-2341',0,NULL,'USER','qwer11234','이한성','$2a$10$bAwurcuTD.e8dxPJ3wEPS.BUl6yuNcarpqONjDllCMjdtaltbUDi.','LOCAL',0,_binary ''),(18,'2025-04-16 09:52:31.159785','faf@ac.kr','SILVER',0,'','--',0,NULL,'USER','qaz1111','vrf','$2a$10$bqZ1cGq1W1/yLGAOYKBE4.rSXnkP9UQt1IN3XBEkSSQg.D9hVFZ8O','LOCAL',1,_binary ''),(20,'2025-04-16 16:38:30.311602','asd020712A2@asdasd','SILVER',0,'제발','--',0,NULL,'USER','rwerwer12','박준하','$2a$10$YdwKiM.N8G/KQFBrkCMYcuecFRMIWjv.g6lUeSXLqDQp.67QV.ydq','LOCAL',3,_binary ''),(21,'2025-04-17 09:54:03.389033','asd020712a@gmail.com22','GOLD',2,'기릿','--',1,NULL,'USER','asd02071a','구함리','$2a$10$WDjgN83NLB1g94xFlW85GuTe3fj5ZLhUk0dieUpyb01kGBhFLX.8G','LOCAL',13,_binary ''),(27,'2025-05-02 14:47:14.018587','dadadd@asdasd22','SILVER',0,'강강술','--',0,'/img/fish.png','USER','asd020712aa','양학중','$2a$10$UyvA3jbFTXTsJi2VedXfiuNRjklrAbdYFdQJnlECm1AoMnFyD2BDK','LOCAL',4,_binary ''),(34,'2025-05-08 11:08:07.674232','sdad323232@asdsad','SILVER',0,NULL,'010-1233-2342',0,NULL,'USER','qwerwer1232','가나다','$2a$10$kJtJpnkTF.aH/zhRgeFt9e5mzd6Y5OeR1bMTwI61QPoeU2TEX2N9i','LOCAL',0,_binary ''),(35,'2025-05-08 11:08:31.833063','asd020712a@gmail.com2212312','SILVER',0,NULL,'010-3123-1231',0,NULL,'USER','fdafsaf23423','이한성','$2a$10$XON4KSU.KPjpr5.yWRWKJeDx0Slo.zUEZfUTIQoZQv9WNyOE47uOO','LOCAL',0,_binary '');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wave_station`
--

DROP TABLE IF EXISTS `wave_station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wave_station` (
  `station_code` varchar(255) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `station_name` varchar(255) DEFAULT NULL,
  `region` enum('BUSAN','CHUNGBUK','CHUNGNAM','DEAGEON','DEAGU','GANGWON','GWWANGJU','GYEONGBUK','GYEONGGI','GYEONGNAM','INCHEON','JEJU','JEONBUK','JEONNAM','SEJONG','SEOUL','ULSSAN') DEFAULT NULL,
  PRIMARY KEY (`station_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wave_station`
--

LOCK TABLES `wave_station` WRITE;
/*!40000 ALTER TABLE `wave_station` DISABLE KEYS */;
INSERT INTO `wave_station` VALUES ('DT_0039',36.719,129.732,'왕돌초',NULL),('DT_0042',34.704,128.306,'교본초',NULL),('IE_0060',32.122,125.182,'이어도',NULL),('IE_0061',33.941,124.592,'신안가거초',NULL),('IE_0062',37.423,124.738,'옹진소청초',NULL),('KG_0024',34.919,129.121,'대한해협',NULL),('KG_0028',33.7,126.59,'제주해협',NULL),('KG_0102',37.742,130.601,'울릉도북서',NULL),('TW_0062',35.148,129.17,'해운대해수욕장',NULL),('TW_0069',36.274,126.457,'대천해수욕장',NULL),('TW_0075',33.234,126.409,'중문해수욕장',NULL),('TW_0079',35.652,126.194,'상왕등도',NULL),('TW_0081',34.258,126.96,'생일도',NULL),('TW_0088',35.052,129.003,'감천항',NULL),('TW_0089',37.808,128.931,'경포대해수욕장',NULL),('TW_0090',35.164,129.219,'송정해수욕장',NULL),('TW_0091',38.122,128.65,'낙산해수욕장',NULL),('TW_0093',38.198,128.631,'속초해수욕장',NULL),('TW_0094',37.616,129.103,'망상해수욕장',NULL),('TW_0095',36.58,129.454,'고래불해수욕장',NULL);
/*!40000 ALTER TABLE `wave_station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'dagon'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-09 10:24:46
