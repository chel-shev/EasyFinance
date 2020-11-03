package ru.ixec.easyfinance.entity;

import lombok.Data;
import ru.ixec.easyfinance.type.InquiryType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity
public class Inquiry {

    @Id
    @GeneratedValue
    private Long inquiryId;
    private String text;
    private InquiryType type;
    private LocalDateTime date;
    private Boolean completed = false;

    @ManyToOne
    private Client client;
}
