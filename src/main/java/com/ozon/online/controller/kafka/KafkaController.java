package com.ozon.online.controller.kafka;

import com.ozon.online.dto.kafka.KafkaTestDto;
import com.ozon.online.service.impl.kafka.KafkaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaServiceImpl kafkaService;

    @PostMapping("/kafka")
    public void sendMessage(@RequestBody KafkaTestDto kafkaTestDto) {
        kafkaService.sendMessage(kafkaTestDto);
    }

}
