<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:007cf0,50:3edbf0,100:c2e9fb&height=180&section=header&text=DAGON&fontSize=75&fontColor=ffffff" width="100%"/>
</p>


## 🚤 프로젝트 소개

🎣**실시간 낚싯배 예약 플랫폼**

- 사용자, 파트너(판매자), 관리자 간 예약·결제·정보 공유를 통합 제공  
- 물때·날씨 실시간 API 연동 (바다누리, 기상청, 한국천문연구원)  
- 실시간 게시판 및 관리자 시스템 포함

📅 **개발 기간** 2025.03.21 ~ 2025.04.25

</br>

</br>


## 🐟 팀원구성

<table align="center">
  <tr>
    <td align="center" style="padding: 20px;">
      <img src="https://i.imgur.com/U3vTGjX.png" width="100"/><br/>
      <b>장은정</b>
    </td>
    <td align="center" style="padding: 20px;">
      <img src="https://images.unsplash.com/photo-1593085512500-5d55148d6f0d?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVDJUJBJTkwJUVCJUE2JUFEJUVEJTg0JUIwfGVufDB8fDB8fHww" width="100"/><br/>
      <b>손강하</b>
    </td>
    <td align="center" style="padding: 20px;">
      <img src="https://i.imgur.com/oYiTqum.png" width="100"/><br/>
      <b>강진석</b>
    </td>
    <td align="center" style="padding: 20px;">
      <img src="https://i.imgur.com/VSEpKcY.png" width="100"/><br/>
      <b>박준하</b>
    </td>
  </tr>
</table>

</br>

</br>

## 🛠 개발 환경 및 기술 스택

| 구분         | 기술                                                                 |
|--------------|--------------------------------------------------------------------------|
| **Backend**  | Java 17, Spring Boot, Spring Security, JPA (Hibernate), JWT             |
| **Frontend** | HTML, CSS, JavaScript, Thymeleaf, Summernote, Vue.js (2차 연동 예정)    |
| **Database** | MySQL 8.x                                                                |
| **빌드 도구** | Maven                                                                    |
| **API 연동**  | KHOA API, 기상청 API, 한국천문연구원 API, 아임포트             |
| **문서화**    | Swagger (Springdoc OpenAPI), Postman                                    |
| **협업 도구** | Git, GitHub, Notion, Discord, Google Sheets, IntelliJ IDEA              |


</br>

</br>

## 🌟 주요 기능

- **회원 기능**: 일반 회원가입, 소셜 로그인(JWT 인증), 마이페이지
  
- **예약 기능**: 파트너 등록 상품 예약/결제(아임포트 연동)
  
- **날씨/물때 정보**: 실시간 기상 API, 물때 정보 API, 월령 기반 물때명
  
- **게시판**: 공지사항, 이벤트, FAQ, 조황정보, 조행기
  
- **관리자 기능**: 관리자 대시보드, 모든 게시판 관리 및 통계


</br>

</br>

## 📁 프로젝트 구조

```plaintext
dagon/
├── .idea/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── kroryi.dagon/
│   │   │       ├── component/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       │   ├── api/
│   │   │       │   │   ├── auth/
│   │   │       │   │   ├── board/
│   │   │       │   │   ├── booking/
│   │   │       │   │   ├── etc/
│   │   │       │   │   └── product/
│   │   │       ├── DTO/
│   │   │       ├── entity/
│   │   │       ├── enums/
│   │   │       ├── handler/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── util/
│   ├── resources/
│   │   ├── static/
│   │   │   ├── css/
│   │   │   ├── js/
│   │   │   └── img/
│   │   └── templates/
│   │       ├── board/
│   │       ├── admin/
│   │       ├── fragments/
│   │       └── ...
├── test/
│   └── java/kroryi/dagon/
├── target/
├── uploads/
├── pom.xml
└── README.md
```

</br>

</br>

## ⚓ 역할분담

### 🧑 장은정  
- 프로젝트 구조 중심 문서 설계 및 정리
- 공공 API 연동 (물때, 날씨) 및 조회 페이지 구현
- 관리자 권한 게시판(공지사항, 이벤트, FAQ) 구현
- Swagger 정리 및 관리자 페이지에 적용


### 🧑 손강하  
- 홈 페이지 초반 틀 구현  
- 파트너(판매자) 배 상품 등록, 리스트 페이지 구현  
- 실시간 배 상품 예약 페이지 구현  
- 조황센터(조황 / 조행기) 페이지 구현  


### 🧑 강진석  
_(역할 내용 미정) 예시_
- 결제 시스템 구현 (아임포트 연동)
- 예약 상품과 결제 연동 처리
- 1:1 문의 게시판 기능 구현


### 🧑 박준하  
_(역할 내용 미정) 예시_
- 로그인/회원가입 및 JWT 인증 처리
- 마이페이지 구현
- 관리자 대시보드 메뉴 구성 및 기능 연동


</br>

</br>

## 📌 기능별 API 설계
Swagger 기반
_시연 영상 및 이미지 추가 예정_

- **회원 기능**: 로그인 / 회원가입 / 마이페이지 API
  
- **예약 기능**: 예약 등록 / 결제 / 예약 조회 API
  
- **물때 · 날씨 정보**: 실시간 기상 데이터 및 조석(물때) API 연동
  
- **게시판 기능**: 공지사항, 이벤트, FAQ, 조황정보, 조행기 등 CRUD API
  
- **관리자 기능**: 관리자 페이지 내 모든 기능 관리 API

> **API 문서 확인 경로**
> - Swagger UI: [`/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
> - 관리자 대시보드: [`/admin-dashboard.html`](http://localhost:8080/admin/dashboard)

</br>

</br>


## 🌊 개발과정

- **1주차**: 프로젝트 주제 선정, 요구사항 정의  
- **2주차**: ERD 설계, 환경 세팅, 문서 설계  
- **3~4주차**: 핵심 기능 및 관리자 기능 개발  
- **5주차**: 기능 통합, 테스트 및 발표 준비

