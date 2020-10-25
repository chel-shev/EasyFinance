package ru.ixec.easyfinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class ExpenseCategory {

    @Id
    @GeneratedValue
    private Long expenseCategoryId;
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "expenseCategory")
    private List<ExpenseProduct> productList;

    public ExpenseCategory(String name) {
        this.name = name;
    }
}