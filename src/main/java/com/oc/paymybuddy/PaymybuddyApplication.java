package com.oc.paymybuddy;

import com.oc.paymybuddy.entity.RecipientList;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.repository.RecipientListRepository;
import com.oc.paymybuddy.repository.TransactionRepository;
import com.oc.paymybuddy.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootApplication
public class PaymybuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(
			UserAccountRepository userAccountRepository,
			TransactionRepository transactionRepository,
			RecipientListRepository recipientListRepository){
		return args -> {
			userAccountRepository.save(new UserAccount(null, "marie.moore@gmail.com", "password123", "Moore", "Marie", "100", BigDecimal.ONE));
			userAccountRepository.save(new UserAccount(null, "robert.moore@gmail.com", "password123", "Moore", "Rob", "101", BigDecimal.ONE));
			userAccountRepository.save(new UserAccount(null, "lou.moore@gmail.com", "password123", "Moore", "Lou", "103", BigDecimal.ONE));
			userAccountRepository.findAll().forEach(user -> System.out.println(user.getEmail()));

			transactionRepository.save(new Transaction(null, 1, 2, new BigDecimal(10), new Date(), "Restaurant bill share"));
			transactionRepository.save(new Transaction(null, 1, 2, new BigDecimal(25), new Date(), "Trip money"));
			transactionRepository.save(new Transaction(null, 1, 2, new BigDecimal(8), new Date(), "Movie tickets"));
			transactionRepository.findAll();

			recipientListRepository.save(new RecipientList(null, 1, 2));

		};

	}

}
