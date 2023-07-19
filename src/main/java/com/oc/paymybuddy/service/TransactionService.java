package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    public Page<Transaction> getUserAccountTransactionsByEmail(UserAccount userAccount, PageRequest pageRequest){
        List<Transaction> transactions =  transactionRepository.findAll().stream().filter(
                        transaction -> Objects.equals(transaction.getSender(), userAccount.getId()))
                .collect(Collectors.toList());
        return PageableExecutionUtils.getPage(transactions, pageRequest, transactions::size);
    }
}
