package com.dark.online.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@NotBlank
public class RegistrationUserDto {
    private String nickname;
    private String password;
    private String confirmPassword;
}
