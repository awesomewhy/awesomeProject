package com.dark.online.dto.mfa;

import lombok.Data;

@Data
public class MfaVerificationRequest {
    private String username;
    private String totp;
}
