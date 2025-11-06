package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestauranteRequestDTO {
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    @NotBlank(message = "O número de telefone é obrigatório")
    private String telefone;

    @NotBlank(message = "A taxa de entrega é obrigatória")
    private BigDecimal taxaEntrega;

}
