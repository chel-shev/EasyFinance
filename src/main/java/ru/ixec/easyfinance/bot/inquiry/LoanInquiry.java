package ru.ixec.easyfinance.bot.inquiry;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.entity.LoanEntity;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.service.LoanService;
import ru.ixec.easyfinance.type.ActionType;
import ru.ixec.easyfinance.type.KeyboardType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class LoanInquiry extends Inquiry {

    private final LoanService loaS;

    public LoanInquiry(ClientEntity client) {
        super(ActionType.LOAN, client);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        loaS = (LoanService) appCtx.getBean("loanService");
    }

    public LoanInquiry(InquiryEntity entity, ClientEntity client) {
        super(entity, client);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        loaS = (LoanService) appCtx.getBean("loanService");
    }

    @Override
    public InquiryResponse process() {
        log.info("PROCESS LoanInquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", getId(), getText(), getType(), getDate(), isCompleted());
        try {
            if (getText().equals("Отмена"))
                return cancel();
            else if (isDoubleParam())
                return saveLoan();
            else
                return new InquiryResponse("Неверный формат!", KeyboardType.CANCEL);
        } catch (JSONException | NullPointerException e) {
            throw new BotException("Ошибка добавления!", KeyboardType.CANCEL);
        }
    }

    @Override
    public String getTextInfo() {
        Collection<LoanEntity> loanByClient = loaS.getLoanByClient(getClient());
        String loanList = loanByClient.stream()
                .collect(Collectors.groupingBy(LoanEntity::getName))
                .values()
                .stream()
                .map(e -> {
                    String name = e.get(0).getName();
                    long amount = e.stream().mapToLong(l -> l.getDirection() ? l.getAmount() : -1 * l.getAmount()).sum();
                    return new LoanEntity(name, amount);
                })
                .filter(e -> e.getAmount() != 0)
                .map(e -> "` " + e.getName() + ": " + String.format("%.2f", e.getAmount() / 100d) + " `\r\n")
                .collect(Collectors.joining());
        return String.format(getType().getInfo(), loanList);
    }

    private InquiryResponse saveLoan() {
        String name = getNameFromParam(0);
        long value = getValueFromParam(1);
        boolean direction = getDirectionFromParam(1);
        setAmount(direction ? value : -1 * value);
        LoanEntity loanEntity = new LoanEntity(name, value, LocalDateTime.now(), null, direction, getAccount());
        loaS.save(loanEntity, getAccount());
        complete();
        return new InquiryResponse("Займ добавлен!", KeyboardType.INQUIRIES);
    }
}
