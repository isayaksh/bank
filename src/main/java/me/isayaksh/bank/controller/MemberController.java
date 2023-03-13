package me.isayaksh.bank.controller;

import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.dto.ResponseDto;
import me.isayaksh.bank.dto.member.MemberReqDto.JoinReqDto;
import me.isayaksh.bank.dto.member.MemberResDto.JoinRespDto;
import me.isayaksh.bank.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), BAD_REQUEST);
        }

        JoinRespDto savedMember = memberService.save(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", savedMember), CREATED);
    }

}
