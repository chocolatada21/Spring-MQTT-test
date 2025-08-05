package com.chocolatada.MQTT.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MQTTOutboundConfig {
    // Config para enviar mensajes al broker MQTT

    // Config para los clientes que creemos y que desean enviar mensajes al server MQTT
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { "tcp://localhost:2025" });
//		options.setUserName("username");
//		options.setPassword("password".toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    // Config final para los clientes, indico el id con el que se va a identificar en el broker MQTT y el topico al cual va a escribir
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler("senderClient", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("myFirstTopic");
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    // Con la anotacion MessagingGateway me engancho al canal de enviar mensajes y uso una interfaz que abstrae la logica de enviar mensajes al broker
    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MyGateway {

        void sendToMqtt(String data);

    }
}
