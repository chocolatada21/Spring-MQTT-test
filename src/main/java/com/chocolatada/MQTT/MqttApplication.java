package com.chocolatada.MQTT;

import com.chocolatada.MQTT.config.MQTTOutboundConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MqttApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(MqttApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		MQTTOutboundConfig.MyGateway gateway = context.getBean(MQTTOutboundConfig.MyGateway.class);
		gateway.sendToMqtt("OLA MQTT");
	}
}
