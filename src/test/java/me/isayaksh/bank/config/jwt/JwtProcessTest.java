package me.isayaksh.bank.config.jwt;

import me.isayaksh.bank.config.auth.LoginMember;
import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.member.MemberRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class JwtProcessTest extends DummyObject{

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Test
    public void create_test() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.CUSTOMER).build();
        LoginMember loginMember = new LoginMember(member);

        // when
        String jwt = JwtProcess.create(loginMember);

        // then
        assertTrue(jwt.startsWith(JwtVO.TOKEN_PREFIX));
    }

    @Test
    public void verify_test() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.CUSTOMER).build();
        LoginMember loginMember = new LoginMember(member);

        // when
        String jwt = JwtProcess.create(loginMember).replace(JwtVO.TOKEN_PREFIX, "");

        // when
        LoginMember loginMember1 = JwtProcess.verify(jwt);
        System.out.println("loginMember.getMember().getId() = " + loginMember1.getMember().getId());
        System.out.println("loginMember.getMember().getRole() = " + loginMember1.getMember().getRole());

        // then
        assertThat(loginMember1.getMember().getId()).isEqualTo(1L);
        assertThat(loginMember1.getMember().getRole()).isEqualTo(MemberRole.CUSTOMER);
    }
}