package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RestauranteService {
    @Autowired
    private RestauranteRepository restauranteRepository;

    public Restaurante cadastrar(Restaurante restaurante) {
        validarDadosRestaurante(restaurante);
        restaurante.setAtivo(true);
        return restauranteRepository.save(restaurante);
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

    private void validarDadosRestaurante(Restaurante restaurante) {
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (restaurante.getCategoria() == null || restaurante.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }

        if (restaurante.getTaxaEntrega() == null || restaurante.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa de entrega deve ser maior ou igual a zero");
        }
    }
}