package ru.olegraskin.testtask.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.olegraskin.testtask.domain.Client;
import ru.olegraskin.testtask.dto.ClientDTO;
import ru.olegraskin.testtask.mapper.ClientMapper;
import ru.olegraskin.testtask.service.ClientService;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(
        value = "/clients",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @PostMapping("/merge")
    public Set<ClientDTO> mergeRiskProfile(@RequestBody Set<ClientDTO> clientsDTOs) {
        Set<Client> clients = clientsDTOs.stream().map(clientMapper::convertToEntity).collect(Collectors.toSet());
        Set<Client> mergedClients = clientService.mergeRiskProfiles(clients);
        return mergedClients.stream().map(clientMapper::convertToDTO).collect(Collectors.toSet());
    }

    @GetMapping
    public Set<ClientDTO> getClients() {
        return clientService.getAll().stream()
                .map(clientMapper::convertToDTO)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ClientDTO get(@PathVariable("id") Long id) {
        Client client = clientService.get(id);
        return clientMapper.convertToDTO(client);
    }

    @PostMapping
    public ClientDTO create(@Valid @RequestBody ClientDTO dto) {
        Client client = clientMapper.convertToEntity(dto);
        Client savedClient = clientService.save(client);
        return clientMapper.convertToDTO(savedClient);
    }

    @PutMapping("/{id}")
    public ClientDTO update(@Valid @RequestBody ClientDTO dto,
                            @PathVariable("id") Long id) {
        dto.setId(id);
        Client client = clientMapper.convertToEntity(dto);
        Client updatedClient = clientService.update(client);
        return clientMapper.convertToDTO(updatedClient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        clientService.delete(id);
    }
}
