package me.isayaksh.bank.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.isayaksh.bank.config.auth.LoginMember;
import me.isayaksh.bank.dto.member.MemberReqDto.LoginReqDto;
import me.isayaksh.bank.dto.member.MemberResDto.MemberLoginResDto;
import me.isayaksh.bank.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login"); // 로그인 URL 지정
        this.authenticationManager = authenticationManager;
    }

    // POST: /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.debug("디버그 : attemptAuthentication 호출됨");

        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginReqDto loginReqDto = mapper.readValue(request.getInputStream(), LoginReqDto.class);

            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReqDto.getUsername(), loginReqDto.getPassword());
            // UserDetailsService.LoadByUsername 호출
            // JWT를 쓴다고 하더라도, 컨트롤러에 진입을 하면 시큐리티 권한 체크, 인증 체크의 도움을 받을 수 있게 세션을 만든다.
            // 이 세션의 유효기간은 request ~ response 까지이다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (Exception e) {
            // JwtAuthenticationFilter.unsuccessfulAuthentication 메서드 호출
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "로그인 실패", HttpStatus.UNAUTHORIZED);
    }

    // JwtAuthenticationFilter.attemptAuthentication 메서드가 잘 실행되면 successfulAuthentication 메서드 이어서 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        log.debug("디버그 : successfulAuthentication 호출됨");

        LoginMember loginMember = (LoginMember) authResult.getPrincipal();
        String jwt = JwtProcess.create(loginMember);
        response.addHeader(JwtVO.HEADER, jwt);
        MemberLoginResDto loginResDto = new MemberLoginResDto(loginMember.getMember());
        CustomResponseUtil.success(response, loginResDto);

    }
}
