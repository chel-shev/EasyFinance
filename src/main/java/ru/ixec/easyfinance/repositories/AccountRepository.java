package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.AccountEntity;

import java.util.Collection;

public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    Collection<AccountEntity> findByClientChatId(Long chatId);
}
