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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

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
        UserAccount currentUser = userAccountService.findCurrentUser();
        Page<Transaction> transactions  = transactionService.getTransactionsBySender(currentUser, PageRequest.of(page, size));
        List<UserAccount> connections = recipientListService.getRecipientListBySender(currentUser, userAccountService.findAllUserAccounts());

        model.addAttribute("transaction", new Transaction());
        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("pages", new int[transactions.getTotalPages()]);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("currentPage", page);
        return "transfers";
    }

    @PostMapping(path="/transfers")
    public String postTransaction(Model model,
                               @Valid Transaction transaction,
                               @RequestParam(name="page", defaultValue = "0") Integer page,
                               @RequestParam(name="size", defaultValue = "10") Integer size){
        UserAccount currentUser = userAccountService.findCurrentUser();

        Transaction newTransaction = new Transaction();
        newTransaction.setSender(currentUser);
        newTransaction.setRecipient(transaction.getRecipient());
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setDate(new Date());
        newTransaction.setDescription(transaction.getDescription());
        transactionService.saveTransaction(newTransaction);

        userAccountService.decreaseUserAccountBalance(currentUser, transaction.getAmount());

        Page<Transaction> transactions  = transactionService.getTransactionsBySender(currentUser, PageRequest.of(page, size));
        List<UserAccount> connections = recipientListService.getRecipientListBySender(currentUser, userAccountService.findAllUserAccounts());

        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("pages", new int[transactions.getTotalPages()]);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("currentPage", page);
        return "transfers";
    }

    @PostMapping(path="/addconnection")
    public String addNewConnection(
            String email,
            RedirectAttributes redirectAttributes
    ){
        UserAccount currentUser = userAccountService.findCurrentUser();
        UserAccount recipientUserAccount = userAccountService.findUserAccountByEmail(email);
        if(recipientUserAccount != null){
            RecipientList newRecipientList = new RecipientList();
            newRecipientList.setSender(currentUser);
            newRecipientList.setRecipient(userAccountService.findUserAccountByEmail(email));
            recipientListService.saveRecipientList(newRecipientList);
            redirectAttributes.addFlashAttribute("success", "This recipient was added successfully");
        }
        else{
            redirectAttributes.addFlashAttribute("error", "No user known with this email address");
        }
        return "redirect:/transfers";
    }

}
