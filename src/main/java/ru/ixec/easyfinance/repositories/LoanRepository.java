package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.Loan;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {
}
