package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.exception.ClientServiceException;
import ru.ixec.easyfinance.repositories.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientEntity signUp(ClientEntity client) {
        if (clientRepository.existsByUsername(client.getUsername()))
            throw new ClientServiceException(String.format("Username: '%s' already exists!", client.getUsername()));
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client);
    }

    public List<AccountEntity> getAllAccount(Long clientId) {
        return clientRepository.findByClientId(clientId).getAccountList();
    }
}