package com.path.variable.mqtt.pub.demo.publisher;

import org.eclipse.paho.client.mqttv3.MqttClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MqttPublisher {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final MqttClient client;

    public MqttPublisher(String url, String clientId) {
        try {
            client = new MqttClient(url, clientId);
            client.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    
    }
    public void publish(Object object) {
        try {
            var json = MAPPER.writeValueAsString(object);
            client.publish("temperature", json.getBytes(), 0, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
