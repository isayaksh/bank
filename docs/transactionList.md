## 거래내역 조회

### Request
```

> GET /api/transactions/account/{number} HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/json; charset=UTF-8
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
