package com.tealer.tealerspringboot.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    /**
     * 从配置文件读取密钥
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * Token 有效期（毫秒）
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 从Token中提取用户名
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从Token中提取过期时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 通用方法：从Token中提取任意信息
     * @param token
     * @param claimsResolver
     * @return
     * @param <T>
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析Token的全部Claims(有效载荷)
     * @param token
     * @return
     */
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


}
