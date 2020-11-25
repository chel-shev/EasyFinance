package ru.ixec.easyfinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name="expense_category")
public class ExpenseCategoryEntity {

    @Id
    @GeneratedValue
    private Long expenseCategoryId;
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "expenseCategory")
    private List<ExpenseProductEntity> productList;

    public ExpenseCategoryEntity(String name) {
        this.name = name;
    }
}