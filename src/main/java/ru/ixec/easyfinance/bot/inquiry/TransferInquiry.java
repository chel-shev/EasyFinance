package ru.ixec.easyfinance.bot.inquiry;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.entity.TransferEntity;
import ru.ixec.easyfinance.service.AccountService;
import ru.ixec.easyfinance.service.ClientService;
import ru.ixec.easyfinance.service.TransferService;
import ru.ixec.easyfinance.type.ActionType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
public class TransferInquiry extends Inquiry {

    private final TransferService traS;
    private final ClientService cliS;
    private final AccountService accS;

    public TransferInquiry(AccountEntity accountEntity) {
        super(ActionType.TRANSFER, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        traS = (TransferService) appCtx.getBean("transferService");
        cliS = (ClientService) appCtx.getBean("clientService");
        accS = (AccountService) appCtx.getBean("accountService");
    }

    public TransferInquiry(InquiryEntity entity, AccountEntity accountEntity) {
        super(entity, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        traS = (TransferService) appCtx.getBean("transferService");
        cliS = (ClientService) appCtx.getBean("clientService");
        accS = (AccountService) appCtx.getBean("accountService");
    }

    @Override
    public InquiryResponse process() {
        log.info("PROCESS TransferInquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", getId(), getText(), getType(), getDate(), isCompleted());
        try {
            if (textEquals("Отмена"))
                return cancel();
            else if (isTripleParam())
                return saveTransfer();
            return new InquiryResponse("Неверный формат!", true);
        } catch (JSONException | NullPointerException e) {
            throw new BotException("Ошибка добавления!", true);
        }
    }

    @Transactional
    public InquiryResponse saveTransfer() {
        long value = getValueFromParam(1);
        setAmount(value);
        AccountEntity accountIn = getAccount(getNameFromParam(2));
        AccountEntity accountOut = getAccount(getNameFromParam(0));
        TransferEntity transferEntity = new TransferEntity(accountIn, accountOut, value, LocalDateTime.now());
        traS.save(transferEntity);
        complete();
        return new InquiryResponse("Перевод добавлен!", false);
    }

    private AccountEntity getAccount(String nameAccount) {
        return cliS.getAllAccount(getClientEntity().getClientId()).stream()
                .filter(e -> e.getName().equals(nameAccount))
                .findFirst()
                .orElseThrow(() -> new BotException("'" + nameAccount + "' не найден!", true));
    }

    @Override
    public String getTextInfo() {
        String accountList = cliS.getAllAccount(getClientEntity().getClientId()).stream().map(a -> "` " + a.getName() + ": " + String.format("%.2f", a.getAmount() / 100d) + " " + (a.isMain() ? "⭐" : "") + "`\r\n").collect(Collectors.joining());
        return String.format(getType().info, accountList);
    }

    @Override
    public InquiryResponse cancel() {
        complete();
        return new InquiryResponse("Перевод отменен!", false);
    }
}
