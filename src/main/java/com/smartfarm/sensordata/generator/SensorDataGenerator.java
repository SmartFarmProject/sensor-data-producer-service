package com.smartfarm.sensordata.generator;

import com.smartfarm.sensordata.producer.SensorDataProducer;
import com.smartfarm.sensordata.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.smartfarm.sensordata.model.IdGenerator.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorDataGenerator implements CommandLineRunner {

    @Value(value = "${sensor.min-delay}")
    private Integer minDelay;

    @Value(value = "${sensor.max-delay}")
    private Integer maxDelay;

    private final SensorDataProducer sensorDataProducer;

    @Override
    public void run(String... args) throws Exception {
        generateEvents();
    }

    private static List<AirHumiditySensor> generateAirHumiditySensor(Random random) {
        List<AirHumiditySensor> events = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            AirHumiditySensor event = AirHumiditySensor.builder()
                    .id(AIR_HUMIDITY_ID_LIST.get(random.nextInt(20)))
                    .value(String.valueOf(random.nextInt(90 - 5) + 5)) // from 5 to 90
                    .unit("%")
                    .build();
            events.add(event);
        }
        return events;
    }

    private static List<LightSensor> generateLightSensor(Random random) {
        List<LightSensor> events = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            LightSensor event = LightSensor.builder()
                    .id(LIGHT_SENSOR_ID_LIST.get(random.nextInt(20)))
                    .lightIntensity(random.nextInt(900 - 50) + 50)
                    .batteryLevel(random.nextInt(100 - 1) + 1)
                    .signalStrength(random.nextInt(100 - 30) + 30)
                    .unit("cd")
                    .build();
            events.add(event);
        }
        return events;
    }

    private static List<PhSoilSensor> generatePhSoilSensor(Random random) {
        List<PhSoilSensor> events = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PhSoilSensor event = PhSoilSensor.builder()
                    .id(PH_SOIL_ID_LIST.get(random.nextInt(20)))
                    .phLevel(random.nextInt(14))
                    .isCalibrated(random.nextBoolean())
                    .batteryLevel(random.nextInt(100 - 1) + 1)
                    .signalStrength(random.nextInt(100 - 30) + 30)
                    .unit("ph")
                    .build();
            events.add(event);
        }
        return events;
    }

    private static List<SoilHumiditySensor> generateSoilHumiditySensor(Random random) {
        List<String> firmwareVersions = Arrays.asList("version: 1", "version: 2", "version: 3");
        List<SoilHumiditySensor> events = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SoilHumiditySensor event = SoilHumiditySensor.builder()
                    .id(SOIL_HUMIDITY_ID_LIST.get(random.nextInt(20)))
                    .humidity(random.nextInt(90 - 5) + 5)
                    .signalStrength(random.nextInt(100 - 30) + 30)
                    .firmwareVersion(firmwareVersions.get(random.nextInt(3)))
                    .unit("%")
                    .build();
            events.add(event);
        }
        return events;
    }

    private static List<TemperatureSensor> generateTemperatureSensor(Random random) {
        List<TemperatureSensor> events = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TemperatureSensor event = TemperatureSensor.builder()
                    .id(TEMPERATURE_SENSOR_ID_LIST.get(random.nextInt(20)))
                    .temperature(random.nextInt(60 - 10) + 10)
                    .batteryLevel(random.nextInt(100 - 1) + 1)
                    .unit("c")
                    .build();
            events.add(event);
        }
        return events;
    }

    private void generateEvents() throws InterruptedException {
        int minDelayTime = minDelay;
        int maxDelayTime = maxDelay;
        Random random = new Random();
        log.info("Sending sensor events");
        while (true) {
            List<AirHumiditySensor> airHumiditySensors = generateAirHumiditySensor(random);
            List<LightSensor> lightSensors = generateLightSensor(random);
            List<PhSoilSensor> phSoilSensors = generatePhSoilSensor(random);
            List<SoilHumiditySensor> soilHumiditySensors = generateSoilHumiditySensor(random);
            List<TemperatureSensor> temperatureSensors = generateTemperatureSensor(random);

            for (int i = 0; i < 20; i++) {
                AirHumiditySensor airHumidityEvent = airHumiditySensors.get(i);
                airHumidityEvent.setTimestamp(new Date());
                sensorDataProducer.sendMessage(airHumidityEvent);
                Thread.sleep(random.nextInt(maxDelayTime - minDelayTime) + maxDelayTime);

                LightSensor lightSensorEvent = lightSensors.get(i);
                lightSensorEvent.setTimestamp(new Date());
                sensorDataProducer.sendMessage(lightSensorEvent);
                Thread.sleep(random.nextInt(maxDelayTime - minDelayTime) + maxDelayTime);

                PhSoilSensor phSoilSensorEvent = phSoilSensors.get(i);
                phSoilSensorEvent.setTimestamp(new Date());
                sensorDataProducer.sendMessage(phSoilSensorEvent);
                Thread.sleep(random.nextInt(maxDelayTime - minDelayTime) + maxDelayTime);

                SoilHumiditySensor soilHumiditySensorEvent = soilHumiditySensors.get(i);
                soilHumiditySensorEvent.setTimestamp(new Date());
                sensorDataProducer.sendMessage(soilHumiditySensorEvent);
                Thread.sleep(random.nextInt(maxDelayTime - minDelayTime) + maxDelayTime);

                TemperatureSensor temperatureSensorEvent = temperatureSensors.get(i);
                temperatureSensorEvent.setTimestamp(new Date());
                sensorDataProducer.sendMessage(temperatureSensorEvent);
                Thread.sleep(random.nextInt(maxDelayTime - minDelayTime) + maxDelayTime);
            }
        }
    }
}
