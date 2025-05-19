package com.daw.services.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoProductoCatalogoDto {
    private String id;
    private String codUsuario;
    private String codProductoCatalogo;
    private BigDecimal cantidadPedida;
    private BigDecimal cantidadEntregada;
    private String nombreProducto; // Nuevo campo
    private BigDecimal precioUnitario; // Nuevo campo
    private BigDecimal subtotal;
}
