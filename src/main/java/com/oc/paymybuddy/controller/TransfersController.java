package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.dto.UserAccountDto;
import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.TransactionRepository;
import com.oc.paymybuddy.service.TransactionService;
import com.oc.paymybuddy.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TransfersController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private TransactionService transactionService;

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserAccountDto> users = userAccountService.findAllUserAccounts();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(path="/useraccounts")
    public String getUserAccounts(Model model){
        List<UserAccountDto> userAccounts = userAccountService.findAllUserAccounts();
        model.addAttribute("userAccounts", userAccounts);
        return "useraccounts";
    }

    @GetMapping(path="/transfers")
    public String getTransfers(Model model,
                               @RequestParam(name="page", defaultValue = "0") Integer page,
                               @RequestParam(name="size", defaultValue = "3") Integer size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount currentUser = userAccountService.findUserAccountByEmail(authentication.getName());

        Page<Transaction> transactions = transactionService.getUserAccountTransactionsByEmail(currentUser, PageRequest.of(page, size));
        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("pages", new int[transactions.getTotalPages()]);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("currentPage", page);
        return "transfers";
    }

}
