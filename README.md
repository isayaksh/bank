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
|:-:|:-|:-|:-:|
|유저 회원가입|`{SERVER}/api/member/join`|[유저 회원가입 doc](https://github.com/isayaksh/bank/blob/master/docs/memberJoin.md)|O|
|유저 로그인|`{SERVER}/api/member/login`|[유저 로그인 doc](https://github.com/isayaksh/bank/blob/master/docs/memberLogin.md)|O|
|유저 비밀번호 갱신|`{SERVER}/api/s/member/password`|[유저 비밀번호 갱신 doc](https://github.com/isayaksh/bank/blob/master/docs/memberResetPassword.md)|O|
|계좌 등록|`{SERVER}/api/s/account`|[계좌 등록 doc](https://github.com/isayaksh/bank/blob/master/docs/accountRegister.md)|O|
|계좌 목록 조회|`{SERVER}/api/s/account/login-user`|[계좌 목록 조회 doc](https://github.com/isayaksh/bank/blob/master/docs/accountList.md)|O|
|계좌 삭제|`{SERVER}/api/s/account/{number}`|[계좌 삭제 doc](https://github.com/isayaksh/bank/blob/master/docs/accountDelete.md)|O|
|계좌 입금|`{SERVER}/api/account/deposit`|[계좌 입금 doc](https://github.com/isayaksh/bank/blob/master/docs/accountDeposit.md)|O|
|계좌 출금|`{SERVER}/api/s/account/withdraw`|[계좌 출금 doc](https://github.com/isayaksh/bank/blob/master/docs/accountWithdraw.md)|O|
|계좌 이체|`{SERVER}/api/s/account/transfer`|[계좌 이체 doc](https://github.com/isayaksh/bank/blob/master/docs/accountTransfer.md)|O|
|계좌 상세보기|`{SERVER}/api/s/account/{number}`|[계좌 상세보기 doc](https://github.com/isayaksh/bank/blob/master/docs/accountDetail.md)|O|
|계좌 비밀번호 갱신|`{SERVER}/api/s/account/password`|[계좌 비밀번호 갱신 doc](https://github.com/isayaksh/bank/blob/master/docs/accountResetPassword.md)|O|
|거래내역 조회|`{SERVER}/api/s/account/{number}/transactions`|[거래내역 조회 doc](https://github.com/isayaksh/bank/blob/master/docs/transactionList.md)|O|