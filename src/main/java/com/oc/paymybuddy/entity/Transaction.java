package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long id_sender;
    private long id_recipient;
    private BigDecimal amount;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String description;
}
