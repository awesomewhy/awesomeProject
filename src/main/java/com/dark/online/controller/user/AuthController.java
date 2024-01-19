package com.dark.online.controller.user;

import com.dark.online.dto.mfa.MfaVerificationRequest;
import com.dark.online.dto.mfa.MfaVerificationResponse;
import com.dark.online.dto.security.LoginRequestDto;
import com.dark.online.dto.security.RegistrationUserDto;
import com.dark.online.entity.User;
import com.dark.online.service.TotpManagerService;
import com.dark.online.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationProvider authenticationProvider;
    private final TotpManagerService totpManagerService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto) {
        return ResponseEntity.ok(userService.registerUserByQrCode(registrationUserDto));
    }

    @PostMapping("/asd")
    public ResponseEntity<?> reg(@Validated @RequestBody String a) {
        // Register User // Generate QR code using the Secret KEY
        try {
            System.out.println(totpManagerService.getQRCode(
                    totpManagerService.generateSecretKey()));
            return ResponseEntity.ok().body(totpManagerService.getQRCode(
                    totpManagerService.generateSecretKey()));
        } catch (QrGenerationException e) {
            return ResponseEntity.internalServerError().body("Something went wrong. Try again.");
        }
    }


    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDto loginRequest) {
        // Validate the user credentials and return the JWT / send redirect to MFA page
//        try {//Get the user and Compare the password
//            Authentication authentication = authenticationProvider.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            User user = (User) authentication.getPrincipal();
//            return ResponseEntity.ok(MfaVerificationResponse.builder()
//                    .username(loginRequest.getUsername())
//                    .tokenValid(Boolean.FALSE)
//                    .authValid(Boolean.TRUE)
//                    .mfaRequired(Boolean.TRUE)
//                    .message("User Authenticated using username and Password")
//                    .jwt("")
//                    .build());
//
//        } catch (Exception e) {
//            return ResponseEntity.ok(MfaVerificationResponse.builder()
//                    .username(loginRequest.getUsername())
//                    .tokenValid(Boolean.FALSE)
//                    .authValid(Boolean.FALSE)
//                    .mfaRequired(Boolean.FALSE)
//                    .message("Invalid Credentials. Please try again.")
//                    .jwt("")
//                    .build());
//        }
        return ResponseEntity.ok(MfaVerificationResponse.builder()
                    .username(loginRequest.getUsername())
                    .tokenValid(Boolean.FALSE)
                    .authValid(Boolean.FALSE)
                    .mfaRequired(Boolean.FALSE)
                    .message("Invalid Credentials. Please try again.")
                    .jwt("")
                    .build());
    }

    @PostMapping("/verifyTotp")
    public ResponseEntity<?> verifyTotp(@Validated @RequestBody MfaVerificationRequest request) {
        MfaVerificationResponse mfaVerificationResponse = MfaVerificationResponse.builder()
                .username(request.getUsername())
                .tokenValid(Boolean.FALSE)
                .message("Token is not Valid. Please try again.")
                .build();

        if (userService.verifyTotp(request.getTotp(), request.getUsername())) {
            mfaVerificationResponse = MfaVerificationResponse.builder()
                    .username(request.getUsername())
                    .tokenValid(Boolean.TRUE)
                    .message("Token is valid")
                    .build();
        }
        return ResponseEntity.ok(mfaVerificationResponse);
    }
}
