package com.dark.online.service.impl.user;

import com.dark.online.dto.order.CreateOrderDto;
import com.dark.online.dto.user.RegistrationUserDto;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.RoleService;
import com.dark.online.service.TotpManagerService;
import com.dark.online.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl
        implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpManagerService totpManagerService;
    private final RoleService roleService;

    @Override
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        Optional<User> userOptional = getAuthenticationPrincipalUserByNickname();

        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user in spring context not found / user not auth"));
        }

        return null;
    }

    @Override
    public Optional<User> getAuthenticationPrincipalUserByNickname() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        String nickname = (String) authentication.getPrincipal();
        return userRepository.findByNickname(nickname);
    }

    @Override
    public void createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        User user = User.builder()
                .password(passwordEncoder.encode(registrationUserDto.getPassword()))
                .nickname(registrationUserDto.getNickname())
                .secretKey(totpManagerService.generateSecretKey())
                .balance(BigDecimal.ZERO)
                .username("First name")
                .surname("Last name")
                .createdAt(LocalDateTime.now())
                .roles(List.of(roleService.getUserRole().get()))
                .build();
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("user with nickname %s not found", nickname)
                ));
        return new org.springframework.security.core.userdetails.User(
                user.getNickname(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
        );
    }

}
