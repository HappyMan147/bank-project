package org.kaczucha.org.service;

public class NoSuficientFundException extends RuntimeException {

    public NoSuficientFundException(String messange) {
        super(messange);
    }
}
