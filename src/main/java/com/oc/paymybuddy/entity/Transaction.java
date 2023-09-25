package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @OrderColumn
    @JoinColumn(name = "id_recipient")
    private UserAccount recipient;

    @NotNull(message = "Amount should not be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount should be greater than or equal to 0.01")
    private BigDecimal amount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String description;


}
