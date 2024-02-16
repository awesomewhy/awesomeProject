package com.dark.online.dto.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NotNull
@NotEmpty
@NotBlank
public class AccessAndRefreshResponseDto {
    private String accessToken;
    private String refreshToken;
}

