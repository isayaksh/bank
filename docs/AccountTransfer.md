## 계좌 이체

### Request
```

> POST /api/s/account/transfer HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/json; charset=UTF-8
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