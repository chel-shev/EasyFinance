package ru.ixec.easyfinance.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.ExpenseProduct;

import java.util.Optional;

@Repository
public interface ExpenseProductRepository extends CrudRepository<ExpenseProduct, Long> {

    Optional<ExpenseProduct> getByNameAndExpenseCategoryName(String expenseProductName, String expenseCategoryName);
    Optional<ExpenseProduct> findByName(String name);
}
