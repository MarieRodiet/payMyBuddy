package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.RecipientList;
import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.service.RecipientListService;
import com.oc.paymybuddy.service.TransactionService;
import com.oc.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.apache.logging.log4j.LogManager.getLogger;

import java.util.Date;
import java.util.List;

@Controller
public class TransfersController {
    private static final Logger logger = getLogger(TransfersController.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RecipientListService recipientListService;


    @GetMapping(path="/transfers")
    public String getTransfers(Model model,
                               @RequestParam(name="page", defaultValue = "1") Integer page,
                               @RequestParam(name="size", defaultValue = "10") Integer size){
        UserAccount currentUser = userAccountService.findCurrentUser();
        logger.info("getting all transactions and connections associated to user");
        Page<Transaction> transactions  = transactionService.getTransactionsBySender(currentUser, PageRequest.of(page -1, size));
        List<UserAccount> connections = recipientListService.getRecipientListBySender(currentUser, userAccountService.findAllUserAccounts());

        int[] pages = new int[transactions.getTotalPages()];
        Integer totalPages = transactions.getTotalPages();
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("pages", pages);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "transfers";
    }

    @PostMapping(path="/transfers")
    public String postTransaction(Model model,
                               @Valid Transaction transaction, BindingResult result, RedirectAttributes redirectAttributes,
                               @RequestParam(name="page", defaultValue = "1") Integer page,
                               @RequestParam(name="size", defaultValue = "10") Integer size){
        UserAccount currentUser = userAccountService.findCurrentUser();
        if(result.hasErrors()){
            logger.error("could not add Transaction for user");
        }
        else{
            Transaction newTransaction = new Transaction();
            newTransaction.setSender(currentUser);
            newTransaction.setRecipient(transaction.getRecipient());
            newTransaction.setAmount(transaction.getAmount());
            newTransaction.setDate(new Date());
            newTransaction.setDescription(transaction.getDescription());
            transactionService.saveTransaction(newTransaction);

            userAccountService.decreaseUserAccountBalance(currentUser, transaction.getAmount());
            logger.info("saving transaction and updating user's balance");
        }

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
            if(recipientListService.checkIfRecipientListExists(newRecipientList)){
                redirectAttributes.addFlashAttribute("info", "This email is already in a recipient");
            }
            else{
                logger.info("added new connection for current user");
                recipientListService.saveRecipientList(newRecipientList);
                redirectAttributes.addFlashAttribute("success", "This recipient was added successfully");
            }
        }
        else{
            logger.error("could not add new connection");
            redirectAttributes.addFlashAttribute("error", "No user known with this email address");
        }
        return "redirect:/transfers";
    }

}
