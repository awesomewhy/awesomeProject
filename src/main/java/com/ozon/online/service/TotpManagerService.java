package com.ozon.online.service;

import dev.samstevens.totp.exceptions.QrGenerationException;

public interface TotpManagerService {
    String generateSecretKey();
    String getQRCode(String secret) throws QrGenerationException;
    boolean verifyTotp(String code, String secret);
}
