package com.sensormanager.iot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorJSONPackageDTO {

    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("json_data")
    private List<Map<String, String>> jsonData;
}
