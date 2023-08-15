## wanted-pre-onboarding-backend

지원자: 이한솔

### 실행 방법(docker)
1. repo clone
```
git clone https://github.com/goodzk/wanted-pre-onboarding-backend.git
```
2. docker-compose 명령어 실행
```
docker-compose up --build
```
3. postman으로 아래 설명된 API를 호출

### 데이터베이스 구조
![database](https://github.com/goodzk/wanted-pre-onboarding-backend/assets/142298265/f9fe4bca-522d-4001-9489-648bb43feb91)


### API 데모 영상
https://www.youtube.com/watch?v=sRFUKvUWOO0&ab_channel=%EA%B3%A0%EA%B7%AC%EB%A7%88

### 구현 방법 및 이유
- 로그인 과정
  - Spring Filter를 이용하여 구현
  - Filter는 Spring Context 밖에서 동작하며 인증된 사용자를 확인해야 하는 과정을 filter에서 통합 관리 가능
- 게시글과 사용자 사이의 외래키를 설정하지 않은 이유
  - 빠른 개발 속도 및 간단한 테스트 코드 작성의 장점과 애플리케이션에서도 무결성 검사를 충분히 진행할 수 있기 때문
- 게시글에 username이 포함된 이유
  - username은 변경되지 않으며 userId로 조인해야하는 과정이 필요없어지므로 포함시킴

### API 명세(request, response)
1. user API

| 기능    | URI                  | METHOD |
|-------|----------------------|------|
| 회원가입  | localhost:8080/users | POST |
| 로그인   |  localhost:8080/login  | POST |

- 회원가입 request
```json
{
    "email": "rara111@gmail.com",
    "password": "12345678",
    "name": "your name"
}
```
- 회원가입 response
```json
{
    "success": true,
    "message": 1 // saved user PK
}
```
- login request
```json
{
    "email": "rara111@gmail.com",
    "password": "12345678"
}
```
- login response
```json
{
  "access-token": "...",
  "expired-time": "2023-08-15 12:50:28"
}
```

2. article API
- 추가, 수정, 삭제 기능은 jwt 포함 필수

| 기능    | URI                            | METHOD |
|-------|--------------------------------|--------|
| 상세 조회 | localhost:8080/articles/{id}   | GET    |
| 목록 조회 | localhost:8080/articles?page=0 | GET    |
| 새로 추가 | localhost:8080/articles        | POST   |
| 수정    | localhost:8080/articles/{id}   | PATCH  |
| 삭제    | localhost:8080/articles/{id}   | DELETE |


- 상세 조회 response
```json
{
    "title": "제목",
    "body": "내용",
    "createdDate": "2023년 08월 15일 10:11:15"
}
```
- 목록 조회 response
```json
{
    "content": [
        {
            "id": 3,
            "title": "제목",
            "body": "내용",
            "createdDate": "2023년 08월 15일 10:11:17"
        },
        {
            "id": 2,
            "title": "제목",
            "body": "내용",
            "createdDate": "2023년 08월 15일 10:11:16"
        },
        {
            "id": 1,
            "title": "제목",
            "body": "내용",
            "createdDate": "2023년 08월 15일 10:11:15"
        }
    ],
    "pageable": {
        "sort": {
...
}
```
- 새로 추가 request
```json
{
    "title": "제목",
    "body": "내용"
}
```
- 수정 request
```json
{
    "title": "변경된 제목", 
    "body": "본문"
}
```
