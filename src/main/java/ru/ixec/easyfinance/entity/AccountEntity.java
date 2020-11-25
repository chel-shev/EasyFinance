package ru.ixec.easyfinance.entity;

import lombok.Data;
import ru.ixec.easyfinance.type.AccountType;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue
    private Long accountId;
    private String name;
    private Long amount;
    private Boolean main;
    private AccountType accountType;

    @ManyToOne
    private ClientEntity client;

    @OneToMany(mappedBy = "account")
    private List<ExpenseEntity> expenseList;

    @OneToMany(mappedBy = "account")
    private List<IncomeEntity> incomeList;

    @OneToMany(mappedBy = "account")
    private List<LoanEntity> loanList;
}
