package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.RecipientList;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.RecipientListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipientListService {

    private RecipientListRepository recipientListRepository;

    public List<UserAccount> getRecipientListBySender(UserAccount sender, List<UserAccount> allUsers){
        List<RecipientList> connectionsIds = recipientListRepository.findRecipientListBySender(sender);
        List<UserAccount> connectionObjects = allUsers
                .stream()
                .filter(userAccount ->
                        connectionsIds
                                .stream()
                                .anyMatch(a -> Objects.equals(userAccount.getId(), a.getRecipient().getId())))
                .collect(Collectors.toList());
        return  connectionObjects;
    }

    public RecipientList saveRecipientList(RecipientList recipientList){
        if(!checkIfRecipientListExists(recipientList)){
            return recipientListRepository.save(recipientList);
        }
        return null;
    }

    public boolean checkIfRecipientListExists(RecipientList recipientList){
        List<RecipientList> all = recipientListRepository.findAll();
        return all.stream().anyMatch(r -> Objects.equals(r, recipientList));
    }

}
