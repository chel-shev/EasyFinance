package ru.ixec.easyfinance.bot.inquiry;

import lombok.Data;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.service.ExpenseService;
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

    final ExpenseService es;
    final InquiryService is;

    Inquiry(InquiryType type, AccountEntity accountEntity) throws BotException {
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        es = (ExpenseService) appCtx.getBean("expenseService");
        is = (InquiryService) appCtx.getBean("inquiryService");
        this.setAccountEntity(accountEntity);
        this.setClientEntity(accountEntity.getClient());
        this.setDate(LocalDateTime.now());
        this.setCompleted(false);
        this.setType(type);
        InquiryEntity inquiryEntity = is.save(getEntity());
        this.setId(inquiryEntity.getInquiryId());
    }

    Inquiry(InquiryEntity entity, AccountEntity accountEntity) throws BotException {
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        es = (ExpenseService) appCtx.getBean("expenseService");
        is = (InquiryService) appCtx.getBean("inquiryService");
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

    public abstract InquiryResponse process(String message);

    public abstract void complete();

    public abstract InquiryResponse cancel();
}
