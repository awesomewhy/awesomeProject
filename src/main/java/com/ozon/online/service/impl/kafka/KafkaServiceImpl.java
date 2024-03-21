package com.ozon.online.service.impl.kafka;

import com.ozon.online.dto.kafka.KafkaTestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl {

    private final KafkaTemplate<String, KafkaTestDto> kafkaTemplate;

    public void sendMessage(@RequestBody KafkaTestDto kafkaTestDto) {
        kafkaTemplate.send("qwe", kafkaTestDto);
    }

}
