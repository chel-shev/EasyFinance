package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity(name = "income")
public class IncomeEntity {

    @Id
    @GeneratedValue
    private Long incomeId;
    private Long amount;

    @ManyToOne
    private IncomeCategoryEntity incomeCategory;

    @ManyToOne
    private AccountEntity account;
}