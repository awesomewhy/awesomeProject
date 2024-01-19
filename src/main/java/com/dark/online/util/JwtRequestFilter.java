//package com.dark.online.util;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    private final JwtTokenUtils jwtTokenUtils;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        String email = null;
//        String jwt = null;
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            jwt = authHeader.substring(7);
//            try {
//                email = jwtTokenUtils.getEmail(jwt);
//            } catch (ExpiredJwtException e) {
//                log.debug("Время жизни токена вышло");
//            } catch (SignatureException e) {
//                log.debug("Подпись неправильная");
//            } catch (MalformedJwtException e) {
//                log.debug("Токен невалидный lol");
//            }
//        }
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UsernamePasswordAuthenticationToken token = createAuthenticationToken(email, jwt);
//            SecurityContextHolder.getContext().setAuthentication(token);
//        }
//        filterChain.doFilter(request, response);
//    }
//    private UsernamePasswordAuthenticationToken createAuthenticationToken(String email, String jwt) {
//        List<SimpleGrantedAuthority> authorities = jwtTokenUtils.getRoles(jwt)
//                .stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//
//        return new UsernamePasswordAuthenticationToken(email, null, authorities);
//    }
//
//}
//
