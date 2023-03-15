## 계좌 비밀번호 갱신

### Request
```

> POST /api/accounts/reset-password HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/json; charset=UTF-8
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