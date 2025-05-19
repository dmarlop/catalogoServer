package com.daw.services.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoProductoCatalogoCreateDto {
    private String codUsuario;
    private String codProductoCatalogo;
    private BigDecimal cantidadPedida;
    private BigDecimal cantidadEntregada;
}