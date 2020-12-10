package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.*;
import ru.ixec.easyfinance.repositories.ClientHistoryRepository;
import ru.ixec.easyfinance.repositories.ExpenseRepository;
import ru.ixec.easyfinance.type.ActionType;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseProductService expProS;
    private final ExpenseRepository expR;
    private final AccountService accS;
    private final ClientHistoryRepository cliHisR;

    public Iterable<ExpenseEntity> get() {
        return expR.findAllByConfirmed(false);
    }

    public ExpenseEntity getById(Long id) {
        return expR.findById(id).orElse(new ExpenseEntity());
    }

    @Transactional
    public void save(ExpenseEntity expense, AccountEntity account) {
        ExpenseProductEntity ep = expProS.getProduct(expense.getProductName());
        expense.setExpenseProduct(ep);
        expense.setAccount(account);
        expR.save(expense);
    }

    public ExpenseEntity save(ExpenseEntity expense) {
        ExpenseProductEntity ep = expProS.getProduct(expense.getProductName());
        expense.setExpenseProduct(ep);
        return expR.save(expense);
    }

    public void save(ExpenseEntity expense, AccountEntity account, long sum) {
        account.subAccountBalance(sum);
        accS.save(account);
        AccountHistoryEntity entity = new AccountHistoryEntity(null, sum, account.getAccountBalance(), ActionType.EXPENSE, expense.getDate(), account);
        cliHisR.save(entity);
        save(expense, account);
    }

    public void saveAll(List<ExpenseEntity> expenseList, AccountEntity account, long sum) {
        account.subAccountBalance(sum);
        accS.save(account);
        AccountHistoryEntity entity = new AccountHistoryEntity(null, sum, account.getAccountBalance(), ActionType.EXPENSE, expenseList.get(0).getDate(), account);
        cliHisR.save(entity);
        expenseList.forEach(expense -> save(expense, account));
    }

    public void updateConfirmed(List<ExpenseEntity> expenseList) {
        expenseList.forEach(expense -> {
            if (expense.isConfirmed()) save(expense);
        });
    }
}