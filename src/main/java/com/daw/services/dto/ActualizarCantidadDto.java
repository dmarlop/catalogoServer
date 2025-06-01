package com.daw.services.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarCantidadDto {
	private String productoCatalogoId;
    private BigDecimal nuevaCantidad;
}
