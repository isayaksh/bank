package me.isayaksh.bank.config.dummy;

import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static me.isayaksh.bank.entity.member.MemberRole.CUSTOMER;

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

    protected Account newMockAccount(Long id, Long number, Long password, Member member) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(password)
                .balance(1000L)
                .member(member)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

}
