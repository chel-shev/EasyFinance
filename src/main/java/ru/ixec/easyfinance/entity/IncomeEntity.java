package ru.ixec.easyfinance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity(name = "income")
@NoArgsConstructor
public class IncomeEntity {

    @Id
    @GeneratedValue
    private Long incomeId;
    private String name;
    private Long amount;
    private LocalDateTime date;

    @ManyToOne
    private IncomeCategoryEntity incomeCategory;

    @ManyToOne
    private AccountEntity account;

    public IncomeEntity(String name, Long amount, LocalDateTime date, IncomeCategoryEntity incomeCategory, AccountEntity account) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.incomeCategory = incomeCategory;
        this.account = account;
    }


}