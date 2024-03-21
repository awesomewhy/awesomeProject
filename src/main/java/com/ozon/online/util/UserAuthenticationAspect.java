//package com.ozon.online.util;
//
//import com.ozon.online.annotations.UserAuthenticationRequired;
//import com.ozon.online.entity.User;
//import com.ozon.online.exception.UserNotAuthException;
//import com.ozon.online.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//public class UserAuthenticationAspect {
////    private UserService userService;
////
////
////    //useless method
////    @Before("@annotation(UserAuthenticationRequired)")
////    public void authenticateUser() throws UserNotAuthException {
////        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
////                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
////        );
////        // @UserAuthenticationRequired
////    }
//}
