package ru.ixec.easyfinance.bot.inquiry;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.service.ClientService;
import ru.ixec.easyfinance.service.InquiryService;
import ru.ixec.easyfinance.type.ActionType;
import ru.ixec.easyfinance.type.KeyboardType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Slf4j
@Data
public abstract class Inquiry {

    private Long id;
    private ActionType type;
    private String text;
    private LocalDateTime date;
    private Long amount;
    private boolean completed;

    private AccountEntity account;
    private ClientEntity client;

    private final ClientService cliS;
    private final InquiryService inqS;

    Inquiry(ActionType type, ClientEntity client) throws BotException {
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        inqS = (InquiryService) appCtx.getBean("inquiryService");
        cliS = (ClientService) appCtx.getBean("clientService");
        this.setClient(client);
        this.setDate(LocalDateTime.now());
        this.setCompleted(false);
        this.setType(type);
        InquiryEntity inquiryEntity = getInqS().save(getEntity());
        this.setId(inquiryEntity.getInquiryId());
        log.info("INIT Inquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", id, text, type, date, completed);
    }

    Inquiry(InquiryEntity entity, ClientEntity client) throws BotException {
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        inqS = (InquiryService) appCtx.getBean("inquiryService");
        cliS = (ClientService) appCtx.getBean("clientService");
        this.setClient(client);
        this.setCompleted(entity.getCompleted());
        if (!isNull(entity.getIn()))
            this.setAccount(entity.getIn());
        this.setDate(entity.getDate());
        this.setType(entity.getType());
        this.setId(entity.getInquiryId());
        log.info("CREATE Inquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", id, text, type, date, completed);
    }

    public abstract InquiryResponse process();

    public boolean isReadyForProcess() {
        return !isNull(getAccount());
    }

    public InquiryEntity getEntity() {
        return new InquiryEntity(this, getAccount(), null);
    }

    public InquiryResponse setAccountFromText(String accountName) {
        if (accountName.equals("Отмена"))
            return cancel();
        AccountEntity account = getAccount(accountName.split(" ")[1]);
        this.setAccount(account);
        getInqS().save(getEntity());
        return new InquiryResponse(getTextInfo(), KeyboardType.CANCEL);
    }

    public AccountEntity getAccount(String nameAccount) {
        return cliS.getAccountList(getClient().getClientId()).stream()
                .filter(e -> e.getName().equals(nameAccount))
                .findFirst()
                .orElseThrow(() -> new BotException("'" + nameAccount + "' не найден!", KeyboardType.CANCEL));
    }

    public void complete() {
        this.setCompleted(true);
        getInqS().save(this.getEntity());
        log.info("COMPLETE Inquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", id, text, type, date, completed);
    }

    public InquiryResponse cancel() {
        getInqS().delete(this.getEntity());
        log.info("CANCEL Inquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", id, text, type, date, completed);
        return new InquiryResponse("Действие отменено!", KeyboardType.INQUIRIES);
    }

    public String getTextInfo() {
        return type.getInfo();
    }

    public String getNameFromParam(int index) {
        return getText().split(":")[index].trim();
    }

    public long getValueFromParam(int index) {
        try {
            return (long) (Double.parseDouble(getText().split(":")[index]
                    .replace(",", ".")
                    .replace("–", "")
                    .replace("-", "")
                    .replace(" ", "")
                    .trim()) * 100);
        } catch (NumberFormatException e) {
            throw new BotException("Неверный формат!", KeyboardType.CANCEL);
        }
    }

    public boolean getDirectionFromParam(int index) {
        return !(getText().split(":")[index].contains("-") || getText().split(":")[index].contains("–"));
    }

    public boolean isDoubleParam() {
        return getText().split(":").length == 2;
    }

    public boolean isTripleParam() {
        return getText().split(":").length == 3;
    }
}
