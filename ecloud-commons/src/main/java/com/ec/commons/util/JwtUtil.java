package com.ec.commons.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtil {

    private static final long EXPIRE_TIME = (long) (60 * 60 * 1000 * 24 * 10);

    private static final String ACCOUNT = "VUsernameZw01";
    private static final String USERID = "VKeyHix";
    private static final String NATIVEID = "nativeId";


    /**
     * 校验token是否正确
     *
     * @param token    密钥
     * @param username 用户名
     * @param secret   用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(ACCOUNT, username)
                    .build();
            //效验TOKEN
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的nativeId
     */
    public static String getNativeId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(NATIVEID).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 生成签名，具有时效性
     *
     * @param nativeId 用户唯一标识
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String nativeId,
                              String secret) {
        //设置有效日期
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim(ACCOUNT, "edward@l")
                .withClaim(USERID, "edward@x")
                .withClaim(NATIVEID, nativeId)
                .withExpiresAt(date)
                .sign(algorithm);
    }


}