package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.entity.RecipientList;
import com.oc.paymybuddy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipientListRepository extends JpaRepository<RecipientList, Integer> {
    List<RecipientList> findRecipientListBySender(UserAccount sender);
}
