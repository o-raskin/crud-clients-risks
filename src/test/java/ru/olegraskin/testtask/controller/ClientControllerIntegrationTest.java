package ru.olegraskin.testtask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.olegraskin.testtask.TestTaskApplication;
import ru.olegraskin.testtask.domain.Client;
import ru.olegraskin.testtask.dto.ClientDTO;
import ru.olegraskin.testtask.repository.ClientRepository;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestTaskApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "integration-tests")
@TestPropertySource(locations = "classpath:application-integration-tests.yaml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientRepository repository;

    @Test
    public void testGetClient200() throws Exception {

        Client client = new Client();
        client.setId(1L);
        client.setRiskProfile(Client.RiskProfile.LOW);

        addToRepository(client);

        mvc.perform(MockMvcRequestBuilders.get("/clients/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(client.getId().intValue())))
                .andExpect(jsonPath("riskProfile", is(client.getRiskProfile().name())));
    }

    @Test
    public void testGetClient400() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/clients/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetClients200() throws Exception {

        Client client = new Client();
        client.setId(1L);
        client.setRiskProfile(Client.RiskProfile.LOW);

        addToRepository(client);

        mvc.perform(MockMvcRequestBuilders.get("/clients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(client.getId().intValue())))
                .andExpect(jsonPath("$[0].riskProfile", is(client.getRiskProfile().name())));
    }

    @Test
    public void testGetClients200EmptyArray() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/clients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testCreate200() throws Exception {

        int generatedId = 1;

        ClientDTO dto = new ClientDTO();
        dto.setRiskProfile(ClientDTO.RiskProfile.LOW);

        mvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON).content(toJSON(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(generatedId)))
                .andExpect(jsonPath("riskProfile", is(dto.getRiskProfile().name())));
    }

    @Test
    public void testCreate400() throws Exception {

        ClientDTO dto = new ClientDTO();

        mvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON).content(toJSON(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate200() throws Exception {

        Client client = new Client();
        client.setId(1L);
        client.setRiskProfile(Client.RiskProfile.HIGH);

        addToRepository(client);

        int clientId = 1;
        ClientDTO dto = new ClientDTO();
        dto.setRiskProfile(ClientDTO.RiskProfile.LOW);

        mvc.perform(MockMvcRequestBuilders.put("/clients/{id}", clientId)
                .contentType(MediaType.APPLICATION_JSON).content(toJSON(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(clientId)))
                .andExpect(jsonPath("riskProfile", is(dto.getRiskProfile().name())));
    }

    @Test
    public void testUpdate400() throws Exception {

        ClientDTO dto = new ClientDTO();

        int clientId = 1;

        mvc.perform(MockMvcRequestBuilders.put("/clients/{id}", clientId)
                .contentType(MediaType.APPLICATION_JSON).content(toJSON(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete200() throws Exception {

        Client client = new Client();
        client.setId(1L);
        client.setRiskProfile(Client.RiskProfile.HIGH);

        addToRepository(client);

        int clientId = 1;

        mvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", clientId))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete404() throws Exception {

        Client client = new Client();
        client.setId(1L);
        client.setRiskProfile(Client.RiskProfile.HIGH);

        addToRepository(client);

        int clientId = 3;

        mvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", clientId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMergeRiskProfile200() throws Exception {

        Client client = new Client();
        client.setId(1L);
        client.setRiskProfile(Client.RiskProfile.HIGH);

        Client client2 = new Client();
        client2.setId(2L);
        client2.setRiskProfile(Client.RiskProfile.LOW);

        addToRepository(client);
        addToRepository(client2);

        mvc.perform(MockMvcRequestBuilders.post("/clients/merge")
                .contentType(MediaType.APPLICATION_JSON).content(toJSON(Arrays.asList(client, client2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id", is(client.getId().intValue())))
                .andExpect(jsonPath("$[1].riskProfile", is(client.getRiskProfile().name())))
                .andExpect(jsonPath("$[0].id", is(client2.getId().intValue())))
                .andExpect(jsonPath("$[0].riskProfile", is(client.getRiskProfile().name())));
    }

    private void addToRepository(Client client) {
        repository.save(client);
    }

    public static String toJSON(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
