package ru.olegraskin.testtask.service;

import ru.olegraskin.testtask.domain.Client;

import javax.validation.constraints.NotNull;
import java.util.Set;

public interface ClientService {

    Set<Client> mergeRiskProfiles(@NotNull Set<Client> ids);

    Client get(@NotNull Long id);

    Set<Client> getAll();

    Client save(@NotNull Client client);

    Client update(@NotNull Client client);

    void delete(@NotNull Long id);
}
