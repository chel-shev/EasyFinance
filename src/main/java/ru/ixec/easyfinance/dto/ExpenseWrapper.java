package ru.ixec.easyfinance.dto;

import lombok.Data;
import ru.ixec.easyfinance.entity.Expense;

import java.util.List;

@Data
public class ExpenseWrapper {
    private List<Expense> expenses;
}
