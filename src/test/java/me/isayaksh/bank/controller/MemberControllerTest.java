package me.isayaksh.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.dto.member.MemberReqDto.JoinReqDto;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.repository.MemberRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@AutoConfigureMockMvc()
@SpringBootTest(webEnvironment = MOCK)
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
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("newUser");
        joinReqDto.setEmail("qwerty@gmail.com");
        joinReqDto.setPassword("1234");
        joinReqDto.setFullName("abc");

        String content = mapper.writeValueAsString(joinReqDto);
        System.out.println("content = " + content);

        // when
        ResultActions resultActions = mvc.perform(post("/api/join").content(content).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("contentAsString = " + contentAsString);
    }

    @Test
    public void join_fail_test() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("qwe");
        joinReqDto.setEmail("user@gmail.com");
        joinReqDto.setPassword("1234");
        joinReqDto.setFullName("userFullName");

        String content = mapper.writeValueAsString(joinReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/join").content(content).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
    }

    private void dataSetting() throws Exception{
        memberRepository.save(newMember("username", "userFullName"));
    }

    private void dataClear() throws Exception{
        memberRepository.deleteAll();
    }
}