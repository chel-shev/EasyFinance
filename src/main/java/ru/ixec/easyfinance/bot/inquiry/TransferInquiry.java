package ru.ixec.easyfinance.bot.inquiry;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.entity.TransferEntity;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.service.TransferService;
import ru.ixec.easyfinance.type.ActionType;
import ru.ixec.easyfinance.type.KeyboardType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Slf4j
public class TransferInquiry extends Inquiry {

    private final TransferService traS;
    private AccountEntity accountOut;

    public TransferInquiry(ClientEntity client) {
        super(ActionType.TRANSFER, client);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        traS = (TransferService) appCtx.getBean("transferService");

    }

    public TransferInquiry(InquiryEntity entity, ClientEntity client) {
        super(entity, client);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        traS = (TransferService) appCtx.getBean("transferService");
        if (!isNull(entity.getOut()))
            this.accountOut = entity.getOut();
    }

    @Override
    public InquiryResponse process() {
        log.info("PROCESS TransferInquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", getId(), getText(), getType(), getDate(), isCompleted());
        try {
            if (getText().equals("Отмена"))
                return cancel();
            return saveTransfer();
        } catch (JSONException | NullPointerException e) {
            throw new BotException("Ошибка добавления!", KeyboardType.CANCEL);
        }
    }

    @Transactional
    public InquiryResponse saveTransfer() {
        long value = getValueFromParam(0);
        setAmount(value);
        TransferEntity transferEntity = new TransferEntity(accountOut, getAccount(), value, LocalDateTime.now());
        traS.save(transferEntity);
        complete();
        return new InquiryResponse("Перевод добавлен!", KeyboardType.INQUIRIES);
    }

    @Override
    public InquiryEntity getEntity() {
        return new InquiryEntity(this, getAccount(), accountOut);
    }

    @Override
    public InquiryResponse setAccountFromText(String accountName) {
        if (accountName.equals("Отмена"))
            return cancel();
        AccountEntity account = getAccount(accountName.split(" ")[0]);
        if (isNull(this.getAccount())) {
            this.setAccount(account);
            getInqS().save(getEntity());
            return new InquiryResponse("Выберите счет, на который хотите совершить перевод:", KeyboardType.ACCOUNTS);
        } else {
            this.accountOut = account;
            getInqS().save(getEntity());
            return new InquiryResponse(getTextInfo(), KeyboardType.CANCEL);
        }
    }

    @Override
    public boolean isReadyForProcess() {
        return !(isNull(getAccount()) || isNull(accountOut));
    }
}
