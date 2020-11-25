package ru.ixec.easyfinance.entity;

import lombok.Getter;
import lombok.Setter;
import ru.ixec.easyfinance.bot.inquiry.Inquiry;
import ru.ixec.easyfinance.type.InquiryType;

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
    private InquiryType type;
    private LocalDateTime date;
    private Boolean completed = false;

    @ManyToOne
    private ClientEntity client;

    public InquiryEntity() {

    }

    public InquiryEntity(Inquiry inquiry) {
        this.inquiryId = inquiry.getId();
        this.text = inquiry.getText();
        this.type = inquiry.getType();
        this.date = inquiry.getDate();
        this.completed = inquiry.isCompleted();
        this.client = inquiry.getClientEntity();
    }
}
