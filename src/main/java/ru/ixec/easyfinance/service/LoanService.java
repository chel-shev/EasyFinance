package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.*;
import ru.ixec.easyfinance.repositories.ClientHistoryRepository;
import ru.ixec.easyfinance.repositories.LoanRepository;
import ru.ixec.easyfinance.type.ActionType;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loaR;
    private final AccountService accS;
    private final ClientHistoryRepository cliHisR;

    public Collection<LoanEntity> getLoanByClient(ClientEntity client) {
        return loaR.findAllByAccountClient(client);
    }

    public void save(LoanEntity loan, AccountEntity account) {
        if (loan.getDirection())
            account.addAccountBalance(loan.getAmount());
        else
            account.subAccountBalance(loan.getAmount());
        accS.save(account);
        AccountHistoryEntity entity = new AccountHistoryEntity(null, loan.getAmount(), account.getAccountBalance(), ActionType.LOAN, loan.getDateStart(), account);
        cliHisR.save(entity);
        loaR.save(loan);
    }
}
