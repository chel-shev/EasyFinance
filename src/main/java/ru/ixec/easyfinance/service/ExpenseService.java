package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ExpenseEntity;
import ru.ixec.easyfinance.entity.ExpenseProductEntity;
import ru.ixec.easyfinance.repositories.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseProductService eps;
    private final ExpenseRepository er;

    public Iterable<ExpenseEntity> get() {
        return er.findAllByConfirmed(false);
    }

    public ExpenseEntity getById(Long id) {
        return er.findById(id).orElse(new ExpenseEntity());
    }

    public void save(ExpenseEntity expense, AccountEntity account) {
        ExpenseProductEntity ep = eps.getProduct(expense.getProductName());
        expense.setExpenseProduct(ep);
        expense.setAccount(account);
        er.save(expense);
    }

    public ExpenseEntity save(ExpenseEntity expense) {
        ExpenseProductEntity ep = eps.getProduct(expense.getProductName());
        expense.setExpenseProduct(ep);
        return er.save(expense);
    }

    public void saveAll(List<ExpenseEntity> expenseList, AccountEntity account) {
        expenseList.forEach(expense -> save(expense, account));
    }

    public void updateConfirmed(List<ExpenseEntity> expenseList) {
        expenseList.forEach(expense -> {
            if (expense.isConfirmed()) save(expense);
        });
    }
}