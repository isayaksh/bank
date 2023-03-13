package me.isayaksh.bank.repository;

import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.handler.ex.CustomApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql("classpath:db/teardown.sql")
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountRepositoryTest extends DummyObject {

    @Autowired MemberRepository memberRepository;
    @Autowired AccountRepository accountRepository;

    List<Long> numbers = Arrays.asList(7282L, 9000L, 5555L,1357L);

    @BeforeEach
    public void setup() {
        Member member = newMember("ssar", "쌀");
        memberRepository.save(member);

        for (Long number : numbers) {
            accountRepository.save(newAccount(number, 1234L, member));
        }


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

    @Test
    public void findAllByMemberId_test() throws Exception {
        // given

        // when
        Member member = memberRepository.findById(1L).orElseThrow(() -> new CustomApiException("해당 유저를 찾을 수 없습니다."));
        List<Account> accounts = accountRepository.findAllByMemberId(member.getId());

        // then
        for (Account account : accounts) {
            System.out.println("account = " + account.getNumber());
        }
    }

}