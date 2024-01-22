package com.dark.online.service;

import com.dark.online.dto.user.DeleteProfileDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public interface DeleteService {
    ResponseEntity<?> deleteProfile(@RequestBody DeleteProfileDto deleteProfileDto);
}
