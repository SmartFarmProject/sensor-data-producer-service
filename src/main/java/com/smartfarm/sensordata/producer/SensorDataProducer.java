package com.smartfarm.sensordata.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorDataProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Object msg) {
        kafkaTemplate.send("iot-data-event", msg);
        log.info("Sensor event was produced: " + msg);
    }
}
