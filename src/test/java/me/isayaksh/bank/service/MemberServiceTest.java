package me.isayaksh.bank.service;

import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.dto.member.MemberReqDto.MemberJoinReqDto;
import me.isayaksh.bank.dto.member.MemberResDto.MemberJoinRespDto;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest extends DummyObject {

    @InjectMocks
    private MemberService memberService;

    @Mock // 가상으로 만듬
    private MemberRepository memberRepository;

    @Spy // spring Bean 에서 직접 가져옴
    private BCryptPasswordEncoder passwordEncoder;
    
    @Test
    public void join() throws Exception {
        // given
        MemberJoinReqDto joinReqDto = new MemberJoinReqDto();
        joinReqDto.setUsername("username");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("userId@email.com");
        joinReqDto.setFullName("userFullName");

        // stub
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());
        // when(memberRepository.findByUsername(any())).thenReturn(Optional.of(new Member()));


        Member member = newMockMember(1L, "username", "userFullName");
        when(memberRepository.save(any())).thenReturn(member);

        // when
        MemberJoinRespDto save = memberService.save(joinReqDto);

        // then

        assertThat(save.getId()).isEqualTo(1L);
        assertThat(save.getUsername()).isEqualTo("username");
        assertThat(save.getFullName()).isEqualTo("userFullName");

    }

    @Test
    public void resetPassword_test() throws Exception {
        // given
        Member member = newMockMember(1L, "kim", "mj");
        String password = passwordEncoder.encode("1234");
        String newPassword = passwordEncoder.encode("4321");
        System.out.println("member.getPassword() = " + member.getPassword());
        // when
        member.checkId(1L);
        member.checkPassword(password);
        member.updatePassword(newPassword);
        System.out.println("member.getPassword() = " + member.getPassword());
        // then
        assertTrue(passwordEncoder.matches("4321", member.getPassword()));
    }

}