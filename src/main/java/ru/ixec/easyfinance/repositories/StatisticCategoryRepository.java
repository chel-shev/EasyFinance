package ru.ixec.easyfinance.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.StatisticCategoryEntity;

public interface StatisticCategoryRepository extends CrudRepository<StatisticCategoryEntity, Long> {

    Iterable<StatisticCategoryEntity> findAll(Sort by);
}
