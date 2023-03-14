## 계좌 삭제

### Request
```

> DELETE /api/s/account/{number} HTTP/1.1
> Host: {SERVER}
> Accept: */*
> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
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