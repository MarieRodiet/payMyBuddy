package com.oc.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipient_lists")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipientList that = (RecipientList) o;
        return Objects.equals(sender, that.sender) && Objects.equals(recipient, that.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, recipient);
    }
}
