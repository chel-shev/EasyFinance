package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.LoanEntity;
import ru.ixec.easyfinance.repositories.LoanRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loaR;
    private final AccountService accS;

    public Collection<LoanEntity> getLoanByClient(ClientEntity client) {
        return loaR.findAllByAccountClient(client);
    }

    public void save(LoanEntity loan, AccountEntity account) {
        if (loan.getDirection())
            account.addAmount(loan.getAmount());
        else
            account.subAmount(loan.getAmount());
        accS.save(account);
        loaR.save(loan);
    }
}
