package com.daw.services.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.daw.persistance.entities.*;
import com.daw.services.dto.*;


import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
@Component
public class ProductoCatalogoMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ProductoCatalogo toEntity(ProductoCatalogoCreateDto dto) {
        if (dto == null) {
            return null;
        }
        ProductoCatalogo entity = new ProductoCatalogo();
        // entity.setId(...) → genera o asigna el ID en el service
        entity.setProductoId(dto.getProductoId());
        entity.setNombreComercial(dto.getNombreComercial());           
        entity.setPvp(dto.getPvp());
        entity.setIva(dto.getIva());
        return entity;
    }

    public ProductoCatalogoDto toDto(ProductoCatalogo entity) {
        if (entity == null) {
            return null;
        }
        ProductoCatalogoDto dto = new ProductoCatalogoDto();
        dto.setId(entity.getId());
        dto.setProductoId(entity.getProductoId());
        dto.setNombreComercial(entity.getNombreComercial());
        dto.setPvp(entity.getPvp());
        dto.setIva(entity.getIva());
        dto.setIngredientes(entity.getIngredientesFlag());
        dto.setCodigoBarras(entity.getCodigoBarras());

        // JSON → List<String>
        if (entity.getCaracteristicas() != null) {
            try {
                List<String> list = OBJECT_MAPPER.readValue(
                  entity.getCaracteristicas(),
                  new TypeReference<List<String>>() {}
                );
                dto.setCaracteristicas(list);
            } catch (Exception e) {
                dto.setCaracteristicas(Collections.emptyList());
            }
        } else {
            dto.setCaracteristicas(Collections.emptyList());
        }

        dto.setCategoriaId(entity.getCategoriaId());
        dto.setCategoriaNombre(entity.getCategoriaNombre());
        dto.setSubcategoriaId(entity.getSubcategoriaId());
        dto.setSubcategoriaNombre(entity.getSubcategoriaNombre());
        dto.setStartTime(entity.getCreatedAt());
        dto.setModifyTime(entity.getUpdatedAt());
        dto.setUnidadDeVenta(entity.getUnidadDeVenta());
        return dto;
    }
}
