package com.poseidon.inventory.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "item_returned")
public class ItemReturned {

    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @PrimaryKeyJoinColumn
    @ManyToOne
    @JoinColumn(name = "barcode")
    private Item item;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_number", foreignKey = @ForeignKey(name = "fk_order_number"))
    private Order order;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = new Date();
        }
    }

}