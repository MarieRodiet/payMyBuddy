package com.oc.paymybuddy;

import com.oc.paymybuddy.entity.User_Account;
import com.oc.paymybuddy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class PaymybuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository){
		return args -> {
			userRepository.save(new User_Account(1, "qsdf@gmail.com", "password", "Moore", "Marie", "132321", BigDecimal.ONE));
			userRepository.save(new User_Account(1, "jkljkl@gmail.com", "password", "Moore", "Rob", "132321", BigDecimal.ONE));
			userRepository.save(new User_Account(1, "bnbnbn@gmail.com", "password", "Moore", "Lou", "132321", BigDecimal.ONE));
			userRepository.findAll().forEach(user -> System.out.println(user));
		};

	}

}
