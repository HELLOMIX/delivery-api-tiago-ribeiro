package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.dto.RestauranteRequestDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestauranteService {
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {

        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega());
        restaurante.setAtivo(true);

        return new RestauranteResponseDTO(restauranteRepository.save(restaurante));
    }

    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        restaurante.setNome(restauranteAtualizado.getNome());
        restaurante.setCategoria(restauranteAtualizado.getCategoria());
        restaurante.setEndereco(restauranteAtualizado.getEndereco());
        restaurante.setTelefone(restauranteAtualizado.getTelefone());
        restaurante.setTaxaEntrega(restauranteAtualizado.getTaxaEntrega());
        restaurante.setAvaliacao(restauranteAtualizado.getAvaliacao());

        return restauranteRepository.save(restaurante);
    }

    public void inativar(Long id) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));
        
        restaurante.inativar();
        restauranteRepository.save(restaurante);
    }

    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorProduto(String produto) {
        return restauranteRepository.findByProdutoNomeContaining(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarProdutosDoRestaurante(Long restauranteId) {
        // valida se o restaurante existe
        restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + restauranteId));

        return produtoRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    //private void validarDadosRestaurante(Restaurante restaurante) {
    //    if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
    //        throw new IllegalArgumentException("Nome é obrigatório");
    //    }
//
    //    if (restaurante.getCategoria() == null || restaurante.getCategoria().trim().isEmpty()) {
    //        throw new IllegalArgumentException("Categoria é obrigatória");
    //    }
//
    //    if (restaurante.getTaxaEntrega() == null || restaurante.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
    //        throw new IllegalArgumentException("Taxa de entrega deve ser maior ou igual a zero");
    //    }
    //}
}