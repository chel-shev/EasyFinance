package ru.ixec.easyfinance.bot.inquiry;

import ru.ixec.easyfinance.bot.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.type.ActionType;

public class InquiryFactory {

    public static Inquiry createInquiry(String typeText, AccountEntity accountEntity) {
        try {
            ActionType type = ActionType.get(typeText);
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
                    throw new BotException("Неверный тип операции!", false);
            }
            return inquiry;
        } catch (NullPointerException e) {
            throw new BotException("Неверный тип операции!", false);
        }
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