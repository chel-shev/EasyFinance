package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.repositories.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository ar;

    public AccountEntity getAccountByChatId(Long chatId) {
        return ar.findByClientChatId(chatId)
                .stream()
                .filter(AccountEntity::getMain)
                .findFirst()
                .orElse(null);
    }
}
