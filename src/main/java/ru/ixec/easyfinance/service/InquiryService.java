package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.Client;
import ru.ixec.easyfinance.entity.Inquiry;
import ru.ixec.easyfinance.repositories.InquiryRepository;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository ir;

    public Inquiry getLast(Client client) {
        return ir.findTopByClientOrderByDateDesc(client);
    }

    public Inquiry save(Inquiry inquiry) {
        return ir.save(inquiry);
    }
}
