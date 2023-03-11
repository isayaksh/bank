package me.isayaksh.bank.config.dummy;

import me.isayaksh.bank.dto.account.AccountReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountDepositReqDto;
import me.isayaksh.bank.dto.account.AccountResDto;
import me.isayaksh.bank.dto.account.AccountResDto.AccountDepositResDto;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.transaction.Transaction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static me.isayaksh.bank.entity.member.MemberRole.CUSTOMER;
import static me.isayaksh.bank.entity.transaction.AccountStatus.DEPOSIT;
import static me.isayaksh.bank.entity.transaction.AccountStatus.WITHDRAW;

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
        return Account.builder()
                .number(number)
                .password(password)
                .balance(1000L)
                .member(member)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
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

}
