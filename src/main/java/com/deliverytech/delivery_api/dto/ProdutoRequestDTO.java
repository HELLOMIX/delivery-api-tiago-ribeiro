package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProdutoRequestDTO {
    
    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;
    
    @NotBlank(message = "A descrição do produto é obrigatória")
    private String descricao;

    @NotBlank(message = "O preço do produto é obrigatório")
    private BigDecimal preco;

    @NotBlank(message = "A categoria do produto é obrigatório")
    private String categoria;

    @NotBlank(message = "A disponibilidade do produto é obrigatória")
    private Boolean disponivel;
    
}
