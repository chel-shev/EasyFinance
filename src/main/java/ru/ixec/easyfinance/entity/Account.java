package ru.ixec.easyfinance.entity;

import lombok.Data;
import ru.ixec.easyfinance.type.AccountType;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long accountId;
    private String name;
    private Long amount;
    private Boolean main;
    private AccountType accountType;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "account")
    private List<Expense> expenseList;

    @OneToMany(mappedBy = "account")
    private List<Income> incomeList;

    @OneToMany(mappedBy = "account")
    private List<Loan> loanList;
}
