package com.daw.services.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.daw.persistance.enums.CatalogoStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatalogoDto {
    private String id;
    private String nombre;
    private CatalogoStatus status;
    private LocalDate fecha;
    private LocalDateTime startTime;
    private LocalDateTime modifyTime;
    
}
