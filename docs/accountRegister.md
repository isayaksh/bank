## 계좌 등록

### Request
```

> POST /api/accounts/register HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/json; charset=UTF-8
> Authorization: {JWT}

```

#### Request Body
```
{
  "number" : Long,
  "password" : Long,
}
```

### Response

#### Response Body
```
{
  "code": 1,
  "msg": "계좌등록 성공",
  "data": {
    "id": Long,
    "number": Long,
    "balance": Long
  }
}
```