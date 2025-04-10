package com.sensormanager.iot.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensormanager.iot.dto.SensorJSONPackageDTO;
import com.sensormanager.iot.service.SensorDataServiceImp;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQSensorDataProcess implements Processor{
    
	@Autowired
    private SensorDataServiceImp sensorDataService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void process(Exchange exchange) throws Exception {
        objectMapper.findAndRegisterModules();
        String jsonBody = exchange.getIn().getBody(String.class);
        try {
            SensorJSONPackageDTO sensorData = objectMapper.readValue(jsonBody, SensorJSONPackageDTO.class);
            sensorDataService.createSensorData(sensorData);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Json format for SensorJSONPackage", e);
        }
    }
}
