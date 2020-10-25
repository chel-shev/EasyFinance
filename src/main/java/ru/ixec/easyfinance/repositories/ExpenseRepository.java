package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.Expense;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {

    Iterable<Expense> findAllByConfirmed(Boolean confirmed);
}
