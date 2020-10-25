package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class Transfer {

    @Id
    @GeneratedValue
    private Long transferId;
    private Long amount;

    @OneToOne
    private Account in;

    @OneToOne
    private Account out;
}
