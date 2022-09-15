package org.daveo.producer;

import com.avroGenerator.Position;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class PositionProducer {

    private final KafkaTemplate<String, Position> producer;

    @Autowired
    public PositionProducer(KafkaTemplate<String, Position> producer) {
        this.producer = producer;
    }

    public void producePosition() {
        Position position = new Position();
        Random random = new Random();
        double longitude = random.nextDouble() * 180;
        double latitude = random.nextDouble() * 90;
        position.setLatitude(latitude);
        position.setLongitude(longitude);
        String key = latitude + "|" + longitude;
        //log.info("Latitude : " + latitude + " " + "Longitude : " + longitude);
        ProducerRecord<String, Position> record = new ProducerRecord<>("positions", key, position);
        producer.send(record);
    }
}
