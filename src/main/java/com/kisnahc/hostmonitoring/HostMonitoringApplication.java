package com.kisnahc.hostmonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HostMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostMonitoringApplication.class, args);
	}

}
