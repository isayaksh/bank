package me.isayaksh.bank.service;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.dto.transaction.TransactionResDto.TransactionListResDto;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.transaction.Transaction;
import me.isayaksh.bank.handler.ex.CustomApiException;
import me.isayaksh.bank.repository.AccountRepository;
import me.isayaksh.bank.repository.TransactionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionListResDto findAll(Long memberId, Long accountNumber, String status , Pageable pageable) {
        // 계좌 번호로 계좌 조회
        Account findAccount = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new CustomApiException("해당 계좌가 존재하지 않습니다."));
        // 계좌와 소유자 일치 여부 검증
        findAccount.checkOwner(memberId);
        // 계좌의 거래 내역 조회
        List<Transaction> transactionList = transactionRepository.findTransactionList(findAccount.getId(), status, pageable.getPageNumber());
        // 계좌의 거래 내역 반환
        return new TransactionListResDto(findAccount, transactionList);
    }

}
