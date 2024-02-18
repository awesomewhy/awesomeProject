package com.ozon.online.service;

import com.ozon.online.dto.user.DeleteProfileDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface DeleteService {
    ResponseEntity<?> deleteProfile(@RequestBody DeleteProfileDto deleteProfileDto);
}
