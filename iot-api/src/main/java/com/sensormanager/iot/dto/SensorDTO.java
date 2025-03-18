package com.sensormanager.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SensorDTO {
	private Long id;
	private String sensorName;
	private String sensorApiKey;
	private Long sensorLocation;
	private Long sensorCompany;
	private Long sensorCreatedAt;
	private Boolean sensorStatus;
}
