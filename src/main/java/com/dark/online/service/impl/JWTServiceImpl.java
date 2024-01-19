package com.dark.online.service.impl;

import com.dark.online.service.JWTService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Duration;
import java.util.*;


@Service
public class JWTServiceImpl implements JWTService {

    private final String key = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

    @Override
    public String generateJwt(String username) throws ParseException {
        Date date= new Date();
        return  Jwts.builder()
                .setIssuer("MFA Server")
                .setSubject("JWT Auth Token")
                .claim("username", username)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + 60000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public Authentication validateJwt(String jwt) {
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(secretKey);
        Claims claims = jwtParser.parseClaimsJws(jwt).getBody();
        String username = (String)claims.getOrDefault("username",null);
        if(Objects.nonNull(username)){
            return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
        }
        return null;
    }

    //    @Override
//    public Authentication validateJwt(String jwt) {
//        JwtParser jwtParser = Jwts.parser()
//                .setSigningKey(secretKey);
//        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwt);
//        Claims claims = claimsJws.getBody();
//        String username = (String) claims.getOrDefault("username", null);
//        if (Objects.nonNull(username)) {
//            return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
//        }
//        return null;
//    }
}
