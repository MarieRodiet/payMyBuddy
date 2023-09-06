package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.SenderRecipientConnection;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.SenderRecipientConnectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SenderRecipientConnectionService {

    private SenderRecipientConnectionRepository senderRecipientConnectionRepository;

    public List<UserAccount> getSenderRecipientConnectionBySender(UserAccount sender, List<UserAccount> allUsers){
        List<SenderRecipientConnection> connectionsIds = senderRecipientConnectionRepository.findSenderRecipientConnectionRepositoryBySender(sender);
        List<UserAccount> connectionObjects = allUsers
                .stream()
                .filter(userAccount ->
                        connectionsIds
                                .stream()
                                .anyMatch(a -> Objects.equals(userAccount.getId(), a.getRecipient().getId())))
                .collect(Collectors.toList());
        return  connectionObjects;
    }

    public SenderRecipientConnection saveSenderRecipientConnectionRepository(SenderRecipientConnection senderRecipientConnection){
        if(!checkIfsenderRecipientConnectionRepositoryExists(senderRecipientConnection)){
            return senderRecipientConnectionRepository.save(senderRecipientConnection);
        }
        return null;
    }

    public boolean checkIfsenderRecipientConnectionRepositoryExists(SenderRecipientConnection senderRecipientConnection){
        List<SenderRecipientConnection> all = senderRecipientConnectionRepository.findAll();
        return all.stream().anyMatch(r -> Objects.equals(r, senderRecipientConnection));
    }

    public SenderRecipientConnection createSenderRecipientConnection(UserAccount sender, UserAccount recipient){
        SenderRecipientConnection newSenderRecipientConnection = new SenderRecipientConnection();
        newSenderRecipientConnection.setSender(sender);
        newSenderRecipientConnection.setRecipient(recipient);
        return newSenderRecipientConnection;
    }

}
