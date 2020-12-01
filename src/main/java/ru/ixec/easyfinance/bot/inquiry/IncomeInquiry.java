package ru.ixec.easyfinance.bot.inquiry;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.IncomeEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.service.IncomeService;
import ru.ixec.easyfinance.type.ActionType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;

@Slf4j
public class IncomeInquiry extends Inquiry {

    private final IncomeService incS;

    public IncomeInquiry(AccountEntity accountEntity) {
        super(ActionType.INCOME, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        incS = (IncomeService) appCtx.getBean("incomeService");
    }

    public IncomeInquiry(InquiryEntity entity, AccountEntity accountEntity) {
        super(entity, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        incS = (IncomeService) appCtx.getBean("incomeService");
    }

    @Override
    public InquiryResponse process() {
        log.info("PROCESS IncomeInquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", getId(), getText(), getType(), getDate(), isCompleted());
        try {
            if (textEquals("Отмена"))
                return cancel();
            else if (isDoubleParam())
                return saveIncome();
            else
                return new InquiryResponse("Неверный формат!", true);
        } catch (JSONException | NullPointerException e) {
            throw new BotException("Ошибка добавления!", true);
        }
    }

    private InquiryResponse saveIncome() {
        long value = getValueFromParam(1);
        setAmount(value);
        String name = getNameFromParam(0);
        IncomeEntity incomeEntity = new IncomeEntity(name, value, LocalDateTime.now(), null, getAccountEntity());
        incS.save(incomeEntity, getAccountEntity());
        complete();
        return new InquiryResponse("Доход добавлен!", false);
    }
}
