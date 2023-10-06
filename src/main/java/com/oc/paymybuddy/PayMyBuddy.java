package com.oc.paymybuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PayMyBuddy {

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddy.class, args);
	}
}
