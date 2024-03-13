package com.ozon.online.service.impl.user;

import com.ozon.online.dto.order.CreateOrderDto;
import com.ozon.online.dto.user.RegistrationUserDto;
import com.ozon.online.entity.Role;
import com.ozon.online.entity.User;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.mapper.UserMapper;
import com.ozon.online.repository.UserRepository;
import com.ozon.online.repository.UserAvatarRepository;
import com.ozon.online.service.RoleService;
import com.ozon.online.service.TotpManagerService;
import com.ozon.online.service.UserService;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl
        implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpManagerService totpManagerService;
    private final RoleService roleService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        User userOptional = getAuthenticationPrincipalUserByNickname().orElseThrow();
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
        Role role = roleService.getUserRole().orElseThrow();
        userMapper.mapRegistrationUserDtoToUserEntityAndSave(registrationUserDto, role);
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
