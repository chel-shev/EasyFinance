package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.Client;
import ru.ixec.easyfinance.entity.Inquiry;

public interface InquiryRepository extends CrudRepository<Inquiry, Long> {
    Inquiry findTopByClientOrderByDateDesc(Client client);
}
