package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity(name = "transfer")
public class TransferEntity {

    @Id
    @GeneratedValue
    private Long transferId;
    private Long amount;
    private LocalDateTime date;

    @ManyToOne
    private AccountEntity in;

    @ManyToOne
    private AccountEntity out;

    public TransferEntity() {

    }

    public TransferEntity(AccountEntity in, AccountEntity out, Long amount, LocalDateTime date) {
        this.in = in;
        this.out = out;
        this.amount = amount;
        this.date = date;
    }
}
