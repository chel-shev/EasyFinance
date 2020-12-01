package ru.ixec.easyfinance.entity;

import lombok.Getter;
import lombok.Setter;
import ru.ixec.easyfinance.bot.inquiry.Inquiry;
import ru.ixec.easyfinance.type.ActionType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "inquiry")
public class InquiryEntity {

    @Id
    @GeneratedValue
    private Long inquiryId;
    private String text;
    private ActionType type;
    private LocalDateTime date;
    private Long amount;
    private Boolean completed = false;

    @ManyToOne
    private AccountEntity in;

    @ManyToOne
    private AccountEntity out;

    @ManyToOne
    private ClientEntity client;

    public InquiryEntity() {

    }

    public InquiryEntity(Inquiry inquiry, AccountEntity in, AccountEntity out) {
        this.inquiryId = inquiry.getId();
        this.text = inquiry.getText();
        this.type = inquiry.getType();
        this.date = inquiry.getDate();
        this.amount = inquiry.getAmount();
        this.completed = inquiry.isCompleted();
        this.client = inquiry.getClient();
        this.in = in;
        this.out = out;
    }
}
