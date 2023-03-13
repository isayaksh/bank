package me.isayaksh.bank.config.jwt;

public interface JwtVO {

    // TODO SECRET 은 노출되면 안된다.
    String SECRET = "secretKey"; // HS256 대칭키
    int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 일주일
    String TOKEN_PREFIX = "BEARER ";
    String HEADER = "AUTHORIZATION";

}
