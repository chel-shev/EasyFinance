package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.IncomeCategoryEntity;

public interface IncomeCategoryRepository extends CrudRepository<IncomeCategoryEntity, Long> {
}
