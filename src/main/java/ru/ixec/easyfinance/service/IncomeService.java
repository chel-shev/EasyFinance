package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.IncomeEntity;
import ru.ixec.easyfinance.repositories.IncomeRepository;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incR;

    public void save(IncomeEntity income) {
        incR.save(income);
    }
}
