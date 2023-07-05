package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.entity.User_Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User_Account, Integer>{
}
