package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Recipient_List")
public class RecipientList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_sender")
    private UserAccount sender;

    @ManyToOne
    @JoinColumn(name = "id_recipient")
    private UserAccount recipient;
    //private long id_sender;
    //private long id_recipient;
}
