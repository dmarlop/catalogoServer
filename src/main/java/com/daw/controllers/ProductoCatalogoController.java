package com.daw.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.daw.services.ProductoCatalogoService;
import com.daw.services.dto.ProductoCatalogoCreateDto;
import com.daw.services.dto.ProductoCatalogoDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/catalogos/{catalogoId}/productos")
@RequiredArgsConstructor
public class ProductoCatalogoController {

    private final ProductoCatalogoService service;

    @GetMapping
    public ResponseEntity<List<ProductoCatalogoDto>> getAll(
            @PathVariable String catalogoId) {
        List<ProductoCatalogoDto> list = service.getAll(catalogoId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{productoCatalogoId}")
    public ResponseEntity<ProductoCatalogoDto> getById(
            @PathVariable String catalogoId,
            @PathVariable String productoCatalogoId) {
        ProductoCatalogoDto dto = service.getById(catalogoId, productoCatalogoId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ProductoCatalogoDto> create(
            @PathVariable String catalogoId,
            @RequestBody ProductoCatalogoCreateDto createDto) {
        ProductoCatalogoDto created = service.create(catalogoId, createDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{productoCatalogoId}")
    public ResponseEntity<ProductoCatalogoDto> update(
            @PathVariable String catalogoId,
            @PathVariable String productoCatalogoId,
            @RequestBody ProductoCatalogoCreateDto updateDto) {
        ProductoCatalogoDto updated = service.update(catalogoId, productoCatalogoId, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{productoCatalogoId}")
    public ResponseEntity<Void> delete(
            @PathVariable String catalogoId,
            @PathVariable String productoCatalogoId) {
        service.delete(catalogoId, productoCatalogoId);
        return ResponseEntity.noContent().build();
    }
    
    
}
