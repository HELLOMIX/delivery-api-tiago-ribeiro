package com.deliverytech.delivery_api.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery_api.dto.response.ProdutoResponseDTO;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.ProdutoService;

@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService{
    
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {

        Produto produto = modelMapper.map(dto, Produto.class);

        produto.setRestaurante(restauranteRepository.findById(dto.getRestauranteId()).get());

        Produto produtoSalvo = produtoRepository.save(produto);

        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado de ID: " + id));
        
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado de ID: " + id));

        Optional<Restaurante> restaurante = restauranteRepository.findById(dto.getRestauranteId());
        // Validar dados do produto
        if (dto.getNome() == null || dto.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (dto.getDescricao() == null || dto.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("Descrição do produto é obrigatória");
        }
        if (dto.getPreco() == null || dto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que zero");
        }
        if (dto.getCategoria() == null || dto.getCategoria().isEmpty()) {
            throw new IllegalArgumentException("Categoria do produto é obrigatória");
        }
        // Atualizar dados do produto
        produtoExistente.setNome(dto.getNome());
        produtoExistente.setDescricao(dto.getDescricao());
        produtoExistente.setPreco(dto.getPreco());
        produtoExistente.setCategoria(dto.getCategoria());
        produtoExistente.setDisponivel(dto.getDisponivel());
        produtoExistente.setRestaurante(restaurante.get());

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);

        return modelMapper.map(produtoAtualizado, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO ativarDesativarProduto(Long id) {
        // Buscar produto existente
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
        // Inverter disponibilidade do produto
        produto.setDisponivel(!produto.getDisponivel());
        // Salvar produto atualizado
        Produto produtoAtualizado = produtoRepository.save(produto);
        // Retornar DTO de resposta
        return modelMapper.map(produtoAtualizado, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO buscarPorNome(String nome) {
        // Buscar produto por nome
        Produto produto = produtoRepository.findByNome(nome);
        if(!produto.getDisponivel()){
            throw new BusinessException ("Produto indisponível: " + nome);
        }
        // Converter entidade para DTO
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    public List<ProdutoResponseDTO> buscarPorRestaurante(Long restauranteId) {
        // Buscar produtos por restaurante ID
        List<Produto> produtos = produtoRepository.findByRestauranteId(restauranteId);
        if (produtos.isEmpty() || produtos.stream().noneMatch(Produto::getDisponivel)) {
            throw new BusinessException("Nenhum produto encontrado para o restaurante ID: " + restauranteId);
        }
        // Converter lista de entidades para lista de DTOs
        return produtos.stream()
                .filter(Produto::getDisponivel) // Filtrar apenas produtos disponíveis
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> buscarPorCategoria(String categoria) {

        List<Produto> produtos = produtoRepository.findByCategoria(categoria);
        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado para a categoria: " + categoria);
        }

        return produtos.stream()
                .map(c -> modelMapper.map(c, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        // Buscar produtos por faixa de preço
        List<Produto> produtos = produtoRepository.findByPrecoLessThanEqual(precoMaximo);
        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado na faixa de preço: " + precoMinimo + " a " + precoMaximo);
        }
        // Converter lista de entidades para lista de DTOs
        return produtos.stream()
                .filter(produto -> produto.getPreco().compareTo(precoMinimo) >= 0)
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> buscarTodosProdutos() {
        // Buscar todos os produtos
        List<Produto> produtos = produtoRepository.findAll();
        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado");
        }
        // Converter lista de entidades para lista de DTOs
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> buscarPorPrecoMenorOuIgual(BigDecimal valor) {
        // Buscar produtos com preço menor ou igual ao valor especificado
        List<Produto> produtos = produtoRepository.findByPrecoLessThanEqual(valor);
        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado com preço menor ou igual a: " + valor);
        }
        // Converter lista de entidades para lista de DTOs
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .toList();
    }

}
