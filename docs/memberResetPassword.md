## 유저 비밀번호 갱신

### Request
```

> POST /api/members/reset-password HTTP/1.1
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
  "msg": "비밀버호 갱신 성공",
  "data": {
    "id": Long,
    "username": "String",
    "fullName": "String"
  }
}
```