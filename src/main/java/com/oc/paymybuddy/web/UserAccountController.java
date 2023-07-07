package com.oc.paymybuddy.web;

import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.TransactionRepository;
import com.oc.paymybuddy.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserAccountController {

    private UserAccountRepository userAccountRepository;
    private TransactionRepository transactionRepository;

    @GetMapping(path="/index")
    public String getUserAccounts(Model model){
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        model.addAttribute("userAccounts", userAccounts);
        return "useraccounts";
    }

    @GetMapping(path="/transfer")
    public String getTransfers(Model model){
        List<Transaction> transactions = transactionRepository.findAll();
        model.addAttribute("transactions", transactions);
        return "transfers";
    }

    @GetMapping(path="/home")
    public String getHome(Model model){
        return "home";
    }


}
