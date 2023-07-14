package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="User_Account")
public class UserAccount{

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
}
