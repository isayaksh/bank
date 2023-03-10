package me.isayaksh.bank.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.transaction.Transaction;
import me.isayaksh.bank.util.CustomDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountResDto {

    @Getter
    @Setter
    public static class AccountRegisterResDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountRegisterResDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    @Getter
    @Setter
    public static class AccountListResDto {
        private String fullName;
        private List<AccountDto> accounts = new ArrayList<>();
        public AccountListResDto(Member member, List<Account> accounts) {
            this.fullName = member.getFullName();
            this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

    @Getter @Setter
    public static class AccountDepositResDto {
        private Long id;
        private Long number;
        private TransactionDto transactionDto;

        public AccountDepositResDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transactionDto = new TransactionDto(transaction);
        }

        @Getter @Setter
        public class TransactionDto {
            private Long id;
            private String status;
            private String sender;
            private String receiver;
            private Long amount;
            private String tel;
            private String createdAt;

            @JsonIgnore
            private Long depositAmountBalance;
            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.status = transaction.getStatus().getStatus();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAmountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreateAt());
            }
        }
    }

    @Getter @Setter
    public static class AccountWithdrawResDto {
        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transactionDto;

        public AccountWithdrawResDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transactionDto = new TransactionDto(transaction);
        }

        @Getter @Setter
        public class TransactionDto {
            private Long id;
            private String status;
            private String sender;
            private String receiver;
            private Long amount;
            private String tel;
            private String createdAt;
            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.status = transaction.getStatus().getStatus();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreateAt());
            }
        }
    }

}
