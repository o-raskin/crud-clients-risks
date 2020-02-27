package ru.olegraskin.testtask.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.olegraskin.testtask.domain.Client;
import ru.olegraskin.testtask.repository.ClientRepository;
import ru.olegraskin.testtask.service.exception.ClientNotFoundException;
import ru.olegraskin.testtask.service.impl.ClientServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class ClientServiceImplTest {

    @MockBean
    private ClientRepository clientRepository;

    private ClientServiceImpl clientService;

    @Before
    public void init() {
        clientService = new ClientServiceImpl(clientRepository);
    }

    @Test
    public void testGetSuccess() {

        Client client = new Client();
        client.setId(1L);
        client.setRiskProfile(Client.RiskProfile.LOW);

        given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

        Client res = clientService.get(client.getId());

        Assert.assertEquals(client, res);
    }

    @Test(expected = ClientNotFoundException.class)
    public void testGetNotFound() {
        given(clientRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        clientService.get(1L);
    }

    @Test
    public void testUpdateSuccess() {

        Client client = new Client();
        client.setId(1L);
        client.setRiskProfile(Client.RiskProfile.LOW);

        given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

        clientService.update(client);

        Mockito.verify(clientRepository, times(1)).save(client);
    }

    @Test(expected = ClientNotFoundException.class)
    public void testUpdateClientNotFound() {
        given(clientRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        clientService.update(new Client());
    }

    @Test
    public void testDeleteSuccess() {

        long id = 1L;
        given(clientRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Client()));

        clientService.delete(id);

        Mockito.verify(clientRepository, times(1)).deleteById(id);
    }

    @Test(expected = ClientNotFoundException.class)
    public void testDeleteClientNotFound() {

        given(clientRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        clientService.delete(1L);
    }

    @Test
    public void mergeRiskProfilesSuccess() {

        //  arrange
        Client client = new Client();
        client.setId(1L);
        client.setRiskProfile(Client.RiskProfile.LOW);

        Set<Client> clients = Collections.singleton(client);

        given(clientRepository.findAllById(Collections.singleton(client.getId()))).willReturn(new ArrayList<>(clients));

        //  act
        Set<Client> res = clientService.mergeRiskProfiles(clients);

        //  assert
        Assert.assertEquals(1, res.size());
        Client resClient = res.iterator().next();
        Assert.assertEquals(client, resClient);
        Mockito.verify(clientRepository, times(1)).saveAll(Mockito.anyList());
    }

    @Test
    public void mergeRiskProfilesClientsNotUpdated() {

        long id = 1L;
        given(clientRepository.findAllById(Collections.singleton(id))).willReturn(Collections.emptyList());

        Set<Client> res = clientService.mergeRiskProfiles(Collections.singleton(new Client()));

        Assert.assertEquals(0, res.size());
        Mockito.verify(clientRepository, times(0)).saveAll(Mockito.anyList());
    }

}
