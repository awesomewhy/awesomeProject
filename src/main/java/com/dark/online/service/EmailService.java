package com.dark.online.service;

import com.dark.online.entity.EmailConfirmationToken;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendConfirmationEmail(EmailConfirmationToken emailConfirmationToken) throws MessagingException;
}
