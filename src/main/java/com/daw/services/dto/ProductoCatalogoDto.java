package com.daw.services.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoCatalogoDto {
    private String id;
    private String productoId;
    private String nombreComercial;
    private BigDecimal pvp;
    private BigDecimal iva;
    private boolean ingredientes;
    private List<String> ingredientesLista;
    private boolean fotografias;
    private String codigoBarras;
    private List<String> caracteristicas;
    private String categoriaId;
    private String subcategoriaId;
    private String categoriaNombre;
    private String subcategoriaNombre;
    private LocalDateTime startTime;
    private LocalDateTime modifyTime;
    private List<String> imagenes;
    // getters y setters…
}
