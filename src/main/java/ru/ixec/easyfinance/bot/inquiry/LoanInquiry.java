package ru.ixec.easyfinance.bot.inquiry;

import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.type.InquiryType;

public class LoanInquiry extends Inquiry {

    public LoanInquiry(AccountEntity accountEntity) {
        super(InquiryType.LOAN, accountEntity);
    }

    public LoanInquiry(InquiryEntity entity, AccountEntity accountEntity) {
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
