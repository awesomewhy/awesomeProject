package com.ozon.online.validation;

import java.util.regex.Pattern;

public class Validation {
    public static boolean isValidEmailAddress(String email) {
        String emailRegexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(emailRegexp).matcher(email).matches();
    }

    public static boolean isValidPassword(String email) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        return email.matches(passwordRegex);
    }
}
