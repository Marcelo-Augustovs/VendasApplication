package org.example.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.validation.NotEmptyList;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    @NotNull(message = "Informe o Código do cliente.")
    private Integer cliente;

    @NotNull(message = "Campo total do Pedido é obrigatório.")
    private BigDecimal total;

    @NotEmptyList(message = "pedido não pode ser realizado sem itens.")
    private List<ItemPedidoDTO> items;

}
