package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {
    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Validated @RequestBody Restaurante restaurante) {
        try {
            Restaurante restauranteSalvo = restauranteService.cadastrar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restauranteSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<Restaurante>> listar() {
        List<Restaurante> restaurantes = restauranteService.listarAtivos();
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Restaurante> restaurante = restauranteService.buscarPorId(id);
        return restaurante.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                     @Validated @RequestBody Restaurante restaurante) {
        try {
            Restaurante restauranteAtualizado = restauranteService.atualizar(id, restaurante);
            return ResponseEntity.ok(restauranteAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            restauranteService.inativar(id);
            return ResponseEntity.ok().body("Restaurante inativado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Restaurante>> buscarPorCategoria(@PathVariable String categoria) {
        List<Restaurante> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(restaurantes);
    }
}