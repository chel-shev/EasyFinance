package ru.ixec.easyfinance.bot.factory;

import ru.ixec.easyfinance.bot.inquiry.*;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.type.ActionType;
import ru.ixec.easyfinance.type.KeyboardType;

public final class InquiryFactory {

    public static void initInquiry(String typeText, ClientEntity client) {
        try {
            ActionType type = ActionType.get(typeText);
            switch (type) {
                case EXPENSE:
                    new ExpenseInquiry(client);
                    break;
                case INCOME:
                    new IncomeInquiry(client);
                    break;
                case LOAN:
                    new LoanInquiry(client);
                    break;
                case TRANSFER:
                    new TransferInquiry(client);
                    break;
                default:
                    throw new BotException("Неверный тип операции!", KeyboardType.INQUIRIES);
            }
        } catch (NullPointerException e) {
            throw new BotException("Неверный тип операции!", KeyboardType.INQUIRIES);
        }
    }

    public static Inquiry createInquiry(InquiryEntity entity, ClientEntity client) {
        Inquiry inquiry;
        switch (entity.getType()) {
            case EXPENSE:
                inquiry = new ExpenseInquiry(entity, client);
                break;
            case INCOME:
                inquiry = new IncomeInquiry(entity, client);
                break;
            case LOAN:
                inquiry = new LoanInquiry(entity, client);
                break;
            case TRANSFER:
                inquiry = new TransferInquiry(entity, client);
                break;
            default:
                throw new BotException("Некорректный запрос!", KeyboardType.INQUIRIES);
        }
        return inquiry;
    }
}