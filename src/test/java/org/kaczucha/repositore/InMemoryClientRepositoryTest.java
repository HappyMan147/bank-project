package org.kaczucha.repositore;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kaczucha.org.Client;
import org.kaczucha.org.repository.InMemoryClientRepository;

import java.util.*;

public class InMemoryClientRepositoryTest {
    private InMemoryClientRepository repository;
    private List<Client> clients;

    @BeforeEach
    public void setup() {
        clients = new ArrayList<>();
        repository = new InMemoryClientRepository(clients);
    }

    @Test
    public void verifyIfUserIsAddingCorrectlyToTheRepository() {
        //given
        final Client client = new Client("Alek", "a@gmail.com", 100);
        final Client expectedClient = new Client("Alek", "a@gmail.com", 100);

        //when
        repository.save(client);
        //then
        final Client altualClient = clients.stream().findFirst().get();


        assertEquals(expectedClient, altualClient);

    }
}

