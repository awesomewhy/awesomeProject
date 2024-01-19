package com.dark.online.util;

import com.dark.online.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //retrieve token
            String jwt = getJWT(request);
            if (Objects.nonNull(jwt)) {
                // Validate the JWT from the Request
                UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) jwtService.validateJwt(jwt);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (Exception e){
            log.error("Exception wile processing the JWT"+e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJWT(HttpServletRequest request){
        String jwt = request.getHeader("authorization");
        if(Objects.nonNull(jwt) && jwt.startsWith("Bearer") &&
                jwt.length()>7){
            return jwt.substring(7);
        }
        return null;
    }
}
