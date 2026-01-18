package org.panda.accountservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static String SECRET;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        JwtUtil.SECRET = secret;
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
