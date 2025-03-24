package com.sensormanager.iot.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "companies")
public class Company {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	@Column(name = "company_api_key")
	private String companyApiKey;
	@Column(name = "company_name")
	private String companyName;
	@Column(name = "company_status")
	private Boolean companyStatus;
	@Column(name = "company_created_at")
	private Long companyCreatedAt;
	
	@PrePersist
    protected void onCreate() {
        this.companyCreatedAt = Instant.now().getEpochSecond();
    }
	
	 @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	 private List<Location> locations;
}
