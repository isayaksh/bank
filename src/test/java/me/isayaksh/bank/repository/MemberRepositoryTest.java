package me.isayaksh.bank.repository;

import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.handler.ex.CustomApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberRepositoryTest extends DummyObject {

    @Autowired MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        Member m1 = newMember("son", "hm");
        Member m2 = newMember("kim", "mj");
        memberRepository.save(m1);
        memberRepository.save(m2);
    }

    @Test
    void findByUsername_test() {
        Member m1 = memberRepository.findByUsername("son").orElseThrow(() -> new CustomApiException("username에 해당하는 유저가 존재하지 않습니다."));
        Member m2 = memberRepository.findByUsername("kim").orElseThrow(() -> new CustomApiException("username에 해당하는 유저가 존재하지 않습니다."));

        assertThat(m1.getFullName()).isEqualTo("hm");
        assertThat(m2.getFullName()).isEqualTo("mj");
    }

    @Test
    void existsByUsername_test() {
        assertTrue(memberRepository.existsByUsername("son"));
        assertTrue(memberRepository.existsByUsername("kim"));
        assertFalse(memberRepository.existsByUsername("lee"));
    }
}