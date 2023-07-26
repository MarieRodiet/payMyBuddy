package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;
    @NotEmpty(message = "Lastname should not be empty")
    private String lastname;

    @NotEmpty(message = "Firstname should not be empty")
    private String firstname;
    @NotEmpty(message = "Account Number should not be empty")
    @Column(name = "account_nb")
    private String accountNumber;

    @NotEmpty(message = "Balance should not be empty")
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
