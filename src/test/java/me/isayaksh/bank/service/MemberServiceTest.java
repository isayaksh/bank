package me.isayaksh.bank.service;

import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.dto.member.MemberReqDto.JoinReqDto;
import me.isayaksh.bank.dto.member.MemberResDto.JoinRespDto;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest extends DummyObject {

    @InjectMocks
    private MemberService memberService;

    @Mock // 가상으로 만듬
    private MemberRepository memberRepository;

    @Spy // spring Bean 에서 직접 가져옴
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void 회원가입() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
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
        JoinRespDto save = memberService.save(joinReqDto);

        // then

        assertThat(save.getId()).isEqualTo(1L);
        assertThat(save.getUsername()).isEqualTo("username");
        assertThat(save.getFullName()).isEqualTo("userFullName");

    }

}