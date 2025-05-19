package com.daw.services.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.daw.persistance.entities.Catalogo;
import com.daw.services.dto.CatalogoCreateDto;
import com.daw.services.dto.CatalogoDto;

@Component
public class CatalogoMapper {

    public Catalogo toEntity(CatalogoCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Catalogo entity = new Catalogo();
        // entity.setId(...) → genera o asigna el ID en el service
        entity.setNombre(dto.getNombre());
        entity.setStatus(dto.getStatus());
        entity.setFecha(LocalDate.now());
        return entity;
    }

    public CatalogoDto toDto(Catalogo entity) {
        if (entity == null) {
            return null;
        }
        CatalogoDto dto = new CatalogoDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setStatus(entity.getStatus());
        dto.setFecha(entity.getFecha());
        dto.setStartTime(entity.getCreatedAt());
        dto.setModifyTime(entity.getUpdatedAt());
        return dto;
    }
}
