package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.ExpenseCategory;

import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends CrudRepository<ExpenseCategory, Long> {

    Optional<ExpenseCategory> getByName(String name);
}
