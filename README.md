# 은행 업무 서비스 API 

#
## 🧷 개요
**은행 업무 서비스 API는 은행 업무에 필요한 다양한 서비스(입금, 송금, 계좌이체)를 API로 실행할 수 있게하는 RESTful API입니다.**</br>
**은행 업무에 대한 결과를 JSON 형식으로 반환합니다.**</br>
**API를 호출할 때는 계좌번호, 출입금, 계좌이체 정보 및 총액 정보를 JSON 형식의 데이터로 전달합니다.**</br>
**은행 업무 서비스 API를 호출할 때 HTTP 요청 헤더에 서버에서 제공한 JWT 값만 전송해 사용할 수 있는 API입니다.**

#
## 🧷 기술 스택

- Springboot 2.7.9
- JPA
- Security
- JWT
- H2
- QueryDSL

#
## 🧷 테이블 구조
<img alt="image" src="https://user-images.githubusercontent.com/85926257/224892792-54f75c8d-9829-4eca-8a4c-4dd3ad8787a5.png">

#
## 🧷 기능 정리

## 회원가입 

### Request
```

> POST /api/join HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8

```

#### Request Body
```
{
  "username" : "String",
  "password" : "String",
  "email" : "String",
  "fullName" : "String"
}
```
### Response

#### Response Body
```
{
  "code": 1,
  "msg": "로그인 성공",
  "data": {
    "id": Long,
    "username": "String",
    "fullName": "String"
  }
}
```

## 로그인

### Request
```

> POST /api/login HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8

```

#### Request Body
```
{
  "username" : "String",
  "password" : "String",
}
```

### Response

#### Response Header
```
> Authorization: {JWT}
```

#### Response Body
```
{
  "code": 1,
  "msg": "회원가입 성공",
  "data": {
    "id": Long,
    "username": "String",
    "createdAt": "String"
  }
}
```

## 계좌 등록

### Request
```

> POST /api/s/account HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
> Authorization: {JWT}

```

#### Request Body
```
{
  "number" : Long,
  "password" : Long,
}
```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "계좌등록 성공",
  "data": {
    "id": Long,
    "number": Long,
    "balance": Long
  }
}
```

## 계좌 목록 조회

### Request
```

> GET /api/s/account/login-user HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
> Authorization: {JWT}

```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "계좌 목록 불러오기 성공",
  "data": {
    "fullName": String,
    "accounts": [
    	{
        	"id": Long,
            "number": Long,
            "balance": Long
        }
    ]
  }
}
```

## 계좌 삭제

### Request
```

> DELETE /api/s/account/{number} HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
> Authorization: {JWT}

```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "계좌 삭제 성공",
  "data": null
}
```

## 입금

### Request
```

> POST /api/account/deposit HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8

```

#### Request Body
```
{
  "number" : Long,
  "amount": Long,
  "status": String,
  "tel" : String
}
```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "입금 성공",
  "data": {
    "id": Long,
    "number": Long,
    "transactionDto": 
    	{
        	"id": Long,
            "status": String,
            "sender": String,
            "receiver": String,
            "amount": Long,
            "tel": String,
            "createAt": String
        }
  }
}
```

## 출금

### Request
```

> POST /api/s/account/withdraw HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
> Authorization: {JWT}

```

#### Request Body
```
{
  "number" : Long,
  "amount": Long,
  "status": String,
  "tel" : String
}
```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "출금 성공",
  "data": {
    "id": Long,
    "number": Long,
    "transactionDto": 
    	{
        	"id": Long,
            "status": String,
            "sender": String,
            "receiver": String,
            "amount": Long,
            "tel": String,
            "createAt": String
        }
  }
}
```

## 계좌 이체

### Request
```

> POST /api/s/account/transfer HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
> Authorization: {JWT}

```

#### Request Body
```
{
  "withdrawNumber": Long,
  "withdrawPassword": Long,
  "depositNumber": Long
  "amount": Long,
  "status": String,
}
```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "이체 성공",
  "data": {
    "id": Long,
    "number": Long,
    "balance": Long,
    "transactionDto": 
    	{
        	"id": Long,
            "status": String,
            "sender": String,
            "receiver": String,
            "amount": Long,
            "tel": String,
            "createAt": String
        }
  }
}
```

## 계좌 상세보기

### Request
```

> GET /api/s/account/{number} HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
> Authorization: {JWT}

```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "계좌 상세보기 성공",
  "data": {
    "id": Long,
    "number": Long,
    "balance": Long,
    "transactions": [
          {
              "id": Long,
              "status": String,
              "sender": String,
              "receiver": String,
              "amount": Long,
              "tel": String,
              "createAt": String
          }
        ]
  }
}
```

## 계좌 비밀번호 갱신

### Request
```

> POST /api/s/account/password HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
> Authorization: {JWT}

```

#### Request Body
```
{
  "number": Long,
  "password": Long,
  "newPassword": Long
}
```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "계좌 비밀번호 갱신 성공",
  "data": {
        "id": Long,
        "number": Long    	
  }
}
```

## 거래내역 조회

### Request
```

> GET /api/s/account/{number}/transactions HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
> Authorization: {JWT}

```

#### Request Parameter
```
?status=&size=&sort=
```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "거래 내역 조회 성공",
  "data": {
    "transactions": [
    	{
        	"id": Long,
            "balance": Long,
            "status": String,
            "sender": String,
            "receiver": String,
            "amount": Long,
            "tel": String,
            "createAt": String
        }    	
    ]
  }
}
```

