package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.TransferEntity;

public interface TransferRepository extends CrudRepository<TransferEntity, Long> {

}
