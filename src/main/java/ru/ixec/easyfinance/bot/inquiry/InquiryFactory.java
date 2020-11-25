package ru.ixec.easyfinance.bot.inquiry;

import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.type.InquiryType;

public class InquiryFactory {

    public static Inquiry createInquiry(String typeText, AccountEntity accountEntity) {
        InquiryType type = InquiryType.get(typeText);
        Inquiry inquiry;
        switch (type) {
            case EXPENSE:
                inquiry = new ExpenseInquiry(accountEntity);
                break;
            case INCOME:
                inquiry = new IncomeInquiry(accountEntity);
                break;
            case LOAN:
                inquiry = new LoanInquiry(accountEntity);
                break;
            case TRANSFER:
                inquiry = new TransferInquiry(accountEntity);
                break;
            default:
                throw new BotException("Неверный тип операции!", true);
        }
        return inquiry;
    }

    public static Inquiry createInquiry(InquiryEntity entity, AccountEntity accountEntity) {
        Inquiry inquiry;
        switch (entity.getType()) {
            case EXPENSE:
                inquiry = new ExpenseInquiry(entity, accountEntity);
                break;
            case INCOME:
                inquiry = new IncomeInquiry(entity, accountEntity);
                break;
            case LOAN:
                inquiry = new LoanInquiry(entity, accountEntity);
                break;
            case TRANSFER:
                inquiry = new TransferInquiry(entity, accountEntity);
                break;
            default:
                throw new BotException("Некорректный запрос!", false);
        }
        return inquiry;
    }
}