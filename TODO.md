## TODO List

### API

- [x] 회원가입
  - POST /auth/join
  - request: username, password, nickname
  - response
    - ✅ SUCCESS -> username, nickname, roles
    - ❌ FAILURE -> error (code, message)

- [x] 로그인
  - POST /auth/login
  - request: username, password
  - response
      - ✅ SUCCESS -> token
      - ❌ FAILURE -> error (code, message)

- [x] 관리자 권한 부여
  - PATCH /users/{userId}/admin
  - response
      - ✅ SUCCESS -> username, nickname, roles
      - ❌ FAILURE -> error (code, message)

- [x] 토큰 재발급
  - GET /auth/refresh
  - request: accessToken, refreshToken
  - response
    - ✅ SUCCESS -> accessToken
    - ❌ FAILURE -> error (code, message)

### 테스트 (Junit)
- AuthController
  - [x] /auth/join
    - 정상적인 사용자
    - 이미 가입된 사용자
  - [x] /auth/login
    - 올바른 자격 증명
    - 잘못된 자격 증명
  - [x] /auth/refresh
    - 유효한 refreshToken
    - 유효하지 않은 refreshToken
- UserController
  - [x] /users/{userId}/admin
      - 관리자 권한을 가진 사용자 요청
      - 일반 권한을 가진 사용자 요청
      - 존재하지 않는 사용자가 요청

### API 명세서
- Swagger 기반 API 문서화
  - [x] /auth/join
  - [x] /auth/login
  - [x] /auth/refresh
  - [x] /users/{userId}/admin