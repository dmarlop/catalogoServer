package com.daw.services.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRemoteDto {
    private String id;
    private String codProducto;
    private String nombre;
    private String nombreComercial;
    private String variedad;
    private String codigoBarras;
    private String categoriaId;
    private String subcategoriaId;
    private List<String> ingredientes;
    private List<String> imagenes;
    private List<String> caracteristicas;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // getters y setters...
}
