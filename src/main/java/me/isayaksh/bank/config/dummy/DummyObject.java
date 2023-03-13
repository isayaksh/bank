package me.isayaksh.bank.config.dummy;

import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.transaction.Transaction;
import me.isayaksh.bank.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static me.isayaksh.bank.entity.member.MemberRole.CUSTOMER;
import static me.isayaksh.bank.entity.transaction.AccountStatus.*;

public class DummyObject {
    protected Member newMember(String username, String fullName) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .email(username+"@email.com")
                .fullName(fullName)
                .role(CUSTOMER)
                .build();
    }

    protected Member newMockMember(Long id, String username, String fullName) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return Member.builder()
                .id(id)
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .email(username+"@email.com")
                .fullName(fullName)
                .role(CUSTOMER)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

    protected Account newAccount(Long number, Long password, Member member) {
        Account account = Account.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .member(member)
                .build();
        return account;
    }

    protected Account newMockAccount(Long id, Long number, Long balance, Member member) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .member(member)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockDepositTransaction(Long id, Account account) {
        account.deposit(100L);
        return Transaction.builder()
                .id(id)
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(100L)
                .status(DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber().toString())
                .tel("01012345678")
                .createAt(account.getCreateAt())
                .build();
    }

    protected Transaction newMockWithdrawTransaction(Long id, Account account) {
        account.deposit(100L);
        return Transaction.builder()
                .id(id)
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(100L)
                .status(WITHDRAW)
                .sender("ATM")
                .receiver(account.getNumber().toString())
                .tel("01012345678")
                .createAt(account.getCreateAt())
                .build();
    }

    protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
        account.deposit(100L);
        // 더티체킹이 안되기 때문에!!
        if (accountRepository != null) {
            accountRepository.save(account);
        }
        Transaction transaction = Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(100L)
                .status(DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber() + "")
                .tel("010-2222-7777")
                .build();
        return transaction;
    }

    protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
        account.withdraw(100L);
        // 더티체킹이 안되기 때문에!!
        if (accountRepository != null) {
            accountRepository.save(account);
        }
        Transaction transaction = Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(100L)
                .status(WITHDRAW)
                .sender(account.getNumber() + "")
                .receiver("ATM")
                .build();
        return transaction;
    }

    protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount,
                                                 AccountRepository accountRepository) {
        withdrawAccount.withdraw(100L);
        depositAccount.deposit(100L);
        // 더티체킹이 안되기 때문에!!
        if (accountRepository != null) {
            accountRepository.save(withdrawAccount);
            accountRepository.save(depositAccount);
        }

        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(100L)
                .status(TRANSFER)
                .sender(withdrawAccount.getNumber() + "")
                .receiver(depositAccount.getNumber() + "")
                .build();
        return transaction;
    }

}
