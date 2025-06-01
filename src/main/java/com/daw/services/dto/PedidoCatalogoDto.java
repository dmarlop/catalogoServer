package com.daw.services.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PedidoCatalogoDto {
    private String id;
    private String codUsuario;
    private String estado;
    private String direccion;
    private LocalDateTime fecha;
    private TotalDto totalPedidoBase;
    private TotalDto totalPedido;
    private TotalDto totalEntrega;
    private List<PedidoProductoCatalogoDto> productos;
    private List<PagoDto> pagos;
    private String nombreUsuario;
    // getters y setters…
}
