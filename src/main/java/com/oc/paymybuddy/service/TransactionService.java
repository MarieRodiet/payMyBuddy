package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The TransactionService is responsible for managing transactions of a user account.
 * This service class provides methods for getting, saving and creating Transaction Objects.
 */
@Service
@AllArgsConstructor
@Transactional
public class TransactionService {

    private TransactionRepository transactionRepository;

    public Page<Transaction> getTransactionsBySender(UserAccount userAccount, PageRequest pageRequest){
        Sort sortByDate = Sort.by(Sort.Order.desc("date"));
        PageRequest sortedPageRequest = PageRequest.of(
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                sortByDate
        );

        Page<Transaction> transactions = transactionRepository.findTransactionsBySender(userAccount, sortedPageRequest);
        return transactions;
    }

    public Transaction saveTransaction(Transaction newTransaction) {
        return transactionRepository.save(newTransaction);
    }

    public Transaction createTransaction(UserAccount sender, UserAccount recipient, BigDecimal amount, String description){
        Transaction newTransaction = new Transaction();
        newTransaction.setSender(sender);
        newTransaction.setRecipient(recipient);
        newTransaction.setAmount(amount);
        newTransaction.setDate(new Date());
        newTransaction.setDescription(description);
        return newTransaction;
    }
}
