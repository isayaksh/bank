package me.isayaksh.bank.repository;

import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountRepositoryTest extends DummyObject {

    @Autowired MemberRepository memberRepository;
    @Autowired AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        Member member = newMember("ssar", "쌀");
        memberRepository.save(member);
        Account account = newAccount(7282L, 1234L, member);
        accountRepository.save(account);
    }

    @Test
    public void existsByNumberAndMemberId_test() throws Exception {
        // given

        // when
        Optional<Account> trueResult = accountRepository.findByNumber(7282L);
        Optional<Account> falseResult = accountRepository.findByNumber(1234L);
        // then
        assertTrue(trueResult.isPresent());
        assertFalse(falseResult.isPresent());
    }

}