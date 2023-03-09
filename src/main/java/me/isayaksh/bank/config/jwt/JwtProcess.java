package me.isayaksh.bank.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.isayaksh.bank.config.auth.LoginMember;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.member.MemberRole;
import me.isayaksh.bank.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtProcess {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // create JWT
    public static String create(LoginMember loginMember) {

        Member member = loginMember.getMember();

        String jwt = JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
                .withClaim("id", member.getId())
                .withClaim("role", member.getRole().toString())
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        return JwtVO.TOKEN_PREFIX + jwt;
    }

    // verify JWT (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public static LoginMember verify(String jwt) {
        DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(jwt);
        Long id = decodedJwt.getClaim("id").asLong();
        String role = decodedJwt.getClaim("role").asString();
        Member member = Member.builder().id(id).role(MemberRole.valueOf(role)).build();
        LoginMember loginMember = new LoginMember(member);
        return loginMember;
    }

}
