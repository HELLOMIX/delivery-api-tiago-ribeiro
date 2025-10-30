package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Buscar produtos por restaurante (usa o campo restauranteId do model)
    List<Produto> findByRestauranteId(Long restauranteId);

    // Buscar por categoria
    List<Produto> findByCategoria(String categoria);

    // Produtos disponíveis
    List<Produto> findByDisponivelTrue();

    // Buscar por nome contendo (ignora maiúsculas/minúsculas)
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}