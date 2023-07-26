package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    public Page<Transaction> getTransactionsBySender(UserAccount userAccount, PageRequest pageRequest){
        Page<Transaction> transactions = transactionRepository.findTransactionsBySender(userAccount, pageRequest);
        return transactions;
    }

    public void saveTransaction(Transaction newTransaction) {
        transactionRepository.save(newTransaction);
    }
}
