package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.ExpenseEntity;

@Repository
public interface ExpenseRepository extends CrudRepository<ExpenseEntity, Long> {

    Iterable<ExpenseEntity> findAllByConfirmed(Boolean confirmed);
}
