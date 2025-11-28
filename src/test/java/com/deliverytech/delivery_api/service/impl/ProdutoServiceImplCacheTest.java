package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery_api.dto.response.ProdutoResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableCaching
@ActiveProfiles("test")
@DisplayName("Testes de Cache - ProdutoService")
class ProdutoServiceImplCacheTest {

    @MockBean
    private ProdutoRepository produtoRepository;

    @MockBean
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoServiceImpl produtoService;

    private Produto produto;

    @BeforeEach
    void setup() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setPreco(BigDecimal.valueOf(50.0));
        produto.setDisponivel(true);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));
    }

    @Test
    @DisplayName("Cache deve evitar consulta duplicada ao buscar por restaurante")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void cacheEvitarConsultaDuplicada() {
        Long restauranteId = 1L;

        when(produtoRepository.findByRestauranteId(anyLong())).thenReturn(List.of(produto));

        // 1a chamada -> busca no repo
        List<ProdutoResponseDTO> resultado1 = produtoService.buscarPorRestaurante(restauranteId);
        assertNotNull(resultado1);
        assertFalse(resultado1.isEmpty());

        // 2a chamada -> deve vir do cache (não chama repo novamente)
        List<ProdutoResponseDTO> resultado2 = produtoService.buscarPorRestaurante(restauranteId);
        assertNotNull(resultado2);

        // Repositório foi chamado apenas 1 vez
        verify(produtoRepository, times(1)).findByRestauranteId(anyLong());
    }

    @Test
    @DisplayName("Cache deve ser invalidado ao criar novo produto")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void cacheInvalidadoAoCadastrar() {
        Long restauranteId = 1L;

        when(produtoRepository.findByRestauranteId(anyLong())).thenReturn(List.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // 1a busca -> carrega no cache
        produtoService.buscarPorRestaurante(restauranteId);
        verify(produtoRepository, times(1)).findByRestauranteId(anyLong());

        // Cadastra novo produto válido (invalida cache)
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Novo Produto");
        dto.setDescricao("Desc");
        dto.setPreco(BigDecimal.valueOf(10.0));
        dto.setCategoria("Cat");
        dto.setDisponivel(true);
        dto.setRestauranteId(restauranteId);

        produtoService.cadastrar(dto);

        // 2a busca -> deve chamar repo novamente (cache foi invalidado)
        produtoService.buscarPorRestaurante(restauranteId);
        verify(produtoRepository, atLeast(2)).findByRestauranteId(anyLong());
    }

    @Test
    @DisplayName("Cache deve evitar consulta ao listar produtos disponíveis")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void cacheListarDisponiveApenasUmaVez() {
        produto.setDisponivel(true);
        when(produtoRepository.findByDisponivelTrue()).thenReturn(List.of(produto));

        // 1a chamada
        produtoService.listarDisponiveis();
        // 2a chamada
        produtoService.listarDisponiveis();

        // Repositório foi chamado apenas 1 vez
        verify(produtoRepository, times(1)).findByDisponivelTrue();
    }
}