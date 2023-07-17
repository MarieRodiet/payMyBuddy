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
@Table(name="Transaction")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_sender")
    private UserAccount sender;

    @ManyToOne
    @JoinColumn(name = "id_recipient")
    private UserAccount recipient;

    private BigDecimal amount;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String description;
}
