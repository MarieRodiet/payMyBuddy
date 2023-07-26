package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserAccountService{

    private UserAccountRepository userAccountRepository;

    public UserAccount findUserAccountByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }


    public List<UserAccount> findAllUserAccounts() {
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        return userAccounts.stream()
                .collect(Collectors.toList());
    }

    public void updateUserAccountBalance(UserAccount userAccount, BigDecimal balance){
        BigDecimal increaseAmount= balance
                .multiply(new BigDecimal("0.5"))
                .divide(new BigDecimal(100))
                .add(balance);
        BigDecimal newBalance = userAccount.getBalance().subtract(increaseAmount);
        userAccountRepository.updateUserAccountBalance(userAccount.getId(), newBalance);
    }

}
