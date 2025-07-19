package com.mritunjay.cineticket.service.auth;

import com.mritunjay.cineticket.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class JWTService {

    private final SecretKey secretKey;

    public JWTService() {
        KeyGenerator kg = getKeyGenerator();
        secretKey = kg.generateKey();
    }

    public KeyGenerator getKeyGenerator() {
        try{
            KeyGenerator kg = KeyGenerator.getInstance("HmacSHA256");
            kg.init(256);
            return kg;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateJWTToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claims(Map.of("ROLES", user.getAuthorities()))
                .issuedAt(new Date())
                .expiration(getExpiryDateForJWT())
                .signWith(secretKey)
                .compact();
    }

    public Date getExpiryDateForJWT() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 600);
        return calendar.getTime();
    }

    public Claims extractAllClaims(String jwt) {
        return (Claims) Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parse(jwt)
                .getPayload();
    }

    public String extractUserNameFromJwt(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

}
