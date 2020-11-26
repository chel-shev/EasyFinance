package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.LoanEntity;

import java.util.Collection;

@Repository
public interface LoanRepository extends CrudRepository<LoanEntity, Long> {

    Collection<LoanEntity> findAllByAccount_AccountId(Long accountId);
}
