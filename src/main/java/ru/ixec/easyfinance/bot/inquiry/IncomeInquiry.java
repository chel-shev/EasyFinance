package ru.ixec.easyfinance.bot.inquiry;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.IncomeEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.service.IncomeService;
import ru.ixec.easyfinance.type.InquiryType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;

@Slf4j
public class IncomeInquiry extends Inquiry {

    private final IncomeService incS;

    public IncomeInquiry(AccountEntity accountEntity) {
        super(InquiryType.INCOME, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        incS = (IncomeService) appCtx.getBean("incomeService");
    }

    public IncomeInquiry(InquiryEntity entity, AccountEntity accountEntity) {
        super(entity, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        incS = (IncomeService) appCtx.getBean("incomeService");
    }

    @Override
    public InquiryResponse process(String textMessage) {
        this.setText(textMessage);
        try {
            if (textMessage.equals("Отмена"))
                return cancel();
            else if (isNameValueParam(textMessage))
                return saveIncome(textMessage);
            else
                return new InquiryResponse("Неверный формат!", true);
        } catch (JSONException | NullPointerException e) {
            throw new BotException("Ошибка добавления!", true);
        }
    }

    private InquiryResponse saveIncome(String textMessage) {
        IncomeEntity incomeEntity = new IncomeEntity(getNameFromParam(textMessage), getValueFromParam(textMessage), LocalDateTime.now(), null, getAccountEntity());
        incS.save(incomeEntity);
        complete();
        return new InquiryResponse("Доход добавлен!", false);
    }
}
