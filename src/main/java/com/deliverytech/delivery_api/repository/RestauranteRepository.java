package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    // Buscar por nome contendo (ignora maiúsculas/minúsculas)
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    // Buscar por categoria exata
    List<Restaurante> findByCategoria(String categoria);

    // Restaurantes ativos (soft delete)
    List<Restaurante> findByAtivoTrue();

    // Buscar por avaliação exata (opcional)
    List<Restaurante> findByAvaliacao(BigDecimal avaliacao);
}