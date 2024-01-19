package com.dark.online.dto.mfa;

import lombok.Data;

@Data
public class MfaVerificationRequest {
    private String nickname;
    private String code;
}
