package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.IncomeEntity;
import ru.ixec.easyfinance.repositories.IncomeRepository;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incR;
    private final AccountService accS;

    public void save(IncomeEntity income, AccountEntity account) {
        account.addAmount(income.getAmount());
        accS.save(account);
        incR.save(income);
    }
}
