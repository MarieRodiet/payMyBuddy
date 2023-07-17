package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @OrderColumn
    @JoinColumn(name = "id_sender")
    private UserAccount sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @OrderColumn
    @JoinColumn(name = "id_recipient")
    private UserAccount recipient;

    private BigDecimal amount;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String description;
}
