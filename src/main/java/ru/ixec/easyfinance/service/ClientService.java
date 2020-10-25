package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.Client;
import ru.ixec.easyfinance.exception.ClientServiceException;
import ru.ixec.easyfinance.repositories.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public Client signUp(Client client) {
        if (clientRepository.existsByUsername(client.getUsername()))
            throw new ClientServiceException(String.format("Username: '%s' already exists!", client.getUsername()));
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client);
    }
}