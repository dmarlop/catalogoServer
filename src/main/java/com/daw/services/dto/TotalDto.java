package com.daw.services.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalDto {
    private BigDecimal total;
    private BigDecimal base;
    private BigDecimal iva;
    public TotalDto(BigDecimal total, BigDecimal base, BigDecimal iva) {
        this.total = total;
        this.base  = base;
        this.iva   = iva;
    }
}
