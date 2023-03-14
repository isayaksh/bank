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