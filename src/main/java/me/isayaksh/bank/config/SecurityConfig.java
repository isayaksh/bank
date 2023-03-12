package me.isayaksh.bank.config;

import me.isayaksh.bank.config.jwt.JwtAuthenticationFilter;
import me.isayaksh.bank.config.jwt.JwtAuthorizationFilter;
import me.isayaksh.bank.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static me.isayaksh.bank.entity.member.MemberRole.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean // IOC 컨테이너에 BCryptPasswordEncoder 객체 등록
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록");
        return new BCryptPasswordEncoder();
    }

    // JWT 필터 생성
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            // 필터 생성!
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain 빈 등록됨");
        // iframe 허용 안함
        http.headers().frameOptions().disable();
        // CSRF 공격 허용 → Postman 사용을 위해
        http.csrf().disable();
        http.cors().configurationSource(configurationSource());
        // JSession id를 서버쪽에서 관리하지 않는다.
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        // form 으로 전달받지 않고, 외부 요청(react, android, ...)만 받는다.
        http.formLogin().disable();
        // httpBasic()은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
        http.httpBasic().disable();

        // JWT 필터 등록
        http.apply(new CustomSecurityFilterManager());

        // 인증 실패
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            CustomResponseUtil.fail(response, "로그인을 진행해 주세요", HttpStatus.UNAUTHORIZED);
        });

        // 권한 실패
        http.exceptionHandling().accessDeniedHandler(((request, response, e) -> {
            CustomResponseUtil.fail(response, "권한이 없습니다.", HttpStatus.FORBIDDEN);
        }));

        http.authorizeRequests()
                .antMatchers("/api/s/**").authenticated()
                .antMatchers("/api/admin/**").hasRole(""+ADMIN)
                .anyRequest().permitAll();

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : configurationSource cors 설정이 securityFilter 에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용(*) → 프론 IP 주소만 허용(?)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 서버에서 제공하는 response 의 헤더 "Authorization" 에 대한 접근을 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
