# 백엔드 개발 과제

## Spring Boot 기반 JWT 인증/인가 및 AWS 배포

1️⃣ **Spring Boot**를 이용하여 JWT 인증/인가 로직과 API를 구현한다.

2️⃣ **Junit** 기반의 테스트 코드를 작성한다.

3️⃣ **Swagger** 로 API를 문서화 한다.

4️⃣ 애플리케이션을 **AWS EC2**에 배포하고, 실제 환경에서 실행되도록 구성한다.

## ✔️ 공통 요구사항

1️⃣ 기본으로 설정된 서버의 주소와 포트는 `0.0.0.0:8080` 이고, 이를 수정하지 않는다.

2️⃣ 모든 API 응답은 적절한 HTTP 상태 코드와 함께 `application/json` 형식으로 반환한다.

3️⃣ 실제 데이터베이스나 파일 시스템을 사용하지 않으며, 모든 데이터는 메모리 내에서 처리된다.

## 🏁 실행 방법
배포 주소: 13.124.31.235

초기 관리자 아이디입니다.  
id: admin  
pw: adminpassword

<details>
<summary>로컬 실행 방법</summary>
  이 저장소를 클론합니다.
  
  ```bash
  git clone https://github.com/moongzz/barointern.git
  ```

  아래 명령어로 실행합니다.

  ```bash
  ./gradlew bootrun
  ```

</details>

## 📊 API 명세
http://13.124.31.235:8080/docs 에서 Swagger를 확인할 수 있습니다.

<details>
<summary>AUTH API</summary>

### 🔐 회원 가입

- **URL**: `/v1/auth/join`
- **Method**: `POST`
- **Content-Type**: `application/json;charset=UTF-8`

#### ✅ 요청 예시 (성공)
```json
{
  "user": {
    "username": "username(JoinSuccessTest)",
    "password": "password(JoinSuccessTest)",
    "nickname": "nickname(JoinSuccessTest)"
  }
}
```

#### ❌ 요청 예시 (실패)
중복된 사용자
```json
{
  "user": {
    "username": "username(JoinFailTest)",
    "password": "password(JoinFailTest)",
    "nickname": "nickname(JoinFailTest)"
  }
}
```

#### 🔄 응답 예시

201 Created

```json
{
  "code": "0",
  "message": "회원가입에 성공하였습니다.",
  "data": {
    "user": {
      "username": "username(JoinSuccessTest)",
      "nickname": "nickname(JoinSuccessTest)"
    }
  }
}
```

409 Conflict

```json
{
  "code": "A101",
  "message": "이미 존재하는 사용자입니다.",
  "httpStatus": 409
}
```


### 🔐 로그인
- **URL**: `/v1/auth/login`
- **Method**: `POST`
- **Content-Type**: `application/json;charset=UTF-8`

#### ✅ 요청 예시 (성공)
```json
{
  "user": {
    "username": "username(LoginSuccessTest)",
    "password": "password(LoginSuccessTest)"
  }
}
```

#### ❌ 요청 예시 (실패)
```json
{
  "user": {
    "username": "username(LoginFailTest)",
    "password": "password(LoginSuccessTest)"
  }
}
```

#### 🔄 응답 예시
200 OK
```json
{
  "accessToken": "Bearer <access_token>",
  "refreshToken": "<refresh_token>"
}
```
401 Unauthorized

```json
{
  "code": "A401",
  "message": "인증에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요.",
  "httpStatus": 401
}
```

### 🔄 토큰 재발급
- **URL**: `/v1/auth/refresh`
- **Method**: `GET`
- **Content-Type**: `application/json;charset=UTF-8`


#### 🔄 응답 예시
200 OK
```json
{
  "code": "0",
  "message": "Access Token 재발급 성공했습니다.",
  "data": {
    "accessToken": "Bearer <new_access_token>"
  }
}
```

401 Unauthorized
```json
{
  "code": "A404",
  "message": "만료된 Token입니다. 재로그인을 요청해주세요.",
  "httpStatus": 401
}
```
</details>

<details>
<summary>USER API</summary>

### 🛠️ 회원 권한 수정

- **URL**: `/v1/users/{id}/admin`
- **Method**: `PATCH`
- **Path Parameter**: `id (Long) — 유저 ID`



Authorization: Bearer <access_token>

#### 🔄 응답 예시
200 OK

```json
{
    "code": "0",
    "message": "권한 수정 성공했습니다.",
    "data": {
        "user": {
            "id": 1,
            "role": "ADMIN"
        }
    }
}
```
</details>

## ✏️ 기록

#### 트러블슈팅

- [예외 상황에서 반환되는 상태 코드가 기대값과 다른 문제](https://github.com/moongzz/barointern/wiki/%EC%98%88%EC%99%B8-%EC%83%81%ED%99%A9%EC%97%90%EC%84%9C-%EB%B0%98%ED%99%98%EB%90%98%EB%8A%94-%EC%83%81%ED%83%9C-%EC%BD%94%EB%93%9C%EA%B0%80-%EA%B8%B0%EB%8C%80%EA%B0%92%EA%B3%BC-%EB%8B%A4%EB%A5%B8-%EB%AC%B8%EC%A0%9C)

#### 기술적 의사결정

- [ConcurrentHashMap vs HashMap](https://github.com/moongzz/barointern/wiki/ConcurrentHashMap-vs-HashMap)
