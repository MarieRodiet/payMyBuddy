package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The UserAccountService is responsible for managing user accounts .
 * This service class provides methods for getting all users, a user and the current user as well as saving a
 * user account and updating its balance.
 */
@Service
@AllArgsConstructor
@Transactional
public class UserAccountService{

    private UserAccountRepository userAccountRepository;

    /**
     * Finds a user account by their email address.
     *
     * @param email The email address of the user.
     * @return The UserAccount object associated with the email or null if not found.
     */
    public UserAccount findUserAccountByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    /**
     * Saves a user account, updating its details if the email already exists.
     *
     * @param userAccount The user account to be saved or updated.
     * @return The saved or updated user account.
     */
    public UserAccount saveUserAccount(UserAccount userAccount){
        UserAccount user = userAccountRepository.findByEmail(userAccount.getEmail());
        if(user != null){
            user.setFirstname(userAccount.getFirstname());
            user.setLastname(userAccount.getLastname());
            user.setAccountNumber(userAccount.getAccountNumber());
            user.setBalance(userAccount.getBalance());
            userAccountRepository.save(user);
            return user;
        }
        return null;
    }

    /**
     * Retrieves a list of all user accounts.
     *
     * @return A list of all user accounts or null if no user accounts are found.
     */
    public List<UserAccount> findAllUserAccounts() {
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        if(userAccounts != null){
            return userAccounts.stream()
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Decreases a user account's balance by a specified amount.
     *
     * @param userAccount         The user account for which to decrease the balance.
     * @param transactionAmount   The amount to decrease the balance by.
     * @return The updated user account with the decreased balance.
     */
    public UserAccount decreaseUserAccountBalance(UserAccount userAccount, BigDecimal transactionAmount){
        BigDecimal increaseAmount = transactionAmount
                .multiply(new BigDecimal("0.5"))
                .divide(new BigDecimal(100))
                .add(transactionAmount);
        BigDecimal newBalance = userAccount.getBalance().subtract(increaseAmount);
        userAccount.setBalance(newBalance);
        userAccountRepository.save(userAccount);
        return userAccount;
    }

    /**
     * Updates a user account's balance by adding the specified amount of money.
     *
     * @param userAccount The user account for which to update the balance.
     * @param money       The amount of money to add to the balance.
     * @return The updated user account with the new balance.
     */
    public UserAccount updateUserAccountBalance(UserAccount userAccount, Integer money){
        BigDecimal newBalance = userAccount.getBalance().add(BigDecimal.valueOf(money));
        userAccount.setBalance(newBalance);
        return userAccountRepository.save(userAccount);
    }

    /**
     * Retrieves the user account associated with the currently authenticated user.
     *
     * @return The user account of the currently authenticated user or null if not authenticated.
     */
    public UserAccount findCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return findUserAccountByEmail(email);
    }

}
