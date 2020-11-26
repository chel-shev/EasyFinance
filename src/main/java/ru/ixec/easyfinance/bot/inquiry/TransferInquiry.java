package ru.ixec.easyfinance.bot.inquiry;

import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.type.InquiryType;

public class TransferInquiry extends Inquiry {

    public TransferInquiry(AccountEntity accountEntity) {
        super(InquiryType.TRANSFER, accountEntity);
    }

    public TransferInquiry(InquiryEntity entity, AccountEntity accountEntity) {
        super(entity, accountEntity);
    }

    @Override
    public InquiryResponse process(String textMessage) {
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
