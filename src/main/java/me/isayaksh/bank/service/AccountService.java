package me.isayaksh.bank.service;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.dto.account.AccountReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountRegisterReqDto;
import me.isayaksh.bank.dto.account.AccountResDto;
import me.isayaksh.bank.dto.account.AccountResDto.AccountRegisterResDto;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.handler.ex.CustomApiException;
import me.isayaksh.bank.repository.AccountRepository;
import me.isayaksh.bank.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public AccountRegisterResDto register(Long memberId, AccountRegisterReqDto dto) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new CustomApiException("해당 유저를 찾을 수 없습니다."));

        if(accountRepository.existsByNumber(dto.getNumber())){ throw new CustomApiException("등록하려는 계좌가 이미 존재합니다."); }

        Account savedAccount = accountRepository.save(dto.toEntity(findMember));

        return new AccountRegisterResDto(savedAccount);
    }
}
