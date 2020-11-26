package ru.ixec.easyfinance.bot.inquiry;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.bot.receipt.Receipt;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ExpenseEntity;
import ru.ixec.easyfinance.entity.ExpenseProductEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.service.ExpenseService;
import ru.ixec.easyfinance.type.InquiryType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ExpenseInquiry extends Inquiry {

    private final ExpenseService expS;
    private final Receipt receipt;

    public ExpenseInquiry(AccountEntity accountEntity) {
        super(InquiryType.EXPENSE, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        expS = (ExpenseService) appCtx.getBean("expenseService");
        receipt = (Receipt) appCtx.getBean("receipt");
    }

    public ExpenseInquiry(InquiryEntity entity, AccountEntity accountEntity) {
        super(entity, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        expS = (ExpenseService) appCtx.getBean("expenseService");
        receipt = (Receipt) appCtx.getBean("receipt");
    }

    @Override
    public InquiryResponse process(String textMessage) {
        this.setText(textMessage);
        try {
            if (textMessage.equals("Отмена")) {
                return cancel();
            } else if (isNameValueParam(textMessage)) {
                return savePurchase(textMessage);
            } else {
                return saveReceipt(textMessage);
            }
        } catch (JSONException | NullPointerException e) {
            throw new BotException("Ошибка добавления!", true);
        }
    }

    private InquiryResponse savePurchase(String textMessage) {
        String name = getNameFromParam(textMessage);
        double value = getValueFromParam(textMessage);
        ExpenseProductEntity expenseProductEntity = new ExpenseProductEntity(name, null);
        ExpenseEntity expenseEntity = new ExpenseEntity(LocalDateTime.now(), (long) value, (long) value, 1.0, expenseProductEntity);
        expS.save(expenseEntity, getAccountEntity());
        complete();
        return new InquiryResponse("Покупка добавлена!", false);
    }

    private InquiryResponse saveReceipt(String qr) throws JSONException {
        receipt.setQR(qr);
        List<ExpenseEntity> expenses = receipt.getExpenses();
        expS.saveAll(expenses, getAccountEntity());
        complete();
        receipt.clear();
        return new InquiryResponse("Чек добавлен!", false);
    }
}
