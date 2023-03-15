package me.isayaksh.bank.service;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.dto.member.MemberReqDto;
import me.isayaksh.bank.dto.member.MemberReqDto.MemberJoinReqDto;
import me.isayaksh.bank.dto.member.MemberReqDto.MemberResetPasswordReqDto;
import me.isayaksh.bank.dto.member.MemberResDto;
import me.isayaksh.bank.dto.member.MemberResDto.MemberJoinRespDto;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.handler.ex.CustomApiException;
import me.isayaksh.bank.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static me.isayaksh.bank.dto.member.MemberResDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public MemberJoinRespDto save(MemberJoinReqDto joinReqDto) {
        // username 중복 검사
        if(memberRepository.existsByUsername(joinReqDto.getUsername())) {
            throw new CustomApiException("동일한 username이 존재합니다.");
        }
        // 회원가입
        Member savedMember = memberRepository.save(joinReqDto.toEntity(passwordEncoder));
        // 회원가입 결과 반환
        return new MemberJoinRespDto(savedMember);
    }

    @Transactional
    public MemberResetPasswordResDto resetPassword(MemberResetPasswordReqDto memberResetPasswordReqDto, Long memberId) {
        Member findMember = memberRepository.findByUsername(memberResetPasswordReqDto.getUsername()).orElseThrow(() -> new CustomApiException("해당 유저가 존재하지 않습니다."));
        findMember.checkId(memberId);
        findMember.checkPassword(passwordEncoder.encode(memberResetPasswordReqDto.getPassword()));
        findMember.updatePassword(passwordEncoder.encode(memberResetPasswordReqDto.getNewPassword()));
        return new MemberResetPasswordResDto(findMember);
    }

}
