package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.Account;
import ru.ixec.easyfinance.entity.Expense;
import ru.ixec.easyfinance.entity.ExpenseProduct;
import ru.ixec.easyfinance.repositories.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseProductService eps;
    private final ExpenseRepository er;

    public Iterable<Expense> get() {
        return er.findAllByConfirmed(false);
    }

    public Expense getById(Long id) {
        return er.findById(id).orElse(new Expense());
    }

    public void save(Expense expense, Account account) {
        ExpenseProduct ep = eps.getProduct(expense.getProductName());
        expense.setExpenseProduct(ep);
        expense.setAccount(account);
        er.save(expense);
    }

    public Expense save(Expense expense) {
        ExpenseProduct ep = eps.getProduct(expense.getProductName());
        expense.setExpenseProduct(ep);
        return er.save(expense);
    }

    public void saveAll(List<Expense> expenseList, Account account) {
        expenseList.forEach(expense -> save(expense, account));
    }

    public void updateConfirmed(List<Expense> expenseList) {
        expenseList.forEach(expense -> {
            if (expense.isConfirmed()) save(expense);
        });
    }
}