package ru.ixec.easyfinance.security.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtResponse implements Serializable {

    private final String username;
    private final String token;
}
