package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.entity.InquiryEntity;

import java.util.Collection;

public interface InquiryRepository extends CrudRepository<InquiryEntity, Long> {
    InquiryEntity findTopByClientOrderByDateDesc(ClientEntity clientEntity);
}
