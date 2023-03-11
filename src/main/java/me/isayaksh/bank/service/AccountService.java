package me.isayaksh.bank.service;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.dto.account.AccountReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountDepositReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountRegisterReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountTransferReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountWithdrawReqDto;
import me.isayaksh.bank.dto.account.AccountResDto;
import me.isayaksh.bank.dto.account.AccountResDto.*;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.transaction.AccountStatus;
import me.isayaksh.bank.entity.transaction.Transaction;
import me.isayaksh.bank.handler.ex.CustomApiException;
import me.isayaksh.bank.repository.AccountRepository;
import me.isayaksh.bank.repository.MemberRepository;
import me.isayaksh.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static me.isayaksh.bank.entity.transaction.AccountStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountRegisterResDto register(Long memberId, AccountRegisterReqDto dto) {
        Member findMember = findMemberByMemberId(memberId);
        if(accountRepository.existsByNumber(dto.getNumber())){ throw new CustomApiException("등록하려는 계좌가 이미 존재합니다."); }
        Account savedAccount = accountRepository.save(dto.toEntity(findMember));
        return new AccountRegisterResDto(savedAccount);
    }

    public AccountListResDto findAllByMemberId(Long memberId) {
        Member findMember = findMemberByMemberId(memberId);
        List<Account> accounts = accountRepository.findAllByMemberId(findMember.getId());
        return new AccountListResDto(findMember, accounts);
    }

    @Transactional
    public void delete(Long number, Long memberId) {
        Account findAccount = findAccountByNumber(number);
        findAccount.checkOwner(memberId);
        accountRepository.deleteById(findAccount.getId());
    }

    @Transactional
    public AccountDepositResDto deposit(AccountDepositReqDto accountDepositReqDto) {
        if(accountDepositReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액은 입금할 수 없습니다.");
        }
        Account findDepositAccount = findAccountByNumber(accountDepositReqDto.getNumber());
        findDepositAccount.deposit(accountDepositReqDto.getAmount());
        Transaction transaction = Transaction.builder()
                .depositAccount(findDepositAccount)
                .withdrawAccount(null)
                .depositAccountBalance(findDepositAccount.getBalance())
                .withdrawAccountBalance(null)
                .amount(accountDepositReqDto.getAmount())
                .status(DEPOSIT)
                .sender("ATM")
                .receiver(findDepositAccount.getNumber().toString())
                .tel(accountDepositReqDto.getTel())
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new AccountDepositResDto(findDepositAccount, savedTransaction);
    }

    public AccountWithdrawResDto withdraw(AccountWithdrawReqDto accountWithdrawReqDto, Long memberId) {
        // 0. 출금액 확인하기
        if(accountWithdrawReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액은 출금할 수 없습니다.");
        }
        // 1. 계좌 찾기
        Account findWithdrawAccount = findAccountByNumber(accountWithdrawReqDto.getNumber());
        // 2. 계좌와 계좌 주인 일치 여부
        findWithdrawAccount.checkOwner(memberId);
        // 3. 비밀번호 일치 여부
        findWithdrawAccount.checkPassword(accountWithdrawReqDto.getPassword());
        // 4. 출금액과 잔액 비교 여부
        findWithdrawAccount.checkBalance(accountWithdrawReqDto.getAmount());
        // 5. 출금
        findWithdrawAccount.withdraw(accountWithdrawReqDto.getAmount());
        // 6. 출금 내역
        Transaction transaction = Transaction.builder()
                .depositAccount(null)
                .withdrawAccount(findWithdrawAccount)
                .depositAccountBalance(null)
                .withdrawAccountBalance(findWithdrawAccount.getBalance())
                .amount(accountWithdrawReqDto.getAmount())
                .status(WITHDRAW)
                .sender(findWithdrawAccount.getNumber().toString())
                .receiver("ATM")
                .build();
        // 6-1. 출금내역 저장
        Transaction savedTransaction = transactionRepository.save(transaction);
        // 7. DTO 응답
        return new AccountWithdrawResDto(findWithdrawAccount, savedTransaction);
    }

    public AccountTransferResDto transfer(AccountTransferReqDto accountTransferReqDto, Long memberId) {
        // 출금계좌 != 입금계좌
        if(accountTransferReqDto.getWithDrawNumber().equals(accountTransferReqDto.getDepositNumber())){
            throw new CustomApiException("입금계좌와 출금계좌가 동일할 순 없습니다.");
        }
        // 출금액 확인하기
        if(accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액은 출금할 수 없습니다.");
        }
        // 출금 계좌 조회
        Account withdrawAccount = findAccountByNumber(accountTransferReqDto.getWithDrawNumber());
        // 입금 계좌 조회
        Account depositAccount = findAccountByNumber(accountTransferReqDto.getDepositNumber());
        // 출금 계좌와 계좌 주인 일치 여부
        withdrawAccount.checkOwner(memberId);
        // 비밀번호 일치 여부
        withdrawAccount.checkPassword(accountTransferReqDto.getWithDrawPassword());
        // 출금액과 잔액 비교 여부
        withdrawAccount.checkBalance(accountTransferReqDto.getAmount());
        // 이체하기
        withdrawAccount.withdraw(accountTransferReqDto.getAmount());
        depositAccount.deposit(accountTransferReqDto.getAmount());
        // 출금 내역 생성
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(accountTransferReqDto.getAmount())
                .status(TRANSFER)
                .sender(withdrawAccount.getNumber().toString())
                .receiver(depositAccount.getNumber().toString())
                .build();
        // 출금 내역 저장
        Transaction savedTransaction = transactionRepository.save(transaction);

        return new AccountTransferResDto(withdrawAccount, savedTransaction);
    }

    private Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomApiException("해당 유저를 찾을 수 없습니다."));
    }

    private Account findAccountByNumber(Long number) {
        return accountRepository.findByNumber(number).orElseThrow(() -> new CustomApiException("해당 계좌를 찾을 수 없습니다."));
    }

}
