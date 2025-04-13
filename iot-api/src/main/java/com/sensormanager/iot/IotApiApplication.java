package com.sensormanager.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;

@SpringBootApplication(exclude = {ActiveMQAutoConfiguration.class})
public class IotApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotApiApplication.class, args);
	}

}
