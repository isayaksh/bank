package me.isayaksh.bank.config.jwt;

public interface JwtVO {

    // TODO SECRET 은 노출되면 안된다.
    public static final String SECRET = "secretKey"; // HS256 대칭키
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 일주일
    public static final String TOKEN_PREFIX = "BEARER ";
    public static final String HEADER = "AUTHORIZATION";

}
