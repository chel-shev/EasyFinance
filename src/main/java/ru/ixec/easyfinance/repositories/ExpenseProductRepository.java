package ru.ixec.easyfinance.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.ExpenseProductEntity;

import java.util.Optional;

@Repository
public interface ExpenseProductRepository extends CrudRepository<ExpenseProductEntity, Long> {

    Optional<ExpenseProductEntity> getByNameAndExpenseCategoryName(String expenseProductName, String expenseCategoryName);
    Optional<ExpenseProductEntity> findByName(String name);
}
