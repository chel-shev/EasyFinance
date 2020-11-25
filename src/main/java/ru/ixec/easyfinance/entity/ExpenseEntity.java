package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Data
@Entity(name = "expense")
public class ExpenseEntity {

    @Id
    @GeneratedValue
    private Long expenseId;
    private LocalDateTime date;
    private Long price;
    private Double quantity;
    private Long sum;
    private boolean checked;
    private boolean deleted;
    private boolean confirmed;

    @ManyToOne
    private ExpenseProductEntity expenseProduct;

    @ManyToOne
    private AccountEntity account;

    public ExpenseEntity() {
    }

    public ExpenseEntity(LocalDateTime date, Long price, Long sum, Double quantity, ExpenseProductEntity expenseProduct) {
        this.date = date;
        this.price = price;
        this.sum = sum;
        this.quantity = quantity;
        this.expenseProduct = expenseProduct;
    }

    public String getProductName(){
        return isNull(expenseProduct) ? "" : expenseProduct.getName();
    }

    public String getProductCategoryName(){
        return isNull(expenseProduct) ? "" : expenseProduct.getCategoryName();
    }

    public ExpenseCategoryEntity getProductCategory(){
        return isNull(expenseProduct) ? null : expenseProduct.getExpenseCategory();
    }
}