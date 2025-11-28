package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.config.IntegrationTest;
import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PedidoControllerIT extends IntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;

    @BeforeEach
    void setupData() {
        produtoRepository.deleteAll();
        clienteRepository.deleteAll();
        restauranteRepository.deleteAll();

        cliente = new Cliente();
        cliente.setNome("Cliente X");
        cliente.setEmail("x@t.com");
        clienteRepository.save(cliente);

        restaurante = new Restaurante();
        restaurante.setNome("Resto");
        restaurante.setTaxaEntrega(BigDecimal.valueOf(3.0));
        restauranteRepository.save(restaurante);

        produto = new Produto();
        produto.setNome("Produto A");
        produto.setPreco(BigDecimal.valueOf(10.0));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        produtoRepository.save(produto);
    }

    @Test
    void postPedido_comDadosValidos_deveCriarRetornar201() throws Exception {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(cliente.getId());
        dto.setRestauranteId(restaurante.getId());
        ItemPedidoRequestDTO item = new ItemPedidoRequestDTO();
        item.setProdutoId(produto.getId());
        item.setQuantidade(2);
        dto.setItens(List.of(item));

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.valorTotal").value(23.0)); // 10*2 + 3 taxa
    }

    @Test
    void postPedido_produtoInexistente_deveRetornar4xx() throws Exception {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(cliente.getId());
        dto.setRestauranteId(restaurante.getId());
        ItemPedidoRequestDTO item = new ItemPedidoRequestDTO();
        item.setProdutoId(99999L);
        item.setQuantidade(1);
        dto.setItens(List.of(item));

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().is4xxClientError());
    }
}