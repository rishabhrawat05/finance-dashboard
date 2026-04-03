package com.finance.finance_dashboard.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtility {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private static final long JWT_VALIDITY = 60 * 60;

    public String getEmailFromToken(String token) {
        String email = getClaimsFromToken(token).getSubject();
        return email;
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build().parseClaimsJws(token).getBody();
        return claims;
    }

    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expDate = claims.getExpiration();
        return expDate.before(new Date());
    }

    public String generateToken(UserDetails user) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("roles",
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_VALIDITY * 1000))
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()), SignatureAlgorithm.HS512)
                .compact();
    }
}
