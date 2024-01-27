package com.dark.online.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeNicknameDto {
    @NotBlank
    private String nickname;
}
