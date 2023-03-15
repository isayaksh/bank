package me.isayaksh.bank.controller;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.config.auth.LoginMember;
import me.isayaksh.bank.dto.ResponseDto;
import me.isayaksh.bank.dto.member.MemberReqDto;
import me.isayaksh.bank.dto.member.MemberReqDto.MemberJoinReqDto;
import me.isayaksh.bank.dto.member.MemberReqDto.MemberResetPasswordReqDto;
import me.isayaksh.bank.dto.member.MemberResDto;
import me.isayaksh.bank.dto.member.MemberResDto.MemberJoinRespDto;
import me.isayaksh.bank.dto.member.MemberResDto.MemberResetPasswordResDto;
import me.isayaksh.bank.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid MemberJoinReqDto joinReqDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), BAD_REQUEST);
        }

        MemberJoinRespDto savedMember = memberService.save(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", savedMember), CREATED);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid MemberResetPasswordReqDto memberResetPasswordReqDto, BindingResult bindingResult,
                                           @AuthenticationPrincipal LoginMember loginMember) {
        MemberResetPasswordResDto memberResetPasswordResDto = memberService.resetPassword(memberResetPasswordReqDto, loginMember.getMember().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "비밀번호 갱신 성공", memberResetPasswordResDto), OK);
    }

}
