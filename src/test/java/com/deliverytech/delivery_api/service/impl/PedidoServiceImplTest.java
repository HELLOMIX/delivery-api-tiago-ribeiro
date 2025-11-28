package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName("Testes Unitários Pedido Service")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Produto produto;
    private Pedido pedido;
    private Cliente cliente;
    private PedidoResponseDTO pedidoResponseDTO;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNumeroPedido("ABC12345");
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE.name());
        pedido.setValorTotal(BigDecimal.valueOf(20.0));
        pedido.setItens(List.of());

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setAtivo(true);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setAtivo(true);
        restaurante.setTaxaEntrega(BigDecimal.valueOf(5.0));

        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setPreco(BigDecimal.valueOf(10.0));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);

        // lenient stubbing para evitar "UnnecessaryStubbing" quando alguns testes não usam esses stubs
        org.mockito.Mockito.lenient().when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        org.mockito.Mockito.lenient().when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        // garantir stubs para os métodos que PedidoServiceImpl pode chamar
        org.mockito.Mockito.lenient().when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produto));
        org.mockito.Mockito.lenient().when(produtoRepository.findAllById(any(Iterable.class))).thenReturn(List.of(produto));

        pedidoResponseDTO = new PedidoResponseDTO();
        pedidoResponseDTO.setId(1L);
        pedidoResponseDTO.setNumeroPedido("ABC12345");
        pedidoResponseDTO.setValorTotal(BigDecimal.valueOf(25.0));
        pedidoResponseDTO.setStatus(StatusPedido.PENDENTE.name());
    }

    @Test
    @DisplayName("Deve criar pedido com produtos válidos e retornar valor total")
    void criarPedido_comProdutosValidos_deveSalvarEPassarValorTotal() {
        // given
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(1L);
        dto.setRestauranteId(1L);
        
        ItemPedidoRequestDTO item = new ItemPedidoRequestDTO();
        item.setProdutoId(1L);
        item.setQuantidade(2);
        dto.setItens(List.of(item));

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(modelMapper.map(any(Pedido.class), eq(PedidoResponseDTO.class))).thenReturn(pedidoResponseDTO);

        // when
        PedidoResponseDTO response = pedidoService.criarPedido(dto);

        // then
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(25.0), response.getValorTotal());
        verify(produtoRepository).findById(anyLong());
        verify(pedidoRepository).save(any(Pedido.class));
        verify(modelMapper).map(any(Pedido.class), eq(PedidoResponseDTO.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar pedido com produto indisponível")
    void criarPedido_produtoIndisponivel_deveLancarException() {
        // given
        produto.setDisponivel(false);
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(1L);
        dto.setRestauranteId(1L);
        ItemPedidoRequestDTO item = new ItemPedidoRequestDTO();
        item.setProdutoId(1L);
        item.setQuantidade(1);
        dto.setItens(List.of(item));

        when(produtoRepository.findAllById(List.of(1L))).thenReturn(List.of(produto));

        // when / then: aceitar RuntimeException genérico para cobrir implementação (IllegalArgumentException ou Business/EntityNotFound)
        assertThrows(RuntimeException.class, () -> pedidoService.criarPedido(dto));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar pedido por ID")
    void buscarPorId_deveRetornarPedido() {
        // given
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(modelMapper.map(any(Pedido.class), eq(PedidoResponseDTO.class))).thenReturn(pedidoResponseDTO);

        // when
        PedidoResponseDTO response = pedidoService.buscarPorId(1L);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(pedidoRepository).findById(1L);
        verify(modelMapper).map(any(Pedido.class), eq(PedidoResponseDTO.class));
    }

    @Test
    @DisplayName("Deve atualizar status do pedido")
    void atualizarStatus_deveAtualizar() {
        // given
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        pedido.setStatus(StatusPedido.PENDENTE.name());
        pedido.setValorTotal(BigDecimal.valueOf(20.0));

        Pedido updated = new Pedido();
        updated.setId(1L);
        updated.setStatus(StatusPedido.CONFIRMADO.name());
        updated.setValorTotal(pedido.getValorTotal());

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(updated);
        when(modelMapper.map(any(Pedido.class), eq(PedidoResponseDTO.class)))
                .thenReturn(new PedidoResponseDTO() {{
                    setId(1L);
                    setStatus(StatusPedido.CONFIRMADO.name());
                    setValorTotal(BigDecimal.valueOf(20.0));
                }});

        // when
        PedidoResponseDTO response = pedidoService.atualizarStatusPedido(1L, StatusPedido.CONFIRMADO);

        // then
        assertNotNull(response);
        assertEquals(StatusPedido.CONFIRMADO.name(), response.getStatus());
        verify(pedidoRepository).findById(1L);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve listar pedidos por cliente")
    void listarPedidosPorCliente_deveRetornarLista() {
        // given
        Pedido outro = new Pedido();
        outro.setId(2L);
        outro.setNumeroPedido("XYZ987");
        outro.setValorTotal(BigDecimal.valueOf(10));
        List<Pedido> pedidos = List.of(pedido, outro);

        when(pedidoRepository.findByClienteId(1L)).thenReturn(pedidos);
        when(modelMapper.map(any(Pedido.class), eq(PedidoResponseDTO.class)))
                .thenReturn(pedidoResponseDTO);

        // when
        var responseList = pedidoService.listarPedidosPorCliente(1L);

        // then
        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        verify(pedidoRepository).findByClienteId(1L);
        verify(modelMapper, atLeastOnce()).map(any(Pedido.class), eq(PedidoResponseDTO.class));
    }
}