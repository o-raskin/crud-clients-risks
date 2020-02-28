package ru.olegraskin.testtask.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.olegraskin.testtask.domain.Client;
import ru.olegraskin.testtask.repository.ClientRepository;
import ru.olegraskin.testtask.service.ClientService;
import ru.olegraskin.testtask.service.exception.ClientNotFoundException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "clientCache")
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Cacheable
    @Override
    public Client get(long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        return clientOptional.orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    public Set<Client> getAll() {
        return new HashSet<>(clientRepository.findAll());
    }

    @Transactional
    @Override
    public Client save(@NonNull Client client) {
        return clientRepository.save(client);
    }

    @CachePut(key = "#client.id")
    public Client update(@NonNull Client client) {
        Optional<Client> optionalClient = clientRepository.findById(client.getId());
        if (optionalClient.isPresent()) {
            Client stored = optionalClient.get();
            stored.setRiskProfile(client.getRiskProfile());
            return clientRepository.save(stored);
        } else {
            throw new ClientNotFoundException(client.getId());
        }
    }

    @CacheEvict
    @Transactional
    @Override
    public void delete(long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            clientRepository.deleteById(id);
        } else {
            throw new ClientNotFoundException(id);
        }
    }

    @Transactional
    @Override
    public Set<Client> mergeRiskProfiles(@NonNull Set<Client> clients) {
        List<Client> storedClients = clientRepository.findAllById(clients.stream()
                .map(Client::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        Optional<Client.RiskProfile> maxRiskProfile = storedClients.stream()
                .max(Comparator.comparing(Client::getRiskProfile))
                .map(Client::getRiskProfile);
        maxRiskProfile.ifPresent(riskProfile -> {
            storedClients.forEach(e -> e.setRiskProfile(riskProfile));
            clientRepository.saveAll(storedClients);
        });
        return new HashSet<>(storedClients);
    }
}
