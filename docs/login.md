## 로그인

### Request
```

> POST /api/login HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/json; charset=UTF-8

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