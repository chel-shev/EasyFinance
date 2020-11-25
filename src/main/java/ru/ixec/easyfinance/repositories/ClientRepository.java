package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<ClientEntity, Long> {

    Boolean existsByUsername(String username);

    Optional<ClientEntity> findByUsername(String username);
}
