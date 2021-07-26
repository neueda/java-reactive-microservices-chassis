package com.neueda.microservice.reactive.chassis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@EnableTransactionManagement
public class ReactiveMicroserviceApplication {

	public static void main(String[] args) {
		BlockHound.install();
		SpringApplication.run(ReactiveMicroserviceApplication.class, args);
	}

}
