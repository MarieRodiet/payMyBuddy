package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.RecipientList;
import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.service.RecipientListService;
import com.oc.paymybuddy.service.TransactionService;
import com.oc.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class TransfersController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RecipientListService recipientListService;



    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserAccount> users = userAccountService.findAllUserAccounts();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(path="/useraccounts")
    public String getUserAccounts(Model model){
        List<UserAccount> userAccounts = userAccountService.findAllUserAccounts();
        model.addAttribute("userAccounts", userAccounts);
        return "useraccounts";
    }

    @GetMapping(path="/transfers")
    public String getTransfers(Model model,
                               @RequestParam(name="page", defaultValue = "0") Integer page,
                               @RequestParam(name="size", defaultValue = "10") Integer size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount currentUser = userAccountService.findUserAccountByEmail(authentication.getName());
        Page<Transaction> transactions  = transactionService.getTransactionsBySender(currentUser, PageRequest.of(page, size));
        List<RecipientList> connectionsIds = recipientListService.getRecipientListBySender(currentUser);
        List<UserAccount> connectionObjects = userAccountService.findAllUserAccounts()
                .stream()
                .filter(userAccount ->
                    connectionsIds
                            .stream()
                            .anyMatch(a -> Objects.equals(userAccount.getId(), a.getRecipient().getId())))
                .collect(Collectors.toList());
        model.addAttribute("connections", connectionObjects);
        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("pages", new int[transactions.getTotalPages()]);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("currentPage", page);
        return "transfers";
    }

    @PostMapping(path="/addconnection")
    public String addNewConnection(
            Model model,
            @Valid @ModelAttribute("newConnection") String newConnection
    ){
        System.out.println(newConnection);
        return "transfers";
    }

}
