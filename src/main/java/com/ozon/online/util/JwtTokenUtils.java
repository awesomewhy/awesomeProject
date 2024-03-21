package com.ozon.online.util;

import com.ozon.online.entity.RefreshToken;
import com.ozon.online.entity.User;
import com.ozon.online.repository.RefreshTokenRepository;
import com.ozon.online.repository.UserRepository;
import com.ozon.online.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_lifetime}")
    private Duration accessLifeTime;

    @Value("${jwt.refresh_lifetime}")
    private Duration refreshLifeTime;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = getClaimRoles(userDetails);

        RefreshToken refreshToken = userRepository.findByNickname(userDetails.getUsername())
                .map(User::getRefreshToken)
                .orElse(null);

        if (refreshToken == null || !verifyExpiration(refreshToken)) return null;

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + accessLifeTime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Map<String, Object> getClaimRoles(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roleList);
        return claims;
    }

    public RefreshToken createRefreshToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + refreshLifeTime.toMillis());

        Optional<User> userOptional = userRepository.findByNickname(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            return null;
        }

        RefreshToken existingRefreshToken = userOptional.get().getRefreshToken();
        if (verifyExpiration(existingRefreshToken)) {
            return existingRefreshToken;
        }

        String token = Jwts.builder()
                .setClaims(getClaimRoles(userDetails))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        if (existingRefreshToken == null) {
            RefreshToken refreshToken = RefreshToken.builder()
                    .token(token)
                    .user(userOptional.get())
                    .expiryDate(expiredDate.toInstant())
                    .build();
            return refreshTokenRepository.save(refreshToken);
        }

        existingRefreshToken.setToken(token);
        existingRefreshToken.setExpiryDate(expiredDate.toInstant());
        return refreshTokenRepository.save(existingRefreshToken);
    }

    public boolean verifyExpiration(RefreshToken token) {
        if (token == null) {
            return false;
        }

        Instant now = Instant.now();
        if (token.getExpiryDate().isBefore(now)) {
            refreshTokenRepository.deleteById(token.getId());
            refreshTokenRepository.flush();
            return false;
        }
        return true;
    }

    public String getNickname(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
