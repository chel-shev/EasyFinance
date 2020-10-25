package ru.ixec.easyfinance.exception;

public class ClientServiceException extends RuntimeException {

    public ClientServiceException(String message) {
        super(message);
    }
}