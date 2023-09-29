package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.Role;
import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionServiceTests {

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;
    private static Page<Transaction> transactionsPage;

    private static Transaction transaction;
    private static UserAccount sender;
    private static UserAccount recipient;
    private static PageRequest sortedPageRequest;

    @BeforeAll
    public static void setUpBeforeClass(){
        sender = new UserAccount(
                        1,
                        "marie@gmail.com",
                        "password",
                        "marieLASTNAME",
                        "marieFIRSTNAME",
                        "ÀÀÀAAAA",
                        new BigDecimal(100),
                        Role.USER.name());
        recipient = new UserAccount(
                    2,
                    "bob@gmail.com",
                    "password",
                    "marieLASTNAME",
                    "marieFIRSTNAME",
                    "ÀÀÀAAAA",
                    BigDecimal.ONE,
                    Role.USER.name());
        transaction = new Transaction(
                1,
                sender,
                recipient,
                new BigDecimal(10),
                new Date(),
                "description"
                );
        transactionsPage = new PageImpl<>(Arrays.asList(transaction));
        sortedPageRequest = PageRequest.of(
                0,
                10
        );

    }

    /*
    @WithMockUser(authorities = "USER")
    @Test
    public void savingTransactionShouldEnableMeToRetrieveAllTransactionsBySender(){
        when(this.transactionRepository.save(transaction)).thenReturn(null);
        Transaction savedTransaction = this.transactionService.saveTransaction(transaction);
        System.out.println(savedTransaction);
        when(this.transactionRepository.findTransactionsBySender(sender, sortedPageRequest)).thenReturn(transactionsPage);
        Page<Transaction> result = this.transactionService.getTransactionsBySender(sender, sortedPageRequest);
        System.out.println(result);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }*/

    @WithMockUser(authorities = "USER")
    @Test
    public void testSaveTransaction() {
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        assertNotNull(savedTransaction);
        assertEquals(BigDecimal.TEN, savedTransaction.getAmount());

    }

    @Test
    public void testCreateTransaction() {
        Transaction newTransaction = transactionService.createTransaction(sender, recipient, BigDecimal.ONE, "description");

        assertNotNull(newTransaction);
        assertEquals(sender, newTransaction.getSender());
        assertEquals(recipient, newTransaction.getRecipient());
        assertEquals(BigDecimal.ONE, newTransaction.getAmount());
        assertEquals("description", newTransaction.getDescription());
    }
}
