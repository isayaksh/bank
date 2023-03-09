package me.isayaksh.bank.service;

import lombok.*;
import me.isayaksh.bank.dto.ResponseDto;
import me.isayaksh.bank.dto.member.MemberReqDto;
import me.isayaksh.bank.dto.member.MemberReqDto.JoinReqDto;
import me.isayaksh.bank.dto.member.MemberResDto;
import me.isayaksh.bank.dto.member.MemberResDto.JoinRespDto;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.member.MemberRole;
import me.isayaksh.bank.handler.ex.CustomApiException;
import me.isayaksh.bank.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static me.isayaksh.bank.entity.member.MemberRole.CUSTOMER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public JoinRespDto save(JoinReqDto joinReqDto) {

        // 1. username 중복 검사 TODO memberRepository.existsByUsername 으로 변경할 것!
        memberRepository.findByUsername(joinReqDto.getUsername())
                .ifPresent(m -> {
                    throw new CustomApiException("동일한 username 이 존재합니다.");
                });

       // 2. 회원가입
        Member savedMember = memberRepository.save(joinReqDto.toEntity(passwordEncoder));

        // 3. dto 반환
        return new JoinRespDto(savedMember);

    }

}
