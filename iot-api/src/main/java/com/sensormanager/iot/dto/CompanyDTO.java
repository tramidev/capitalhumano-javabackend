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
public class CompanyDTO {	
	private Long id;	
	private String companyApiKey;
	private String companyName;
	private Boolean companyStatus;
	private Long companyCreatedAt;
}
