package com.oc.paymybuddy.security;

import com.oc.paymybuddy.entity.Role;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;

@Service @AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserAccountRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void authenticateUser(UserAccount userAccountDto) {
        UserDetails userDetails = loadUserByUsername(userAccountDto.getEmail());
    }

    // Register new user account
    public void saveUserAccount(UserAccount userAccountDto) {
        UserAccount userAccount = new UserAccount();
        userAccount.setFirstname(userAccountDto.getFirstname());
        userAccount.setLastname(userAccountDto.getLastname());
        userAccount.setEmail(userAccountDto.getEmail());
        // encrypt the password using spring security
        userAccount.setPassword(passwordEncoder.encode(userAccountDto.getPassword()));
        userAccount.setRole(Role.USER.name());
        userAccount.setAccountNumber(userAccountDto.getAccountNumber());
        userAccount.setBalance(userAccountDto.getBalance());
        userRepository.save(userAccount);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userRepository.findByEmail(username);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    Collections.singleton(mapRoleToAuthorities(Role.valueOf(user.getRole()))));
        } else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private GrantedAuthority mapRoleToAuthorities(Role role) {
        return new SimpleGrantedAuthority(role.toString());
    }
}
