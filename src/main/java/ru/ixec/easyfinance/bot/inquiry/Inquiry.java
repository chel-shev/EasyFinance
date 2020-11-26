package ru.ixec.easyfinance.bot.inquiry;

import lombok.Data;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.service.InquiryService;
import ru.ixec.easyfinance.type.InquiryType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;

@Data
public abstract class Inquiry {

    private Long id;
    private InquiryType type;
    private String text;
    private LocalDateTime date;
    private boolean completed;

    private ClientEntity clientEntity;
    private AccountEntity accountEntity;

    private final InquiryService inqS;

    Inquiry(InquiryType type, AccountEntity accountEntity) throws BotException {
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        inqS = (InquiryService) appCtx.getBean("inquiryService");
        this.setAccountEntity(accountEntity);
        this.setClientEntity(accountEntity.getClient());
        this.setDate(LocalDateTime.now());
        this.setCompleted(false);
        this.setType(type);
        InquiryEntity inquiryEntity = getInqS().save(getEntity());
        this.setId(inquiryEntity.getInquiryId());
    }

    Inquiry(InquiryEntity entity, AccountEntity accountEntity) throws BotException {
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        inqS = (InquiryService) appCtx.getBean("inquiryService");
        this.setAccountEntity(accountEntity);
        this.setClientEntity(accountEntity.getClient());
        this.setCompleted(entity.getCompleted());
        this.setDate(entity.getDate());
        this.setType(entity.getType());
        this.setId(entity.getInquiryId());
    }

    public InquiryEntity getEntity() {
        return new InquiryEntity(this);
    }

    public String getTextInfo() {
        return type.getInfo();
    }

    public abstract InquiryResponse process(String textMessage);

    public void complete() {
        this.setCompleted(true);
        getInqS().save(this.getEntity());
    }

    public InquiryResponse cancel() {
        this.setCompleted(true);
        getInqS().save(this.getEntity());
        return new InquiryResponse("Добавление отменено!", false);
    }

    public String getNameFromParam(String textMessage) {
        return textMessage.split(":")[0];
    }

    public long getValueFromParam(String textMessage) {
        return (long) Double.parseDouble(textMessage.split(":")[1]
                .replace(",", ".")
                .replace("–", "")
                .replace("-", "")
                .replace(" ", "")
                .trim()) * 100;
    }

    public boolean getDirectionFromParam(String textMessage) {
        return !(textMessage.split(":")[1].contains("-") || textMessage.split(":")[1].contains("–"));
    }

    public boolean isNameValueParam(String textMessage) {
        return textMessage.split(":").length == 2;
    }
}
