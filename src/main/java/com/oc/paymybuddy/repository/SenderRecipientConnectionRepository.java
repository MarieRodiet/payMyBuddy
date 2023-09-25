package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.entity.SenderRecipientConnection;
import com.oc.paymybuddy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SenderRecipientConnectionRepository extends JpaRepository<SenderRecipientConnection, Integer> {
    List<SenderRecipientConnection> findSenderRecipientConnectionRepositoryBySender(UserAccount sender);
}
