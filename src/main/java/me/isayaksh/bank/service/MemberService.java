package me.isayaksh.bank.service;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.dto.member.MemberReqDto.JoinReqDto;
import me.isayaksh.bank.dto.member.MemberResDto.JoinRespDto;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.handler.ex.CustomApiException;
import me.isayaksh.bank.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public JoinRespDto save(JoinReqDto joinReqDto) {
        // username 중복 검사
        if(memberRepository.existsByUsername(joinReqDto.getUsername())) {
            throw new CustomApiException("동일한 username이 존재합니다.");
        }
        // 회원가입
        Member savedMember = memberRepository.save(joinReqDto.toEntity(passwordEncoder));
        // 회원가입 결과 반환
        return new JoinRespDto(savedMember);
    }

}
