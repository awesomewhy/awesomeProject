package com.ozon.online.dto.mfa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MfaTokenData {
    private String qrCode;
    private String mfaCode;
}
