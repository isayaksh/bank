## 계좌 상세보기

### Request
```

> GET /api/accounts/{number} HTTP/1.1
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