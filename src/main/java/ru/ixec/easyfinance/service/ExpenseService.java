package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ExpenseEntity;
import ru.ixec.easyfinance.entity.ExpenseProductEntity;
import ru.ixec.easyfinance.repositories.*;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseProductService expProS;
    private final ExpenseRepository expR;
    private final AccountService accS;

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

    public void save(ExpenseEntity expense, AccountEntity account, long sum){
        account.subAmount(sum);
        accS.save(account);
        save(expense, account);
    }

    public void saveAll(List<ExpenseEntity> expenseList, AccountEntity account, long sum) {
        account.subAmount(sum);
        accS.save(account);
        expenseList.forEach(expense -> save(expense, account));
    }

    public void updateConfirmed(List<ExpenseEntity> expenseList) {
        expenseList.forEach(expense -> {
            if (expense.isConfirmed()) save(expense);
        });
    }
}