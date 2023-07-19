package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_accounts")
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
