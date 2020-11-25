package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.bot.inquiry.Inquiry;
import ru.ixec.easyfinance.bot.inquiry.InquiryFactory;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.repositories.InquiryRepository;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository ir;

    public Inquiry getLast(AccountEntity account) {
        InquiryEntity entity = ir.findTopByClientOrderByDateDesc(account.getClient());
        return InquiryFactory.createInquiry(entity, account);
    }

    public InquiryEntity save(InquiryEntity inquiry) {
        return ir.save(inquiry);
    }
}
