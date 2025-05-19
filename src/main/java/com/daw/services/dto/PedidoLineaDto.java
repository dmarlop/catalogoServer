package com.daw.services.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoLineaDto {
    private String id;
    private String codProductoCatalogo;
    private int cantidadPedida;
    private String nombreComercial;
    private double pvp;
}
