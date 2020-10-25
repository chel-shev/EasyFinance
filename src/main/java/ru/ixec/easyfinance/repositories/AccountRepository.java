package ru.ixec.easyfinance.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ixec.easyfinance.entity.Account;

import java.util.Collection;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Collection<Account> findByClientChatId(Long chatId);
}
