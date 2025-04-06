package com.sensormanager.iot.camel;
import com.sensormanager.iot.service.UserService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQSensorRoute extends RouteBuilder {

    @Autowired
    private UserService userService;

    private String queueName = "clase06-grupo05-1";

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .log("❌ Error processing package: ${exception.message}")
                .handled(true) // don't propagate error
                .to("log:error");


        from("activemq:queue:"+queueName)
                .log("📥 Message Recieved: ${body}")
                .process("activeMQSensorDataProcess")
                .log("✅ Message processed correctly");
    }
}
