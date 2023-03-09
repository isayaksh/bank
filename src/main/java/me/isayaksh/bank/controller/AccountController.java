package me.isayaksh.bank.controller;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.config.auth.LoginMember;
import me.isayaksh.bank.dto.ResponseDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountRegisterReqDto;
import me.isayaksh.bank.dto.account.AccountResDto.AccountRegisterResDto;
import me.isayaksh.bank.service.AccountService;
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

    @PostMapping("/s/register")
    public ResponseEntity<?> register(@RequestBody @Valid AccountRegisterReqDto accountRegisterReqDto, BindingResult bindingResult,
                                      @AuthenticationPrincipal LoginMember loginMember) {
        AccountRegisterResDto accountRegisterResDto = accountService.register(loginMember.getMember().getId(), accountRegisterReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 등록 성공", accountRegisterResDto), HttpStatus.CREATED);
    }

}
