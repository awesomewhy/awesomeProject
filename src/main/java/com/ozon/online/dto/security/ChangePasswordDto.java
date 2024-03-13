package com.ozon.online.dto.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotBlank
@NotNull
@NotBlank
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
    private String repeatPassword;
}

