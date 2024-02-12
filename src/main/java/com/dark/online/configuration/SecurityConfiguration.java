package com.dark.online.configuration;

import com.dark.online.service.AuthService;
import com.dark.online.service.UserService;
import com.dark.online.util.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.jooq.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    // NOT AUTH

        // AUTH
    private static final String REGISTER = "/api/v1/auth/register";
    private static final String LOGIN = "/api/v1/auth/login";
    private static final String VERIFY_CODE = "/api/v1/auth/verifycode";
        // PRODUCT
    private static final String ALL_PRODUCTS = "/api/v1/product/products";
    private static final String CORRECT_PRODUCT = "/api/v1/product/correctproduct";
    private static final String SEARCH_PRODUCT = "/api/v1/product/search";
    private static final String SORT_PRODUCT = "/api/v1/product/sort";


    // USER
    private static final String CHANGE_NICKNAME = "/api/v1/safety/changenickname";
    private static final String CHANGE_PASSWORD = "/api/v1/safety/changepassword";
    private static final String CREATE2FA = "/api/v1/auth/create2FA";
    private static final String SET_AVATAR = "/api/v1/profile/set";
    private static final String GET_AVATAR = "/api/v1/profile/avatar";


    // PRODUCT
    private static final String CREATE_PRODUCT = "/api/v1/product/create";
    private static final String MY_PRODUCTS = "/api/v1/product/myproducts";

    // CHAT
    private static final String SEND_MESSAGE = "/api/v1/chat/chats/send";
    private static final String OPEN_CHAT = "/api/v1/chat/chats";
    private static final String MY_CHATS = "/api/v1/chat/my";
    private static final String DELETE_CHAT = "/api/v1/chat/delete/{id}";

    // REVIEW
    private static final String GET_MY_REVIEWS = "/profile/reviews";
    private static final String AVERAGE = "/user/average";
    private static final String ADD_REVIEW = "/user/create/{id}";

    // ADMIN
    private static final String USERS = "/admin/users";
    private static final String SET_USER_ROLE = "/userrole/{id}";
    private static final String SET_ADMIN_ROLE = "/adminrole/{id}";


    private final PasswordEncoderConfiguration passwordEncoderConfiguration;
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;
//    private CustomCorsConfiguration customCorsConfiguration;



    private void sharedSecurityConfiguration(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .securityMatcher("http://localhost:3000/**")
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });

    }

    @Bean
    public SecurityFilterChain securityFilterChainUsersAPI(HttpSecurity httpSecurity) throws Exception {
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity
                .securityMatcher(CREATE2FA, CHANGE_NICKNAME, SEND_MESSAGE, OPEN_CHAT, MY_CHATS, DELETE_CHAT,
                        CHANGE_PASSWORD, SET_AVATAR, GET_AVATAR, CREATE_PRODUCT, MY_PRODUCTS,
                        ADD_REVIEW, AVERAGE)
                .authorizeHttpRequests(auth -> {
//                    auth.anyRequest().authenticated();
                    auth.anyRequest().hasRole("USER");
                })
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChainModerAPI(HttpSecurity httpSecurity) throws Exception {
//        sharedSecurityConfiguration(httpSecurity);
//        httpSecurity
//                .securityMatcher()
//                .authorizeHttpRequests(auth -> {
//                    auth.anyRequest().hasRole("MODERATOR");
//                })
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChainAdminsAPI(HttpSecurity httpSecurity) throws Exception {
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity
                .securityMatcher(SET_ADMIN_ROLE, SET_USER_ROLE)
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().hasRole("ADMIN");
                })
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoderConfiguration.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.disable())
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers(SYS_ADMIN_PATTERNS).hasAuthority("SYSTEM_ADMIN")
//                        .requestMatchers("/api/v1/**").authenticated()
//                )
//                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }

    //    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        // Configure the mail sender properties
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//        mailSender.setUsername("your-email-username");
//        mailSender.setPassword("your-email-password");
//        // Additional configuration if needed
//        return mailSender;
//    }

}

