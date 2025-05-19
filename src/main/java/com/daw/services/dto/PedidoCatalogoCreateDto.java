package com.daw.services.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoCatalogoCreateDto {
    private String codUsuario;
    private LocalDateTime fecha;
    private String estado;
    private String catalogoId;
}