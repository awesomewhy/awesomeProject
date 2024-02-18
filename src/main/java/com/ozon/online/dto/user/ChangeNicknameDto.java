package com.ozon.online.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
@NotEmpty
@NotBlank
public class ChangeNicknameDto {
    private String nickname;
}
