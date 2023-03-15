## 회원가입

### Request
```

> POST /api/join HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/json; charset=UTF-8

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