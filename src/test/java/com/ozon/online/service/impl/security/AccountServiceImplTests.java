package com.ozon.online.service.impl.security;

import com.ozon.online.dto.user.ChangeNicknameDto;
import com.ozon.online.entity.User;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.mapper.ProductMapper;
import com.ozon.online.repository.UserAvatarRepository;
import com.ozon.online.repository.UserRepository;
import com.ozon.online.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTests {

    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserAvatarRepository userAvatarRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .nickname("userName")
                .nickname("userNickname")
                .build();
    }

    @Test
    void changePassword() {
    }

    @Test
    @DisplayName("Test changeNickname")
    void giveUserObjectToUpdate_whenChangeNickname_thenNicknameChangedAndSavedInDataBase() throws UserNotAuthException {
        when(userService.getAuthenticationPrincipalUserByNickname()).thenReturn(Optional.of(user));

        ChangeNicknameDto changeNicknameDto = new ChangeNicknameDto();
        changeNicknameDto.setNickname("changedNickname");

        accountService.changeNickname(changeNicknameDto);

        assertThat(user.getNickname()).isNotNull();
        assertEquals(user.getNickname(), "changedNickname");
    }

    @Test
    void giveUserObjectToUpdate_whenChangeImage_thenImageChangedAndSavedInDataBase() throws UserNotAuthException, IOException {
        when(userService.getAuthenticationPrincipalUserByNickname()).thenReturn(Optional.of(new User()));

        ResponseEntity<?> response = accountService.addImage(mock(MultipartFile.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("avatar added", ((ErrorResponse) Objects.requireNonNull(response.getBody())).message());
    }
}