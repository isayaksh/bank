# 은행 업무 서비스 API 

#
## 🧷 개요
**은행 업무 서비스 API는 은행 업무에 필요한 다양한 서비스(입금, 송금, 계좌이체)를 API로 실행할 수 있게하는 RESTful API입니다.**</br>
**은행 업무에 대한 결과를 JSON 형식으로 반환합니다.**</br>
**API를 호출할 때는 계좌번호, 출입금, 계좌이체 정보 및 총액 정보를 JSON 형식의 데이터로 전달합니다.**</br>
**은행 업무 서비스 API를 호출할 때 HTTP 요청 헤더에 서버에서 제공한 JWT 값만 전송해 사용할 수 있는 API입니다.**

#
## 🧷 기술 스택

- Springboot 2.7.9
- JPA
- Security
- JWT
- H2
- QueryDSL

#
## 🧷 테이블 구조
<img alt="image" src="https://user-images.githubusercontent.com/85926257/224892792-54f75c8d-9829-4eca-8a4c-4dd3ad8787a5.png">

#
## 🧷 기능 정리

|기능|요청 URL|문서|테스트|
|:-:|:-:|:-:|:-:|
|회원가입|`{SERVER}/api/join`|[회원가입 doc](https://github.com/isayaksh/bank/blob/master/docs/join.md)|O|
|로그인|`{SERVER}/api/login`|[로그인 doc](https://github.com/isayaksh/bank/blob/master/docs/login.md)|O|
|계좌등록|`{SERVER}/api/s/account`|[계좌등록 doc](https://github.com/isayaksh/bank/blob/master/docs/registerAccount.md)|O|
|계좌목록 조회|`{SERVER}/api/s/account/login-user`|[계좌목록 조회 doc](https://github.com/isayaksh/bank/blob/master/docs/findAccountList.md)|O|
|계좌 삭제|`{SERVER}/api/s/account/{number}`|[계좌 삭제 doc](https://github.com/isayaksh/bank/blob/master/docs/deleteAccount.md)|O|
|입금|`{SERVER}/api/account/deposit`|[입금 doc](https://github.com/isayaksh/bank/blob/master/docs/deposit.md)|O|
|출금|`{SERVER}/api/s/account/withdraw`|[출금 doc](https://github.com/isayaksh/bank/blob/master/docs/withdraw.md)|O|
|계좌이체|`{SERVER}/api/s/account/transfer`|[계좌이체 doc](https://github.com/isayaksh/bank/blob/master/docs/transfer.md)|O|
|계좌 상세보기|`{SERVER}/api/s/account/{number}`|[계좌 상세보기 doc](https://github.com/isayaksh/bank/blob/master/docs/findAccountDetail.md)|O|
|계좌 비밀번호 갱신|`{SERVER}/api/s/account/password`|[계좌 비밀번호 갱신 doc](https://github.com/isayaksh/bank/blob/master/docs/resetPassword.md)|O|
|거래내역 조회|`{SERVER}/api/s/account/{number}/transactions`|[거래내역 조회 doc](https://github.com/isayaksh/bank/blob/master/docs/findTransactionList.md)|O|