package me.isayaksh.bank.config.jwt;

import me.isayaksh.bank.config.auth.LoginMember;
import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.member.MemberRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class JwtProcessTest {
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
        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTY3ODk0ODg4NH0.CWFNUNOQm5GtQHY7LOHE0VHRt0cPHF8gqDeknXSIC6M4eZeQ0uvR_f2Mp23x_j81xzaaj_sEI0hvAOZMLCBMOQ";

        // when
        LoginMember loginMember = JwtProcess.verify(jwt);
        System.out.println("loginMember.getMember().getId() = " + loginMember.getMember().getId());
        System.out.println("loginMember.getMember().getRole() = " + loginMember.getMember().getRole());
        // then
        assertThat(loginMember.getMember().getId()).isEqualTo(1L);
        assertThat(loginMember.getMember().getRole()).isEqualTo(MemberRole.CUSTOMER);
    }
}