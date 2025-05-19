package com.daw.services.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoriaCreateDto {
    private String codCategoria;
    private String nombre;
    private Integer orden;
    // getters y setters…
}
