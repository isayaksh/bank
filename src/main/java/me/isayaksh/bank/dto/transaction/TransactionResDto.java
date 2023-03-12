package me.isayaksh.bank.dto.transaction;

import lombok.Getter;
import lombok.Setter;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.transaction.Transaction;
import me.isayaksh.bank.util.CustomDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionResDto {
    @Getter
    @Setter
    public static class TransactionListResDto {

        public TransactionListResDto(Account account, List<Transaction> transactions) {
            this.transactions = transactions.stream()
                    .map(t -> new TransactionDto(t, account.getNumber()))
                    .collect(Collectors.toList());
        }

        private List<TransactionDto> transactions = new ArrayList<>();

        @Getter @Setter
        public class TransactionDto{
            private Long id;
            private Long amount;
            private Long balance;
            private String status;
            private String createdAt;
            private String sender;
            private String receiver;
            private String tel;

            public TransactionDto(Transaction transaction, Long accountNumber) {
                this.id = transaction.getId();
                this.amount = transaction.getAmount();
                this.status = transaction.getStatus().getStatus();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreateAt());
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.tel = transaction.getTel();


                if( transaction.getDepositAccount() != null && transaction.getWithdrawAccount() != null) {
                    // 계좌이체(TRANSFER)
                    this.balance = (transaction.getDepositAccount().equals(accountNumber)) ? transaction.getDepositAccountBalance() : transaction.getWithdrawAccountBalance();
                } else {
                    // 입금(DEPOSIT), 출금(WITHDRAW)
                    this.balance = (transaction.getDepositAccount() == null) ? transaction.getWithdrawAccountBalance() : transaction.getDepositAccountBalance();
                }

            }
        }
    }
}
