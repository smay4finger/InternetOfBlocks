package net.foojiyama.minecraft;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class PubSubClient implements MqttCallback {

    private final String topicPrefix;
    private final MqttClient mqttClient;
    private final Logger log;

    public PubSubClient(String url, String topicPrefix, Logger log) {
        this.topicPrefix = topicPrefix;
        this.log = log;

        try {
            mqttClient = new MqttClient(url, "InternetOfBlocks");
            mqttClient.setCallback(this);
        } catch (MqttException e) {
            throw new RuntimeException("Huh?");
        }
    }

    public void connect() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setWill(this.topicPrefix + "/status", "offline".getBytes(),
                    0, true);
            mqttClient.connect(options);
            mqttClient.publish(topicPrefix + "/status", "online".getBytes(), 0, true);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mqttClient.publish(topicPrefix + "/status", "offline".getBytes(), 0, true);
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setQos(0);
            mqttMessage.setRetained(true);
            mqttMessage.setPayload(message.getBytes());
            mqttClient.publish(topicPrefix + "/" + topic, mqttMessage);
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable arg0) {
        log.severe("connection lost");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        log.info("delivery complete");
    }

    @Override
    public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
        log.info("message arrived");
    }
}
