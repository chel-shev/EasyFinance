package ru.ixec.easyfinance.dto;

import lombok.Data;
import ru.ixec.easyfinance.entity.ExpenseEntity;

import java.util.List;

@Data
public class ExpenseWrapper {
    private List<ExpenseEntity> expens;
}
