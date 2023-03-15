## 계좌 삭제

### Request
```

> DELETE /api/accounts/{number} HTTP/1.1
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
  "msg": "계좌 삭제 성공",
  "data": null
}
```