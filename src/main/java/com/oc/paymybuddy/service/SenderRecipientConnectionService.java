package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.SenderRecipientConnection;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.SenderRecipientConnectionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The SenderRecipientConnectionService is responsible for managing connections between different userAccounts.
 * This service class provides methods for getting, saving, creating and checking the existence of
 * SenderRecipientConnection Objects.
 */
@Service
@AllArgsConstructor
@Transactional
public class SenderRecipientConnectionService {

    private SenderRecipientConnectionRepository senderRecipientConnectionRepository;

    /**
     * Finds a list of connections for a user account
     *
     * @param sender the user account to which connections are associated
     * @param allUsers the list of all the existing user accounts
     * @return a list of all the connections associated to the sender
     */
    public List<UserAccount> getSenderRecipientConnectionBySender(UserAccount sender, List<UserAccount> allUsers){
        List<SenderRecipientConnection> connectionsIds = senderRecipientConnectionRepository.findSenderRecipientConnectionRepositoryBySender(sender);
        List<UserAccount> connectionObjects = allUsers
                .stream()
                .filter(userAccount ->
                        connectionsIds
                                .stream()
                                .anyMatch(a -> Objects.equals(userAccount.getId(), a.getRecipient().getId())))
                .collect(Collectors.toList());
        return connectionObjects;
    }

    /**
     * Saves a connection between a sender and a recipient
     *
     * @param senderRecipientConnection The sender and recipient connection to be saved
     * @return The saved or updated connection or null if the connection already exists.
     */
    public SenderRecipientConnection saveSenderRecipientConnection(SenderRecipientConnection senderRecipientConnection){
        if(!checkIfsenderRecipientConnectionRepositoryExists(senderRecipientConnection)){
            return senderRecipientConnectionRepository.save(senderRecipientConnection);
        }
        return null;
    }

    /**
     * Checks if a connection between a sender and a recipient already exists
     *
     * @param senderRecipientConnection The sender and recipient connection
     * @return true if the connection already exists or false if it does not.
     */
    public boolean checkIfsenderRecipientConnectionRepositoryExists(SenderRecipientConnection senderRecipientConnection){
        List<SenderRecipientConnection> all = senderRecipientConnectionRepository.findAll();
        return all.stream().anyMatch(r -> Objects.equals(r, senderRecipientConnection));
    }

    /**
     * Creates a new connection with the provided sender and recipient
     *
     * @param sender      The user account adding this user account as his recipient
     * @param recipient   The user account becoming the sender's recipient and possibly receiving transactions in the future.
     * @return The newly created connection.
     */
    public SenderRecipientConnection createSenderRecipientConnection(UserAccount sender, UserAccount recipient){
        SenderRecipientConnection newSenderRecipientConnection = new SenderRecipientConnection();
        newSenderRecipientConnection.setSender(sender);
        newSenderRecipientConnection.setRecipient(recipient);
        return newSenderRecipientConnection;
    }

}
