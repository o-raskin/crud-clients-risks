package ru.olegraskin.testtask.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.olegraskin.testtask.domain.Client;
import ru.olegraskin.testtask.dto.ClientDTO;
import ru.olegraskin.testtask.mapper.ClientMapper;

@RequiredArgsConstructor
@Component
public class ClientMapperImpl implements ClientMapper {

    private final ModelMapper modelMapper;

    @Override
    public ClientDTO convertToDTO(Client client) {
        return modelMapper.map(client, ClientDTO.class);
    }

    @Override
    public Client convertToEntity(ClientDTO dto) {
        return modelMapper.map(dto, Client.class);
    }
}
