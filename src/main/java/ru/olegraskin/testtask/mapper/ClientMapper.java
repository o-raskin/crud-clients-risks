package ru.olegraskin.testtask.mapper;

import ru.olegraskin.testtask.domain.Client;
import ru.olegraskin.testtask.dto.ClientDTO;

public interface ClientMapper {

    ClientDTO convertToDTO(Client client);

    Client convertToEntity(ClientDTO dto);
}
