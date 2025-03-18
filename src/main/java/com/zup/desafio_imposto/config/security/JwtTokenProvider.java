package com.zup.desafio_imposto.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final long TOKEN_VALIDITY = 3600 * 1000; // 1 hora
    private static final String ROLE_CLAIM = "role";

    public String getSecretKey() {
        String secretKey = System.getenv("JWT_SECRET_KEY");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("A variável de ambiente JWT_SECRET_KEY não está definida!");
        }
        return secretKey;
    }

    public String generateToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(ROLE_CLAIM, role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDITY);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) throws RuntimeException {
        try {
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token expired!");
        }
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
}
