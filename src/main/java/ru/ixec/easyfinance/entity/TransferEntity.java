package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity(name = "transfer")
public class TransferEntity {

    @Id
    @GeneratedValue
    private Long transferId;
    private Long amount;

    @OneToOne
    private AccountEntity in;

    @OneToOne
    private AccountEntity out;
}
