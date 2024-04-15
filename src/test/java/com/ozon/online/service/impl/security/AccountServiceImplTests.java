package com.ozon.online.service.impl.security;

import com.ozon.online.dto.user.ChangeNicknameDto;
import com.ozon.online.entity.User;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccountServiceImplTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void changePassword() {
    }

    @Test
    @DisplayName("Test changeNickname")
    void giveUserObjectToUpdate_whenChangeNickname_thenNicknameChangedAndSavedInDataBase() throws UserNotAuthException {

//        User user = User.builder()
//                .id(UUID.randomUUID())
//                .nickname("userName")
//                .nickname("userNickname")
//                .build();

        User user = new User();

        when(userService.getAuthenticationPrincipalUserByNickname()).thenReturn(Optional.of(user));

        ChangeNicknameDto changeNicknameDto = new ChangeNicknameDto();
        changeNicknameDto.setNickname("changedNickname");

        accountService.changeNickname(changeNicknameDto);

        assertEquals(user.getNickname(), "changedNickname");

    }

    @Test
    void addImage() {
    }
}