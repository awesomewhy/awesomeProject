package com.dark.online.dto.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NotNull
@NotEmpty
@NotBlank
public class JwtResponseDto {
    private String token;
}

