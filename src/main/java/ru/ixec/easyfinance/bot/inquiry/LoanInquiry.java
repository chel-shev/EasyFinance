package ru.ixec.easyfinance.bot.inquiry;

import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.entity.LoanEntity;
import ru.ixec.easyfinance.service.LoanService;
import ru.ixec.easyfinance.type.InquiryType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public class LoanInquiry extends Inquiry {

    private final LoanService loaS;

    public LoanInquiry(AccountEntity accountEntity) {
        super(InquiryType.LOAN, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        loaS = (LoanService) appCtx.getBean("loanService");
    }

    public LoanInquiry(InquiryEntity entity, AccountEntity accountEntity) {
        super(entity, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        loaS = (LoanService) appCtx.getBean("loanService");
    }

    @Override
    public InquiryResponse process(String textMessage) {
        this.setText(textMessage);
        try {
            if (textMessage.equals("Отмена"))
                return cancel();
            else if (isNameValueParam(textMessage))
                return saveLoan(textMessage);
            else
                return new InquiryResponse("Неверный формат!", true);
        } catch (JSONException | NullPointerException e) {
            throw new BotException("Ошибка добавления!", true);
        }
    }

    private InquiryResponse saveLoan(String textMessage) {
        LoanEntity loanEntity = new LoanEntity(getNameFromParam(textMessage), getValueFromParam(textMessage), LocalDateTime.now(), null, getDirectionFromParam(textMessage), getAccountEntity());
        loaS.save(loanEntity);
        complete();
        return new InquiryResponse("Займ добавлен!", false);
    }

    @Override
    public String getTextInfo() {
        Collection<LoanEntity> loanByClient = loaS.getLoanByAccountId(getAccountEntity().getAccountId());
        String loanList = loanByClient.stream()
                .collect(Collectors.groupingBy(LoanEntity::getName))
                .values()
                .stream()
                .map(e -> {
                    String name = e.get(0).getName();
                    long amount = e.stream().mapToLong(l -> l.getDirection() ? l.getAmount() : (-1 * l.getAmount())).sum();
                    return new LoanEntity(name, amount);
                })
                .filter(e -> e.getAmount() != 0)
                .map(e -> "` " + e.getName() + ": " + e.getAmount() / 100 + " `\r\n")
                .collect(Collectors.joining());
        return String.format(getType().getInfo(), loanList);
    }
}
