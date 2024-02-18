package com.ozon.online.dto.security;

import lombok.Data;

@Data
public class LoginRequestDto {
    String nickname;
    String password;
}
