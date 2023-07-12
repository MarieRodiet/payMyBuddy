package com.oc.paymybuddy.repository;
import com.oc.paymybuddy.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
