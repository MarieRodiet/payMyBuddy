package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.RecipientList;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.RecipientListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecipientListService {

    private RecipientListRepository recipientListRepository;

    public List<RecipientList> getRecipientListBySender(UserAccount sender){
        return recipientListRepository.findRecipientListBySender(sender);
    }
}
