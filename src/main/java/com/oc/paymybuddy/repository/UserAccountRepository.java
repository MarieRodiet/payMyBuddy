package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository <UserAccount, Integer>{
}
