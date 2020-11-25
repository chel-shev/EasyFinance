package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity(name = "loan")
public class LoanEntity {

    @Id
    @GeneratedValue
    private Long loanId;
    private Long amount;
    private LocalDateTime dateStart;
    private LocalDateTime dateFinish;
    private Boolean direction;

    @ManyToOne
    private AccountEntity account;
}
