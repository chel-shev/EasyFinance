package ru.ixec.easyfinance.bot.inquiry;

import lombok.extern.slf4j.Slf4j;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.type.InquiryType;

@Slf4j
public class IncomeInquiry extends Inquiry {

    public IncomeInquiry(AccountEntity accountEntity) {
        super(InquiryType.INCOME, accountEntity);
    }

    public IncomeInquiry(InquiryEntity entity, AccountEntity accountEntity) {
        super(entity, accountEntity);
    }

    @Override
    public InquiryResponse process(String message) {
        return new InquiryResponse();
    }

    @Override
    public void complete() {
        new InquiryResponse();
    }

    @Override
    public InquiryResponse cancel() {
        return new InquiryResponse();
    }
}
