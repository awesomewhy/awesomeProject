package com.ozon.online.service.impl.security;

import com.ozon.online.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private UserRepository userRepository;

    @Value("${jwt.refresh_lifetime}")
    private Duration refreshLifeTome;

    @Value("${jwt.secret}")
    private String secret;

//    public RefreshToken createRefreshToken(UserDetails userDetails) {
//        String refreshToken = Jwts.builder()
//                .setSubject(userDetails.getUsername()) // nickname
//                .setIssuedAt(refreshLifeTome)
//                .setExpiration(expiredDate)
//                .signWith(SignatureAlgorithm.HS256, secret)
//                .compact();
//        return refreshTokenRepository.save(refreshToken);
//    }
//
//
////    public Optional<RefreshToken> findByToken(String token) {
////        return refreshTokenRepository.findByToken(token);
////    }
//
//
//    public boolean verifyExpiration(RefreshToken token) {
//        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//            refreshTokenRepository.delete(token);
//            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
//        }
//        return true;
//    }
}
