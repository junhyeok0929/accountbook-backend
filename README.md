# 📔 추억 가계부 (Accountbook) - Backend

사용자의 소중한 추억과 금융 거래를 함께 기록하는 '추억 가계부' 서비스의 백엔드 서버입니다.

## 🛠 기술 스택
- **Framework:** Spring Boot 3.x
- **Language:** Java 17
- **Security:** Spring Security, JWT (JSON Web Token)
- **Database:** MySQL, Spring Data JPA
- **Build Tool:** Gradle

## ✨ 주요 기능
- **회원 관리:** JWT 기반의 보안 로그인 및 회원가입 서비스 제공
- **가계부 관리:** 지출/수입 내역의 생성, 조회, 수정, 삭제 (CRUD)
- **추억 기록:** 거래 내역과 연동된 일기 작성 및 관리 기능
- **데이터 보안:** Spring Security와 JWT 인터셉터를 통한 사용자별 데이터 접근 제어

## ⚙️ 실행 방법
1. MySQL에 `memoir_db` 데이터베이스를 생성합니다.
2. `src/main/resources/application.properties`에서 DB 계정 정보를 확인합니다.
3. `./gradlew bootRun` 명령어로 애플리케이션을 실행합니다.
4. 서버는 `http://localhost:8088`에서 구동됩니다.

## 📂 프로젝트 구조
- `controller`: REST API 엔드포인트 정의
- `service`: 비즈니스 로직 처리
- `repository`: 데이터베이스 접근 인터페이스
- `jwt`: 토큰 생성 및 검증 로직
- `config`: 보안 및 애플리케이션 설정
