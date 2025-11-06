package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ProdutoRequestDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());
        produto.setDisponivel(true);

        return new ProdutoResponseDTO(produtoRepository.save(produto));
    }

    @Transactional(readOnly = true)
    public List<Produto> listarDisponiveis() {
        return produtoRepository.findByDisponivelTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setCategoria(produtoAtualizado.getCategoria());

        return produtoRepository.save(produto);
    }

    public void indisponibilizar(Long id) {
        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
        
        produto.indisponibilizar();
        produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    //private void validarDadosProduto(Produto produto) {
    //    if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
    //        throw new IllegalArgumentException("Nome é obrigatório");
    //    }
//
    //    if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
    //        throw new IllegalArgumentException("Preço deve ser maior que zero");
    //    }
//
    //    if (produto.getRestauranteId() == null) {
    //        throw new IllegalArgumentException("Restaurante é obrigatório");
    //    }
    //}
}