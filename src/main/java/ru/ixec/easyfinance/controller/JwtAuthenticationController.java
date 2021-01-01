package ru.ixec.easyfinance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.security.JwtTokenProvider;
import ru.ixec.easyfinance.security.dto.JwtResponse;
import ru.ixec.easyfinance.service.ClientService;

import static java.util.Objects.isNull;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ClientService clientService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<?> login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            ClientEntity clientEntity = clientService.getClientByName(username);
            if (isNull(clientEntity))
                throw new UsernameNotFoundException("User not found!");
            String token = jwtTokenProvider.createToken(username);
            return ResponseEntity.ok(new JwtResponse(username, token));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
