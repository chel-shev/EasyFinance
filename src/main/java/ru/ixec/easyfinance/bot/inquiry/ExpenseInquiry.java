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
import ru.ixec.easyfinance.type.InquiryType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ExpenseInquiry extends Inquiry {

    private final Receipt receipt;

    public ExpenseInquiry(AccountEntity accountEntity) {
        super(InquiryType.EXPENSE, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        receipt = (Receipt) appCtx.getBean("receipt");
    }

    public ExpenseInquiry(InquiryEntity entity, AccountEntity accountEntity) {
        super(entity, accountEntity);
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        receipt = (Receipt) appCtx.getBean("receipt");
    }

    @Override
    public InquiryResponse process(String message) {
        try {
            this.setText(message);
            if (message.equals("Отмена")) {
                return cancel();
            } else if (message.split(":").length == 2) {
                return savePurchase(this.getAccountEntity(), message);
            } else {
                return saveReceipt(this.getAccountEntity(), message);
            }
        } catch (JSONException | NullPointerException e) {
            throw new BotException("Ошибка добавления!", true);
        }
    }

    private InquiryResponse savePurchase(AccountEntity accountEntity, String text) {
        String name = text.split(":")[0];
        double value = Double.parseDouble(text.split(":")[1].replace(",", ".").trim()) * 100;
        ExpenseProductEntity expenseProductEntity = new ExpenseProductEntity(name, null);
        ExpenseEntity expenseEntity = new ExpenseEntity(LocalDateTime.now(), (long) value, (long) value, 1.0, expenseProductEntity);
        es.save(expenseEntity, accountEntity);
        complete();
        return new InquiryResponse("Покупка добавлена!", false);
    }

    private InquiryResponse saveReceipt(AccountEntity accountEntity, String qr) throws JSONException {
        receipt.setQR(qr);
        List<ExpenseEntity> expenses = receipt.getExpenses();
        es.saveAll(expenses, accountEntity);
        complete();
        receipt.clear();
        return new InquiryResponse("Чек добавлен!", false);
    }


    @Override
    public void complete() {
        this.setCompleted(true);
        is.save(this.getEntity());
        new InquiryResponse();
    }

    @Override
    public InquiryResponse cancel() {
        this.setCompleted(true);
        is.save(this.getEntity());
        return new InquiryResponse("Добавление отменено!", false);
    }
}
