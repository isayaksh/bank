package me.isayaksh.bank.config.jwt;

import com.auth0.jwt.JWT;
import me.isayaksh.bank.config.auth.LoginMember;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.member.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired MockMvc mvc;

    @Test
    public void authorization_success_test() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.CUSTOMER).build();
        LoginMember loginMember = new LoginMember(member);
        String jwt = JwtProcess.create(loginMember);

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/hello").header(JwtVO.HEADER, jwt));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void authorization_fail_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/hello"));

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void authorization_admin_test() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.ADMIN).build();
        LoginMember loginMember = new LoginMember(member);
        String jwt = JwtProcess.create(loginMember);
        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/hello").header(JwtVO.HEADER, jwt));
        // then
        resultActions.andExpect(status().isNotFound());

        // given
        Member member1 = Member.builder().id(1L).role(MemberRole.CUSTOMER).build();
        LoginMember loginMember1 = new LoginMember(member1);
        String jwt1 = JwtProcess.create(loginMember1);
        // when
        ResultActions resultActions1 = mvc.perform(get("/api/admin/hello").header(JwtVO.HEADER, jwt1));
        // then
        resultActions1.andExpect(status().isForbidden());
    }
}