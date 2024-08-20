package com.lamdangfixbug.qmshoe.auth.service;

import com.lamdangfixbug.qmshoe.user.entity.Token;
import com.lamdangfixbug.qmshoe.user.entity.TokenType;
import com.lamdangfixbug.qmshoe.user.repository.TokenRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private final TokenRepository tokenRepository;

    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        Token t = tokenRepository.findByToken(token).orElse(null);
        if(!extractClaims(token, Claims::getSubject).equals(userDetails.getUsername())) return false;
        if(t==null) return false;
        return !extractClaims(token,Claims::getExpiration).before(new Date()) && !t.isRevoke();
    }

    public String generateToken(UserDetails user, TokenType tokenType) {
        Date expiration = switch (tokenType){
            case ACCESS_TOKEN -> new Date(new Date().getTime() + 1000 * 60 * 60);
            case REFRESH_TOKEN -> new Date(new Date().getTime() + 1000 * 60 * 60 * 7);
            case FORGOT_PASSWORD_TOKEN -> new Date(new Date().getTime() + 1000 * 60);
            default -> new Date();
        };
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(getSignKey())
                .compact();
    }

    public SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(JWT_SECRET));
    }
}
