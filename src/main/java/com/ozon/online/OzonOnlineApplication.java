package com.ozon.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class OzonOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(OzonOnlineApplication.class, args);
    }

}
