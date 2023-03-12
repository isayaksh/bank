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

    public TransactionListResDto find(Long memberId, Long accountNumber, String status , Pageable pageable) {
        Account accountPs = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new CustomApiException("해당 계좌가 존재하지 않습니다."));

        accountPs.checkOwner(memberId);

        List<Transaction> transactionList = transactionRepository.findTransactionList(accountPs.getId(), status, pageable.getPageNumber());
        return new TransactionListResDto(accountPs, transactionList);
    }

}
