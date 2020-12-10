package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.AccountHistoryEntity;
import ru.ixec.easyfinance.entity.IncomeEntity;
import ru.ixec.easyfinance.repositories.ClientHistoryRepository;
import ru.ixec.easyfinance.repositories.IncomeRepository;
import ru.ixec.easyfinance.type.ActionType;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incR;
    private final AccountService accS;
    private final ClientHistoryRepository cliHisR;

    public void save(IncomeEntity income, AccountEntity account) {
        account.addAccountBalance(income.getAmount());
        accS.save(account);
        AccountHistoryEntity entity = new AccountHistoryEntity(null, income.getAmount(), account.getAccountBalance(), ActionType.INCOME, income.getDate(), account);
        cliHisR.save(entity);
        incR.save(income);
    }
}
