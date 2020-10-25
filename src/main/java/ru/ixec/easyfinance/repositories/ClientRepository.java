package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ixec.easyfinance.entity.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    Boolean existsByUsername(String username);

    Optional<Client> findByUsername(String username);
}
