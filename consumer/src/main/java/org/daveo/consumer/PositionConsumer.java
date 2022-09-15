package org.daveo.consumer;

import com.avroGenerator.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PositionConsumer {

    @KafkaListener(topics = "positions", groupId = "spring-consumer-5")
    public void listenPositions(Position position) {
        Double latitude = position.getLatitude();
        Double longitude = position.getLongitude();
        String key = latitude + "|" + longitude;
        log.info(key);
    }
}
