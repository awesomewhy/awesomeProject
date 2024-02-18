package com.ozon.online.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@NotNull
@NotEmpty
@NotBlank
public class RegistrationUserDto {
    private String nickname;
    private String password;
    private String confirmPassword;
}
