package ru.ixec.easyfinance.bot.inquiry;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.service.InquiryService;
import ru.ixec.easyfinance.type.ActionType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;

@Slf4j
@Data
public abstract class Inquiry {

    private Long id;
    private ActionType type;
    private String text;
    private LocalDateTime date;
    private Long amount;
    private boolean completed;

    private ClientEntity clientEntity;
    private AccountEntity accountEntity;

    private final InquiryService inqS;

    Inquiry(ActionType type, AccountEntity accountEntity) throws BotException {
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        inqS = (InquiryService) appCtx.getBean("inquiryService");
        this.setAccountEntity(accountEntity);
        this.setClientEntity(accountEntity.getClient());
        this.setDate(LocalDateTime.now());
        this.setCompleted(false);
        this.setType(type);
        InquiryEntity inquiryEntity = getInqS().save(getEntity());
        this.setId(inquiryEntity.getInquiryId());
        log.info("INIT Inquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", id, text, type, date, completed);
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
        log.info("CREATE Inquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", id, text, type, date, completed);
    }

    public InquiryEntity getEntity() {
        return new InquiryEntity(this);
    }

    public String getTextInfo() {
        return type.getInfo();
    }

    public abstract InquiryResponse process();

    public void complete() {
        this.setCompleted(true);
        getInqS().save(this.getEntity());
        log.info("COMPLETE Inquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", id, text, type, date, completed);
    }

    public InquiryResponse cancel() {
        complete();
        return new InquiryResponse("Добавление отменено!", false);
    }

    public String getNameFromParam(int index) {
        return getText().split(":")[index].trim();
    }

    public long getValueFromParam(int index) {
        return (long) (Double.parseDouble(getText().split(":")[index]
                .replace(",", ".")
                .replace("–", "")
                .replace("-", "")
                .replace(" ", "")
                .trim()) * 100);
    }

    public boolean getDirectionFromParam(int index) {
        return !(getText().split(":")[index].contains("-") || getText().split(":")[index].contains("–"));
    }

    public boolean textEquals(String value){
        return getText().equals(value);
    }

    public boolean isDoubleParam() {
        return getText().split(":").length == 2;
    }

    public boolean isTripleParam() {
        return getText().split(":").length == 3;
    }
}
