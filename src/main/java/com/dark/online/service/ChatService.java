package com.dark.online.service;

import com.dark.online.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface ChatService {

    ResponseEntity<?> getSome();
    ResponseEntity<?> getAllChats();
    ResponseEntity<?> openChat(@RequestParam User userId);

}
