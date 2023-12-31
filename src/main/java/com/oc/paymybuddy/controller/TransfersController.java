package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.SenderRecipientConnection;
import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.service.SenderRecipientConnectionService;
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

import java.util.List;
import java.util.Objects;

/**
 * The TransfersController class is responsible for handling HTTP requests related to user transfers and connections.
 * It provides methods for displaying a list of past transfers, creating new ones, and managing user connections.
 */
@Controller
public class TransfersController {
    private static final Logger logger = getLogger(TransfersController.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SenderRecipientConnectionService senderRecipientConnectionService;


    /**
     * Handles GET requests to the "/transfers" endpoint, displaying a list of user transactions and connections.
     *
     * @param model        The Spring MVC model used for storing data to be rendered in the view.
     * @param page         The page number for pagination (default is 1).
     * @param size         The number of items per page (default is 10).
     * @return The view name "transfers" or redirects to the login page if the user is not authenticated.
     */
    @GetMapping(path="/transfers")
    public String getTransfers(Model model,
                               @RequestParam(name="page", defaultValue = "1") Integer page,
                               @RequestParam(name="size", defaultValue = "10") Integer size){
        UserAccount currentUser = userAccountService.findCurrentUser();
        if (currentUser == null) {
            // Redirect to the login page
            return "redirect:/login";
        }
        logger.info("getting all transactions and connections associated to user");
        Page<Transaction> transactions  = transactionService.getTransactionsBySender(currentUser, PageRequest.of(page -1, size));
        List<UserAccount> connections = senderRecipientConnectionService.getSenderRecipientConnectionBySender(currentUser, userAccountService.findAllUserAccounts());

        model.addAttribute("transaction", new Transaction());
        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("pages", new int[transactions.getTotalPages()]);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("currentPage", page);
        return "transfers";
    }

    /**
     * Handles POST requests to the "/transfers" endpoint, allowing users to create new transactions.
     *
     * @param model               The Spring MVC model used for storing data to be rendered in the view.
     * @param transaction         The transaction data to be created and saved.
     * @param result              The binding result for validation errors.
     * @param redirectAttributes  Used for adding flash attributes to the redirect.
     * @param page                The page number for pagination (default is 1).
     * @param size                The number of items per page (default is 10).
     * @return The view name "transfers" with updated data or an error message if validation fails.
     */
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
            if(currentUser.getBalance().compareTo(transaction.getAmount()) > 0){
                Transaction newTransaction = transactionService.createTransaction(currentUser, transaction.getRecipient(), transaction.getAmount(), transaction.getDescription());
                transactionService.saveTransaction(newTransaction);

                userAccountService.decreaseUserAccountBalance(currentUser, transaction.getAmount());
                logger.info("saving transaction and updating user's balance");
                redirectAttributes.addFlashAttribute("success", "Transaction successfully made");
            }
            else {
                logger.error("Insufficient balance to perform the transaction");
                redirectAttributes.addFlashAttribute("error", "Insufficient balance to perform the transaction");
            }

        }

        Page<Transaction> transactions  = transactionService.getTransactionsBySender(currentUser, PageRequest.of(page -1, size));
        List<UserAccount> connections = senderRecipientConnectionService.getSenderRecipientConnectionBySender(currentUser, userAccountService.findAllUserAccounts());

        model.addAttribute("transaction", new Transaction());
        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("pages", new int[transactions.getTotalPages()]);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("currentPage", page);
        return "redirect:/transfers";
    }

    /**
     * Handles POST requests to the "/addconnection" endpoint, allowing users to add new connections.
     *
     * @param email               The email of the user to be connected.
     * @param redirectAttributes  Used for adding flash attributes to the redirect.
     * @return Redirects to the "/transfers" page with success or error messages.
     */
    @PostMapping(path="/addconnection")
    public String addNewConnection(
            String email,
            RedirectAttributes redirectAttributes
    ){
        UserAccount currentUser = userAccountService.findCurrentUser();
        UserAccount recipientUserAccount = userAccountService.findUserAccountByEmail(email);
        if(recipientUserAccount != null && !Objects.equals(recipientUserAccount, currentUser)){
            SenderRecipientConnection newSenderRecipientConnection = senderRecipientConnectionService.createSenderRecipientConnection(currentUser, userAccountService.findUserAccountByEmail(email));

            if(senderRecipientConnectionService.checkIfsenderRecipientConnectionRepositoryExists(newSenderRecipientConnection)){
                redirectAttributes.addFlashAttribute("info", "Connection already exists");
            }
            else{
                logger.info("added new connection for current user");
                senderRecipientConnectionService.saveSenderRecipientConnection(newSenderRecipientConnection);
                redirectAttributes.addFlashAttribute("success", "Connection successfully created");
            }
        }
        else{
            logger.error("could not add new connection");
            redirectAttributes.addFlashAttribute("error", "Could not add new connection");
        }
        return "redirect:/transfers";
    }

}
