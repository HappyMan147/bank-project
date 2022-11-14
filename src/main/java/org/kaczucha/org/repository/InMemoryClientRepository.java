package org.kaczucha.org.repository;

import org.kaczucha.org.Client;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class InMemoryClientRepository implements ClientRepository {
    private List<Client> clients;

    public InMemoryClientRepository(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public void save(Client client) {
        clients.add(client);
    }

    @Override
    public Client findByEmail(String email) {
        return clients
                .stream()
                .filter(s -> Objects.equals(s.getEmail(), email))
                .findFirst()
                .orElseThrow(() ->new NoSuchElementException("Client with following email: " + email + " not found"));

    }
}
