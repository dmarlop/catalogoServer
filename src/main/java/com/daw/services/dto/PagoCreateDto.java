package com.daw.services.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoCreateDto {
    private String codPago;
    private BigDecimal cantidadEntregada;
    private BigDecimal cantidadAdeudada;
    private String estado;
    private String formaPago;
  
}
