package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.config.IntegrationTest;
import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClienteControllerIT extends IntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setupData() {
        clienteRepository.deleteAll();
        Cliente c = new Cliente();
        c.setNome("Joao Teste");
        c.setEmail("joao@teste.com");
        c.setTelefone("99999999");
        clienteRepository.save(c);
    }

    @Test
    void postCliente_valido_deveCriarRetornar201() throws Exception {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNome("Maria");
        dto.setEmail("maria@teste.com");
        dto.setTelefone("88888888");

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.nome").value("Maria"))
            .andExpect(jsonPath("$.email").value("maria@teste.com"));
    }

    @Test
    void getCliente_existente_retornar200() throws Exception {
        Cliente saved = clienteRepository.findAll().get(0);
        mockMvc.perform(get("/api/clientes/{id}", saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(saved.getId()))
            .andExpect(jsonPath("$.email", is(saved.getEmail())));
    }

    @Test
    void getCliente_naoExiste_retornar404() throws Exception {
        mockMvc.perform(get("/api/clientes/{id}", 9999L))
            .andExpect(status().isNotFound());
    }

    @Test
    void listarClientes_comPaginacao_retornarPage() throws Exception {
        mockMvc.perform(get("/api/clientes")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void putCliente_invalido_retornar400() throws Exception {
        ClienteRequestDTO dto = new ClienteRequestDTO(); // sem campos obrigatórios
        mockMvc.perform(put("/api/clientes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }
}