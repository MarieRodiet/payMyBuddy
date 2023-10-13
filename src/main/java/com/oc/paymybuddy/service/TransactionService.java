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

    /**
     * Retrieves a page of transactions initiated by a user, sorted by date in descending order.
     *
     * @param userAccount    The user account for which to retrieve transactions.
     * @param pageRequest    The page request specifying page number and page size.
     * @return A page of transactions sorted by date, or an empty page if no transactions are found.
     */
    public Page<Transaction> getTransactionsBySender(UserAccount userAccount, PageRequest pageRequest){
        Sort sortByDate = Sort.by(Sort.Order.desc("date"));
        PageRequest sortedPageRequest = PageRequest.of(
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                sortByDate
        );

        Page<Transaction> transactions = transactionRepository.findTransactionsBySender(userAccount, sortedPageRequest);
        if(transactions == null){
            return Page.empty();
        }
        return transactions;
    }

    /**
     * Saves a new transaction in the repository.
     *
     * @param newTransaction The transaction to be saved.
     * @return The saved transaction.
     */
    public Transaction saveTransaction(Transaction newTransaction) {
        return transactionRepository.save(newTransaction);
    }

    /**
     * Creates a new transaction with the provided sender, recipient, amount, and description.
     *
     * @param sender      The user account initiating the transaction.
     * @param recipient   The user account receiving the transaction.
     * @param amount      The transaction amount.
     * @param description A description of the transaction.
     * @return The newly created transaction.
     */
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
