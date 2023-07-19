package com.oc.paymybuddy.service;

import com.oc.paymybuddy.dto.UserAccountDto;
import com.oc.paymybuddy.entity.Role;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserAccountService{

    private UserAccountRepository userAccountRepository;
    private PasswordEncoder passwordEncoder;

    public void saveUserAccount(UserAccountDto userAccountDto) {
        UserAccount userAccount = new UserAccount();
        userAccount.setFirstname(userAccountDto.getFirstname());
        userAccount.setLastname(userAccountDto.getLastname());
        userAccount.setEmail(userAccountDto.getEmail());
        // encrypt the password using spring security
        userAccount.setPassword(passwordEncoder.encode(userAccountDto.getPassword()));
        userAccount.setRole(Role.USER.name());
        userAccount.setAccountNumber(userAccountDto.getAccountNumber());
        userAccount.setBalance(userAccountDto.getBalance());
        userAccountRepository.save(userAccount);

    }


    public UserAccount findUserAccountByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }


    public List<UserAccountDto> findAllUserAccounts() {
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        return userAccounts.stream()
                .map((userAccount) -> mapToUserAccountDto(userAccount))
                .collect(Collectors.toList());
    }

    private UserAccountDto mapToUserAccountDto(UserAccount userAccount) {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setFirstname(userAccount.getFirstname());
        userAccountDto.setLastname(userAccount.getLastname());
        userAccountDto.setEmail(userAccount.getEmail());
        return userAccountDto;
    }
}
