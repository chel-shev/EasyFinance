package ru.ixec.easyfinance.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "statistic_category")
public class StatisticCategoryEntity {

    @Id
    @GeneratedValue
    private Long statisticCategoryId;
    private String singleName;
    private Long frequency;

    @ManyToOne
    private ExpenseCategoryEntity expenseCategory;
}
