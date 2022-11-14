package org.kaczucha.org.service;

import org.kaczucha.org.Client;
import org.kaczucha.org.repository.ClientRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BankService {
    private final ClientRepository clientRepository;

    public BankService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public void transfer(String fromEmail, String toEmial, double amount) {

        validateAmount(amount <= 0, "Amount must be positive");

        validateAmount(fromEmail.equals(toEmial), "fromEamil and toEmail can't be equal");


        Client fromClient = findByEmail(fromEmail);
        Client toClient = findByEmail(toEmial);


        if (fromClient.getBalance() - amount >= 0) {
            fromClient.setBalance(fromClient.getBalance() - amount);
            toClient.setBalance(toClient.getBalance() + amount);
        } else {
            throw new NoSuficientFundException("Not enought funds");
        }
    }

    public void withdraw(
            final String email,
            final double amount) {

        validateAmount(amount <= 0, "Amount must be positive");
        validateAmount(Objects.isNull(email), "Email can't be null");
        final Client client = findByEmail(email.toLowerCase());
        if (amount > client.getBalance()){
            throw new NoSuficientFundException("Balance must be higher or equal then amount");
        }
        final double newBalance = client.getBalance() - amount;
        client.setBalance(newBalance);

    }

    private void validateAmount(boolean amount, String Amount_must_be_positive) {
        if (amount) {
            throw new IllegalArgumentException(Amount_must_be_positive);
        }
    }
}
