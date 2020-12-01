package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.LoanEntity;

import java.util.Collection;

public interface LoanRepository extends CrudRepository<LoanEntity, Long> {

    Collection<LoanEntity> findAllByAccount_AccountId(Long accountId);

    Collection<LoanEntity> findAllByAccountClient(ClientEntity client);
}
