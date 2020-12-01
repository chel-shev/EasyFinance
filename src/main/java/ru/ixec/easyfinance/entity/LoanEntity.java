package ru.ixec.easyfinance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity(name = "loan")
@NoArgsConstructor
public class LoanEntity {

    @Id
    @GeneratedValue
    private Long loanId;
    private String name;
    private Long amount;
    private LocalDateTime dateStart;
    private LocalDateTime dateFinish;
    private Boolean direction;

    @ManyToOne
    private AccountEntity account;

    public LoanEntity(String name, long amount, LocalDateTime dateStart, LocalDateTime dateFinish, boolean direction, AccountEntity account) {
        this.name = name;
        this.amount = amount;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.direction = direction;
        this.account = account;
    }

    public LoanEntity(String name, long amount) {
        this.name = name;
        this.amount = amount;
    }
}
