package ru.ixec.easyfinance.entity;

import lombok.Data;
import ru.ixec.easyfinance.type.ActionType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity(name = "clientHistory")
public class ClientHistoryEntity {

    @Id
    @GeneratedValue
    private Long clientHistoryId;
    private Long amount;
    private ActionType type;
    private LocalDateTime date;

    @ManyToOne
    private AccountEntity account;
}
