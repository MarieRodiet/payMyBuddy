package com.oc.paymybuddy.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private BigDecimal amount;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String description;
}
