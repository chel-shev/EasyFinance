package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.*;

import static java.util.Objects.isNull;

@Data
@Entity
public class ExpenseProduct {

    @Id
    @GeneratedValue
    private Long expenseProductId;
    private String name;

    @ManyToOne
    private ExpenseCategory expenseCategory;

    public ExpenseProduct() {
    }

    public ExpenseProduct(String name, ExpenseCategory expenseCategory) {
        this.name = name;
        this.expenseCategory = expenseCategory;
    }

    public String getCategoryName(){
        return isNull(expenseCategory) ? "" : expenseCategory.getName();
    }
}