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
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service @AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserAccountRepository userRepository;

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
