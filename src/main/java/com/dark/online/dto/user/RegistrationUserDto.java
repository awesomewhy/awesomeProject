package com.dark.online.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegistrationUserDto {
    private String nickname;
    private String password;
    private String confirmPassword;
}
