package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.exception.EasyFinanceException;
import ru.ixec.easyfinance.repositories.ClientRepository;
import ru.ixec.easyfinance.type.KeyboardType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository cliR;
    private final PasswordEncoder passwordEncoder;

    public ClientEntity signUp(ClientEntity client) {
        if (cliR.existsByUsername(client.getUsername()))
            throw new EasyFinanceException(String.format("Username: '%s' already exists!", client.getUsername()));
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return cliR.save(client);
    }

    public ClientEntity getClientByName(String name) {
        return cliR.findByUsername(name).orElse(null);
    }

    public ClientEntity getClientByChatId(Long chatId) {
        return cliR.findByChatId(chatId).orElseThrow(() -> new BotException("Пользователь не найден!", KeyboardType.CANCEL));
    }

    public List<AccountEntity> getAccountList(Long clientId) {
        return cliR.findByClientId(clientId).getAccountList();
    }
}