package com.oc.paymybuddy.web;

import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.TransactionRepository;
import com.oc.paymybuddy.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class TransfersController {

    private UserAccountRepository userAccountRepository;
    private TransactionRepository transactionRepository;

    @GetMapping(path="/index")
    public String getUserAccounts(Model model){
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        model.addAttribute("userAccounts", userAccounts);
        return "useraccounts";
    }

    @GetMapping(path="/transfers")
    public String getTransfers(Model model,
                               @RequestParam(name="page", defaultValue = "0") Integer page,
                               @RequestParam(name="size", defaultValue = "3") Integer size){
        Page<Transaction> transactions = transactionRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("pages", new int[transactions.getTotalPages()]);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("currentPage", page);
        return "transfers";
    }

    @GetMapping(path="/home")
    public String getHome(Model model){
        return "home";
    }


}
