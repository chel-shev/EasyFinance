package ru.ixec.easyfinance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ixec.easyfinance.type.ActionType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity(name = "account_history")
@AllArgsConstructor
@NoArgsConstructor
public class AccountHistoryEntity {

    @Id
    @GeneratedValue
    private Long clientHistoryId;
    private Long amount;
    private Long accountBalance;
    private ActionType type;
    private LocalDateTime date;

    @ManyToOne
    private AccountEntity account;
}
