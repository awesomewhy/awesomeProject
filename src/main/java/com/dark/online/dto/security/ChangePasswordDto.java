package com.dark.online.dto.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@NotBlank
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
    private String repeatPassword;
}

