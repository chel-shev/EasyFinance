package ru.ixec.easyfinance.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.repositories.ClientRepository;
import ru.ixec.easyfinance.security.client.ClientDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ClientEntity> clientOptional = clientRepository.findByUsername(username);
        ClientEntity clientEntity = clientOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("user"));
        return new ClientDetails(clientEntity.getUsername(), clientEntity.getPassword(), authorities, clientEntity);
    }
}
