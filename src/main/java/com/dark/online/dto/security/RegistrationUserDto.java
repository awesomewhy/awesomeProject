package com.dark.online.dto.security;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String nickname;
    private String email;
    private String password;
    private String repeatPassword;
}
