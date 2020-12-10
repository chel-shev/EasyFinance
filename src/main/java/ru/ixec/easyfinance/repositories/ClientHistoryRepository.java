package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.AccountHistoryEntity;

public interface ClientHistoryRepository extends CrudRepository<AccountHistoryEntity, Long> {

}
