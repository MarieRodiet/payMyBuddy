package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_accounts")
public class UserAccount implements UserDetails {

    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    private String lastname;
    private String firstname;
    @Column(name = "account_nb")
    private String accountNumber;
    private BigDecimal balance;
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming your Role enum has a method to get the role name (e.g., getRoleName())
        return Collections.singleton(new SimpleGrantedAuthority(getRole()));
    }

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
