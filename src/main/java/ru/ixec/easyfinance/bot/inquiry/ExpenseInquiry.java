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
import ru.ixec.easyfinance.type.ActionType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ExpenseInquiry extends Inquiry {

    private final ExpenseService expS;
    private final Receipt receipt;

    public ExpenseInquiry(AccountEntity accountEntity) {
        super(ActionType.EXPENSE, accountEntity);
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
    public InquiryResponse process() {
        log.info("PROCESS ExpenseInquiry(inquiryId: {}, text: {}, type: {}, date: {}, completed: {})", getId(), getText(), getType(), getDate(), isCompleted());
        try {
            if (textEquals("Отмена")) {
                return cancel();
            } else if (isDoubleParam()) {
                return savePurchase();
            } else {
                return saveReceipt();
            }
        } catch (JSONException | NullPointerException e) {
            throw new BotException("Ошибка добавления!", true);
        }
    }

    private InquiryResponse savePurchase() {
        String name = getNameFromParam(0);
        long value = getValueFromParam(1);
        setAmount(value);
        ExpenseProductEntity expenseProductEntity = new ExpenseProductEntity(name, null);
        ExpenseEntity expenseEntity = new ExpenseEntity(LocalDateTime.now(), value, expenseProductEntity);
        expS.save(expenseEntity, getAccountEntity());
        complete();
        return new InquiryResponse("Расход добавлена!", false);
    }

    private InquiryResponse saveReceipt() throws JSONException {
        receipt.setQR(getText());
        List<ExpenseEntity> expenses = receipt.getExpenses();
        setAmount(receipt.getSum());
        expS.saveAll(expenses, getAccountEntity(), getAmount());
        complete();
        receipt.clear();
        return new InquiryResponse("Чек добавлен!", false);
    }
}
