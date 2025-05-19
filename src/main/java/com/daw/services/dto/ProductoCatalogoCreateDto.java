package com.daw.services.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoCatalogoCreateDto {
    private String productoId;        
    private String nombreComercial;   
    private BigDecimal coste;         
    private BigDecimal pvp;          
    private BigDecimal iva;          
   
}
