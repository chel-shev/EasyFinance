package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.ClientEntity;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {

    Boolean existsByUsername(String username);

    Optional<ClientEntity> findByUsername(String username);

    ClientEntity findByClientId(Long clientId);

    Optional<ClientEntity> findByChatId(Long chatId);
}
