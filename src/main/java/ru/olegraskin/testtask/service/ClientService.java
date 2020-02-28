package ru.olegraskin.testtask.service;

import ru.olegraskin.testtask.domain.Client;

import java.util.Set;

public interface ClientService {

    Set<Client> mergeRiskProfiles(Set<Client> ids);

    Client get(long id);

    Set<Client> getAll();

    Client save(Client client);

    Client update(Client client);

    void delete(long id);
}
