package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.entity.UserAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface UserAccountRepository extends JpaRepository <UserAccount, Integer>{

    UserAccount findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserAccount u SET u.balance = :balance WHERE u.id = :userAccountId")
    void updateUserAccountBalance(Integer userAccountId, BigDecimal balance);
}
