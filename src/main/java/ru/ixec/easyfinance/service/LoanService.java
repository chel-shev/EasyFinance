package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.LoanEntity;
import ru.ixec.easyfinance.repositories.LoanRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loaR;

    public Collection<LoanEntity> getLoanByAccountId(Long accountId) {
        return loaR.findAllByAccount_AccountId(accountId);
    }

    public void save(LoanEntity loan) {
        loaR.save(loan);
    }
}
