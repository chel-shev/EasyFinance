package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.IncomeEntity;

public interface IncomeRepository extends CrudRepository<IncomeEntity, Long> {
}
