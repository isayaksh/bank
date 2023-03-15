## 출금

### Request
```

> POST /api/s/account/withdraw HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/json; charset=UTF-8
> Authorization: {JWT}

```

#### Request Body
```
{
  "number" : Long,
  "amount": Long,
  "status": String,
  "tel" : String
}
```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "출금 성공",
  "data": {
    "id": Long,
    "number": Long,
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