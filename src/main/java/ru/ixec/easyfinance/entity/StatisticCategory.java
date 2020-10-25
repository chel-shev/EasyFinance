package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class StatisticCategory {

    @Id
    @GeneratedValue
    private Long statisticCategoryId;
    private String singleName;
    private Long frequency;

    @ManyToOne
    private ExpenseCategory expenseCategory;
}
