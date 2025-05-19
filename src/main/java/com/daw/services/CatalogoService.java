// src/main/java/com/daw/services/service/CatalogoService.java
package com.daw.services;

import com.daw.persistance.entities.*;
import com.daw.persistance.repository.*;
import com.daw.services.dto.CatalogoDto;
import com.daw.services.dto.CatalogoCreateDto;
import com.daw.services.mapper.CatalogoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CatalogoService {
	
	@Autowired
    private CatalogoRepository catalogoRepository;
	
	@Autowired
    private CatalogoMapper catalogoMapper;

    

    public List<CatalogoDto> findAll() {
        return catalogoRepository.findAll()
            .stream()
            .map(catalogoMapper::toDto)
            .collect(Collectors.toList());
    }

    public CatalogoDto findById(String id) {
        return catalogoRepository.findById(id)
            .map(catalogoMapper::toDto)
            .orElseThrow(() -> new RuntimeException("Catálogo no encontrado: " + id));
    }

    public CatalogoDto create(CatalogoCreateDto dto) {
        Catalogo entity = catalogoMapper.toEntity(dto);
        entity.setId(UUID.randomUUID().toString());
        entity.setFecha(LocalDate.now());
        entity.setCreatedAt(LocalDateTime.now());
        Catalogo saved = catalogoRepository.save(entity);
        return catalogoMapper.toDto(saved);
    }

    public CatalogoDto update(String id, CatalogoCreateDto dto) {
        Catalogo entity = catalogoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Catálogo no encontrado: " + id));
        entity.setNombre(dto.getNombre());
        entity.setStatus(dto.getStatus());
        entity.setFecha(LocalDate.now());
        
        Catalogo updated = catalogoRepository.save(entity);
        return catalogoMapper.toDto(updated);
    }

    public void delete(String id) {
        if (!catalogoRepository.existsById(id)) {
            throw new RuntimeException("Catálogo no encontrado: " + id);
        }
        catalogoRepository.deleteById(id);
    }
}
