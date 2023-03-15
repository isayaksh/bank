## 회원가입

### Request
```

> POST /api/member/password HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/json; charset=UTF-8
> Authorization: {JWT}

```

#### Request Body
```
{
  "username" : "String",
  "password" : "String",
  "newPassword" : "String",
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