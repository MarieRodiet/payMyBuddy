package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity(name="User_Account")
public class UserAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String lastname;
    private String firstname;
    @Column(name = "account_nb")
    private String accountNumber;
    private BigDecimal balance;
}
