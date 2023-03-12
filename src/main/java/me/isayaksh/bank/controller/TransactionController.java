package me.isayaksh.bank.controller;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.config.auth.LoginMember;
import me.isayaksh.bank.dto.ResponseDto;
import me.isayaksh.bank.dto.transaction.TransactionResDto.TransactionListResDto;
import me.isayaksh.bank.service.TransactionService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/s/account/{number}/transactions")
    public ResponseEntity<?> transactionList(@PathVariable Long number,
                                             @RequestParam(value = "status", defaultValue = "ALL") String status,
                                             Pageable pageable,
                                             @AuthenticationPrincipal LoginMember loginMember) {

        TransactionListResDto transactionListResDto = transactionService.find(loginMember.getMember().getId(), number, status, pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "거래 내역 조회 성공", transactionListResDto), HttpStatus.OK);
    }
}
