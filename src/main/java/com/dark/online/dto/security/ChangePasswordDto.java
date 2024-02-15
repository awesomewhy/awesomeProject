package com.dark.online.dto.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@NotBlank
@NotNull
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
    private String repeatPassword;
}

