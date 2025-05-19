package com.daw.services.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoCompletoCreateDto {
    private String codUsuario;
    private String catalogoId;
    private List<LineaPedidoCreateDto> lineas;
    private String codDireccion; 
    @Getter
    @Setter
    public static class LineaPedidoCreateDto {
        private String codProductoCatalogo;
        private BigDecimal cantidadPedida;
    }
}
