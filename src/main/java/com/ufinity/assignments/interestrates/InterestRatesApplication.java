package com.ufinity.assignments.interestrates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ufinity.assignments")
public class InterestRatesApplication {

  public static void main(String[] args) {
		SpringApplication.run(InterestRatesApplication.class, args);
	}
}
