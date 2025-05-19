
package com.daw.controllers;

import com.daw.services.dto.CatalogoDto;
import com.daw.services.dto.CatalogoCreateDto;
import com.daw.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/catalogos")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public List<CatalogoDto> listAll() {
        return catalogoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogoDto> getById(@PathVariable String id) {
        CatalogoDto dto = catalogoService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CatalogoDto> create(
           @RequestBody CatalogoCreateDto createDto) {
        CatalogoDto dto = catalogoService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogoDto> update(
            @PathVariable String id,
             @RequestBody CatalogoCreateDto updateDto) {
        CatalogoDto dto = catalogoService.update(id, updateDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        catalogoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
