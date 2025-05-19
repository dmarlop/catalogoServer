package com.daw.services.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoDto {
    private String id;
    private String codPago;
    private BigDecimal cantidadEntregada;
    private BigDecimal cantidadAdeudada;
    private String estado;
    private String formaPago;
    
}
