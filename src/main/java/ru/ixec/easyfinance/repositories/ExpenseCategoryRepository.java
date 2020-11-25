package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.ExpenseCategoryEntity;

import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends CrudRepository<ExpenseCategoryEntity, Long> {

    Optional<ExpenseCategoryEntity> getByName(String name);
}
