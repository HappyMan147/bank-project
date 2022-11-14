package org.kaczucha.org.repository;

import org.kaczucha.org.Client;

public interface ClientRepository {
    void  save(Client client);

    Client findByEmail(String email);
}
