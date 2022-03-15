package com.authentication.jwtauth.service;

import com.authentication.jwtauth.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JWT_Utils implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 10000;
    @Value("ae4c837c249a3ef39462a1538203f518ba421cbde2267f85189effa06b128f717854322967a568bb33b4f77a72c698f4f1dfc6cbab39459710f63849cb143fc5")
    private String accessSecret;

    public String generateAccessToken(Optional<UserModel> user) {
        Map<String, Object> claims = new HashMap<>();
        if (user.isPresent()) {
            claims.put("name", user.get().getName());
            claims.put("email", user.get().getEmail());
            return doGenerateToken(claims, user.get().getName());
        }
        return null;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, accessSecret).compact();
    }

    public Boolean validateToken(String token, UserModel userModel) {
        final String email = getUserNameFromToken(token);
        return (email.equals(userModel.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(accessSecret).parseClaimsJws(token).getBody();
    }

    private Date getExpirationDate(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
}
