package me.isayaksh.bank.config.jwt;

import me.isayaksh.bank.config.auth.LoginMember;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 검증 필터
**/
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(JwtVO.HEADER);

        if(isExist(header)) {
            String jwt = header.replace(JwtVO.TOKEN_PREFIX, ""); // "BEARER " 제거
            LoginMember loginMember = JwtProcess.verify(jwt);

            // 임시 세션 강제 주입 (생명주기 request ~ response)
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginMember, null, loginMember.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean isExist(String header) {
        return (header != null && header.startsWith(JwtVO.TOKEN_PREFIX));
    }
}
