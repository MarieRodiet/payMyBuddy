package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Recipient_List")
public class RecipientList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_sender")
    private UserAccount sender;

    @ManyToOne
    @JoinColumn(name = "id_recipient")
    private UserAccount recipient;
}
