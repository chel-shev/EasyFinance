package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.Account;
import ru.ixec.easyfinance.repositories.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository ar;

    public Account getAccountByChatId(Long chatId) {
        return ar.findByClientChatId(chatId)
                .stream()
                .filter(Account::getMain)
                .findFirst()
                .orElse(null);
    }
}
