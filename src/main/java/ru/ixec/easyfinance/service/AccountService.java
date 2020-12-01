package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.repositories.AccountRepository;
import ru.ixec.easyfinance.type.KeyboardType;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accR;

    public AccountEntity getAccountByChatId(Long chatId) {
        return accR.findByClientChatId(chatId)
                .stream()
                .filter(AccountEntity::isMain)
                .findFirst()
                .orElseThrow(() ->  new BotException("Пользователь не найден!", KeyboardType.CANCEL));
    }

    public Collection<AccountEntity> getAccountListByChatId(Long chatId) {
        return new ArrayList<>(accR.findByClientChatId(chatId));
    }

    public AccountEntity save(AccountEntity account) {
        return accR.save(account);
    }
}
