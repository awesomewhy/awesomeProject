package com.dark.online.dto;

import lombok.Data;

@Data
public class MfaVerificationRequest {
    private String username;
    private String totp;
}
