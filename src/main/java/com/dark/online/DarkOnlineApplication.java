package com.dark.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class DarkOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DarkOnlineApplication.class, args);
    }

}
