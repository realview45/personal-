package com.example.chatserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey;

    private final int expiration;
    private final Key SECRET_KEY;
    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey, @Value("${jwt.expiration}")int expiration){
        this.secretKey = secretKey;
        this.expiration = expiration;
        this.SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
    }
    public String createToken(String email, String role){
        Claims claims = Jwts.claims().setSubject(email);//대푯값
        claims.put("role", role);//나머지 사용자정보
        Date now = new Date();
        String token = Jwts.builder()
//               아래 3가지 요소는 페이로드
                .setClaims(claims)
                .setIssuedAt(now)//발행시간
                .setExpiration(new Date(now.getTime()+expiration*60*1000L))//유효시간
//              secret키를 통해 서명값(signature) 생성
                .signWith(SECRET_KEY)
                .compact();
        return token;
    }
}
