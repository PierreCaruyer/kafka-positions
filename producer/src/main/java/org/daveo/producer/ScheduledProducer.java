package org.daveo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class ScheduledProducer {

    private final PositionProducer producer;

    @Autowired
    public ScheduledProducer(PositionProducer producer) {
        this.producer = producer;
    }

    @Scheduled(fixedDelay = 1)
    public void main() {
        producer.producePosition();
    }
}
