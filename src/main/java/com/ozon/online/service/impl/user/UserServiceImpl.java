package com.ozon.online.service.impl.user;

import com.ozon.online.dto.order.CreateOrderDto;
import com.ozon.online.dto.user.RegistrationUserDto;
import com.ozon.online.entity.Role;
import com.ozon.online.entity.User;
import com.ozon.online.exception.ErrorResponse;
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
    private final UserAvatarRepository userAvatarRepository;

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
//        Optional<User_Avatar> userAvatar = userAvatarRepository.findById(1L);
//        if(userAvatar.isEmpty()) {
//            log.info("user avatar not found");
//            return;
//        }

        // if you delete all db then you need to add this in table
        //insert into roles (id, name)
        //values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

        Optional<Role> role = roleService.getUserRole();
        if(role.isEmpty()) {
            log.info("role not found");
            return;
        }
        User user = User.builder()
                .password(passwordEncoder.encode(registrationUserDto.getPassword()))
                .nickname(registrationUserDto.getNickname())
                .secretKey(totpManagerService.generateSecretKey())
                .balance(BigDecimal.ZERO)
                .username("First name")
                .surname("Last name")
                .createdAt(LocalDateTime.now())
                .roles(List.of(role.get()))
                .build();
//        userAvatar.get().setUserId(user);
//        user.setAvatarId(userAvatar.get());
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
