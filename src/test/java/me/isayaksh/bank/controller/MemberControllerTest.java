package me.isayaksh.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.config.jwt.JwtVO;
import me.isayaksh.bank.dto.member.MemberReqDto;
import me.isayaksh.bank.dto.member.MemberReqDto.MemberJoinReqDto;
import me.isayaksh.bank.dto.member.MemberReqDto.MemberLoginReqDto;
import me.isayaksh.bank.dto.member.MemberReqDto.MemberResetPasswordReqDto;
import me.isayaksh.bank.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MemberControllerTest extends DummyObject {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private MemberRepository memberRepository;

    @BeforeEach
    public void setup() throws Exception {
        dataSetting();
    }

    @AfterEach
    public void setDown() throws Exception {
        dataClear();
    }

    @Test
    public void join_success_test() throws Exception {
        // given
        MemberJoinReqDto joinReqDto = new MemberJoinReqDto();
        joinReqDto.setUsername("newUser");
        joinReqDto.setEmail("qwerty@gmail.com");
        joinReqDto.setPassword("1234");
        joinReqDto.setFullName("abc");

        String content = mapper.writeValueAsString(joinReqDto);
        System.out.println("content = " + content);

        // when
        ResultActions resultActions = mvc.perform(post("/api/members/join").content(content).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("contentAsString = " + contentAsString);
    }

    @Test
    public void join_fail_test() throws Exception {
        // given
        MemberJoinReqDto joinReqDto = new MemberJoinReqDto();
        joinReqDto.setUsername("qwe");
        joinReqDto.setEmail("user@gmail.com");
        joinReqDto.setPassword("1234");
        joinReqDto.setFullName("userFullName");

        String content = mapper.writeValueAsString(joinReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/members/join").content(content).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void login_test() throws Exception {
        // given
        MemberLoginReqDto memberLoginReqDto = new MemberLoginReqDto();
        memberLoginReqDto.setUsername("ssar");
        memberLoginReqDto.setPassword("1234");
        String requestBody = mapper.writeValueAsString(memberLoginReqDto);
        // when
        ResultActions resultActions = mvc.perform(post("/api/members/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String jwt = resultActions.andReturn().getResponse().getHeader("Authorization");
        System.out.println("jwt = " + jwt);
        // then
        Assertions.assertTrue(jwt.startsWith(JwtVO.TOKEN_PREFIX));
        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void resetPassword_test() throws Exception {
        // given
        MemberResetPasswordReqDto memberResetPasswordReqDto = new MemberResetPasswordReqDto();
        memberResetPasswordReqDto.setUsername("ssar");
        memberResetPasswordReqDto.setPassword("1234");
        memberResetPasswordReqDto.setNewPassword("4321");
        String requestBody = mapper.writeValueAsString(memberResetPasswordReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/members/reset-password").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        // then
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);
        resultActions.andExpect(status().isOk());
    }

    private void dataSetting() throws Exception{
        memberRepository.save(newMember("ssar", "userFullName"));
    }

    private void dataClear() throws Exception{
        memberRepository.deleteAll();
    }
}