package me.isayaksh.bank.controller;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.config.auth.LoginMember;
import me.isayaksh.bank.dto.ResponseDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountDepositReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountRegisterReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountTransferReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountWithdrawReqDto;
import me.isayaksh.bank.dto.account.AccountResDto.*;
import me.isayaksh.bank.service.AccountService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/s/account")
    public ResponseEntity<?> register(@RequestBody @Valid AccountRegisterReqDto accountRegisterReqDto, BindingResult bindingResult,
                                      @AuthenticationPrincipal LoginMember loginMember) {
        AccountRegisterResDto accountRegisterResDto = accountService.register(loginMember.getMember().getId(), accountRegisterReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 등록 성공", accountRegisterResDto), HttpStatus.CREATED);
    }

    @GetMapping("/s/account/login-user")
    public ResponseEntity<?> findAccountList(@AuthenticationPrincipal LoginMember loginMember) {
        AccountListResDto accountListResDto = accountService.findAllByMemberId(loginMember.getMember().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 목록 불러오기 성공", accountListResDto), HttpStatus.OK);
    }

    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<?> deleteAccount(@PathVariable("number") Long number,
                                           @AuthenticationPrincipal LoginMember loginMember){
        accountService.delete(number, loginMember.getMember().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 성공", null), HttpStatus.OK);

    }

    @PostMapping("/account/deposit")
    public ResponseEntity<?> deposit(@RequestBody @Valid AccountDepositReqDto accountDepositReqDto,
                                     BindingResult bindingResult) {
        AccountDepositResDto accountDepositResDto = accountService.deposit(accountDepositReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "입금 성공", accountDepositResDto), HttpStatus.CREATED);
    }

    @PostMapping("/s/account/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody @Valid AccountWithdrawReqDto accountWithdrawReqDto, BindingResult bindingResult,
                                      @AuthenticationPrincipal LoginMember loginMember) {
        AccountWithdrawResDto accountWithdrawResDto = accountService.withdraw(accountWithdrawReqDto, loginMember.getMember().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "출금 성공", accountWithdrawResDto), HttpStatus.CREATED);
    }

    @PostMapping("/s/account/transfer")
    public ResponseEntity<?> transfer(@RequestBody @Valid AccountTransferReqDto accountTransferReqDto, BindingResult bindingResult,
                                      @AuthenticationPrincipal LoginMember loginMember) {
        AccountTransferResDto accountWithdrawResDto = accountService.transfer(accountTransferReqDto, loginMember.getMember().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "이체 성공", accountWithdrawResDto), HttpStatus.CREATED);
    }

    @GetMapping("/s/account/{number}/")
    public ResponseEntity<?> accountDetail(@PathVariable Long number,
                                           @AuthenticationPrincipal LoginMember loginMember,
                                           Pageable pageable) {

        AccountDetailResDto accountDetailResDto = accountService.findAccountDetail(number, loginMember.getMember().getId(), pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 상세보기 성공", accountDetailResDto), HttpStatus.OK);
    }

}
