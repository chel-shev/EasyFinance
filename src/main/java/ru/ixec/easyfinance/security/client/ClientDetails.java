package ru.ixec.easyfinance.security.client;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.ixec.easyfinance.entity.ClientEntity;

import java.util.Collection;

public class ClientDetails extends User {

    @Getter
    private final ClientEntity clientEntity;

    public ClientDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, ClientEntity clientEntity) {
        super(username, password, authorities);
        this.clientEntity = clientEntity;
    }

    public ClientDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, ClientEntity clientEntity) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.clientEntity = clientEntity;
    }
}
