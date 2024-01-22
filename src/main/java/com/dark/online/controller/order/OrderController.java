package com.dark.online.controller.order;

import com.dark.online.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;
}
