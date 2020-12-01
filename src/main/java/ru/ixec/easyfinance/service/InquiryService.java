package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.bot.factory.InquiryFactory;
import ru.ixec.easyfinance.bot.inquiry.Inquiry;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;
import ru.ixec.easyfinance.repositories.InquiryRepository;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inqR;

    public Inquiry getLast(ClientEntity client) {
        InquiryEntity entity = inqR.findTopByClientOrderByDateDesc(client);
        if (isNull(entity))
            return null;
        return InquiryFactory.createInquiry(entity, client);
    }

    public InquiryEntity save(InquiryEntity inquiry) {
        return inqR.save(inquiry);
    }

    public void delete(InquiryEntity entity) {
        inqR.delete(entity);
    }
}
