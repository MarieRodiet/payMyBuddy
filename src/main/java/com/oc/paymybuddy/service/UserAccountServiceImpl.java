package com.oc.paymybuddy.service;

import com.oc.paymybuddy.dto.UserAccountDto;
import com.oc.paymybuddy.entity.Role;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService{

    private UserAccountRepository userAccountRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public void saveUserAccount(UserAccountDto userAccountDto) {
        UserAccount user = new UserAccount();
        user.setFirstname(userAccountDto.getFirstname());
        user.setLastname(userAccountDto.getLastname());
        user.setEmail(userAccountDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userAccountDto.getPassword()));
        user.setRole(Role.USER.name());
        userAccountRepository.save(user);

    }

    @Override
    public UserAccount findUserAccountByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    @Override
    public List<UserAccountDto> findAllUserAccounts() {
        List<UserAccount> users = userAccountRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserAccountDto mapToUserDto(UserAccount user) {
        UserAccountDto userDto = new UserAccountDto();
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
