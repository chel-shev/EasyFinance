package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Income {

    @Id
    @GeneratedValue
    private Long incomeId;
    private Long amount;

    @ManyToOne
    private IncomeCategory incomeCategory;

    @ManyToOne
    private Account account;
}