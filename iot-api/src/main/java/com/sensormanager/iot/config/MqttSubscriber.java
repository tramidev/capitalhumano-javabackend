package com.sensormanager.iot.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class MqttSubscriber {

    private static final String BROKER = "tcp://localhost:1883"; // Dirección del servidor MQTT
    private static final String CLIENT_ID = "iot-mqtt-client";
    private static final String TOPIC = "iot/sensors"; // topic conceptual MQTT donde los sensores envían datos

    @PostConstruct
    public void subscribe() {
        try {
            MqttClient client = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
            client.connect();
            System.out.println("Conectado al broker MQTT: " + BROKER);
            client.subscribe(TOPIC, (topic, message) -> {
                String payload = new String(message.getPayload());
                System.out.println("Mensaje recibido en " + topic + ": " + payload);
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
