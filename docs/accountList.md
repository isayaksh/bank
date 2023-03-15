## 계좌 목록 조회

### Request
```

> GET /api/accounts/login-user HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/json; charset=UTF-8
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