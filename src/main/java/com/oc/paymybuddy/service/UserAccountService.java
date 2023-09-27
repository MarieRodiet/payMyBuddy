package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAccountService{

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccount findUserAccountByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

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


    public List<UserAccount> findAllUserAccounts() {
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        if(userAccounts != null){
            return userAccounts.stream()
                    .collect(Collectors.toList());
        }
        return null;
    }

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

    public UserAccount updateUserAccountBalance(UserAccount userAccount, Integer money){
        BigDecimal newBalance = userAccount.getBalance().add(BigDecimal.valueOf(money));
        userAccount.setBalance(newBalance);
        return userAccountRepository.save(userAccount);
    }

    public UserAccount findCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return findUserAccountByEmail(email);
    }

}
