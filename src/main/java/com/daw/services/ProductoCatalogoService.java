package com.daw.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.HashMap;  // Importa HashMap

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistance.entities.ProductoCatalogo;
import com.daw.persistance.repository.CatalogoRepository;
import com.daw.persistance.repository.ProductoCatalogoRepository;
import com.daw.services.dto.CategoriaDto;
import com.daw.services.dto.ProductoCatalogoCreateDto;
import com.daw.services.dto.ProductoCatalogoDto;
import com.daw.services.dto.ProductoRemoteDto;
import com.daw.services.dto.SubCategoriaDto;
import com.daw.services.mapper.ProductoCatalogoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductoCatalogoService {

    @Autowired
    private ObjectMapper objectMapper;

    private final ProductClient productClient;
    private final ProductoCatalogoRepository repo;
    private final CatalogoRepository catalogoRepo;
    private final ProductoCatalogoMapper mapper;

    public ProductoCatalogoService(ProductClient productClient, ProductoCatalogoRepository repo,
            CatalogoRepository catalogoRepo, ProductoCatalogoMapper mapper) {
        this.productClient = productClient;
        this.repo = repo;
        this.catalogoRepo = catalogoRepo;
        this.mapper = mapper;
    }

    public List<ProductoCatalogoDto> getAll(String catalogoId) {
        if (!catalogoRepo.existsById(catalogoId)) {
            throw new EntityNotFoundException("Catálogo no encontrado: " + catalogoId);
        }

        // Solo accede a la base de datos local
        List<ProductoCatalogo> productosCatalogo = repo.findByCatalogoId(catalogoId);

        // Mapeo directo a DTO sin enriquecer con datos remotos
        return productosCatalogo.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }



    public ProductoCatalogoDto getById(String catalogoId, String productoId) {
        if (!catalogoRepo.existsById(catalogoId)) {
            throw new EntityNotFoundException("Catálogo no encontrado: " + catalogoId);
        }
        ProductoCatalogo entidad = repo.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto en catálogo no encontrado: " + productoId));
        // Corregido: Llama a la versión del método que SÍ usa el mapa.  Para un solo producto, no hay ganancia en usar el batch.
        ProductoRemoteDto remoto = productClient.getById(entidad.getProductoId());
        CategoriaDto cat = productClient.getCategoria(remoto.getCategoriaId());
        SubCategoriaDto sub = productClient.getSubcategoria(
                remoto.getCategoriaId(),
                remoto.getSubcategoriaId()
        );
        ProductoCatalogoDto dto = mapper.toDto(entidad);

        if (entidad.getIngredientesFlag() != null && entidad.getIngredientesFlag()) {
            dto.setIngredientesLista(remoto.getIngredientes());
        } else {
            dto.setIngredientesLista(Collections.emptyList());
        }
        dto.setCategoriaNombre(cat.getNombre());
        dto.setSubcategoriaNombre(sub.getNombre());

        return dto;

    }

    public ProductoCatalogoDto create(String catalogoId, ProductoCatalogoCreateDto dto) {
        var catalogo = catalogoRepo.findById(catalogoId)
                .orElseThrow(() -> new EntityNotFoundException("Catálogo no encontrado: " + catalogoId));

        System.out.println(">>> Buscando producto remoto...");
        ProductoRemoteDto remoto = productClient.getById(dto.getProductoId());
        System.out.println(">>> Producto obtenido: " + remoto);

        System.out.println(">>> Buscando categoría...");
        CategoriaDto cat = productClient.getCategoria(remoto.getCategoriaId());
        System.out.println(">>> Categoría obtenida: " + cat);

        System.out.println(">>> Buscando subcategoría...");
        SubCategoriaDto sub = productClient.getSubcategoria(remoto.getCategoriaId(), remoto.getSubcategoriaId());
        System.out.println(">>> Subcategoría obtenida: " + sub);

        ProductoCatalogo entidad = mapper.toEntity(dto);
        entidad.setId(UUID.randomUUID().toString());
        entidad.setCatalogo(catalogo);
        entidad.setCategoriaId(remoto.getCategoriaId());
        entidad.setCategoriaNombre(cat.getNombre());
        entidad.setSubcategoriaId(remoto.getSubcategoriaId());
        entidad.setSubcategoriaNombre(sub.getNombre());
        entidad.setCodigoBarras(remoto.getCodigoBarras());
        entidad.setUnidadDeVenta(remoto.getUnidadDeVenta());

        try {
            entidad.setCaracteristicas(
                    objectMapper.writeValueAsString(remoto.getCaracteristicas())
            );
        } catch (JsonProcessingException e) {
            entidad.setCaracteristicas("[]");
        }

        entidad.setIngredientesFlag(!remoto.getIngredientes().isEmpty());
        entidad.setCreatedAt(LocalDateTime.now());
        entidad.setUpdatedAt(LocalDateTime.now());

        // Verificación antes del save
        System.out.println(">>> CategoriaNombre en entidad: " + entidad.getCategoriaNombre());
        System.out.println(">>> SubcategoriaNombre en entidad: " + entidad.getSubcategoriaNombre());

        ProductoCatalogo guardado = repo.save(entidad);

        System.out.println(">>> ProductoCatalogo guardado con ID: " + guardado.getId());

        return mapper.toDto(guardado);
    }



    public ProductoCatalogoDto update(String catalogoId, String productoCatalogoId, ProductoCatalogoCreateDto dto) {

        if (!catalogoRepo.existsById(catalogoId)) {
            throw new EntityNotFoundException("Catálogo no encontrado: " + catalogoId);
        }

        ProductoCatalogo entidad = repo.findById(productoCatalogoId).orElseThrow(
                () -> new EntityNotFoundException("Producto en catálogo no encontrado: " + productoCatalogoId));

        // Solo si permites cambiar el producto
        ProductoRemoteDto remoto = productClient.getById(dto.getProductoId());
        CategoriaDto cat = productClient.getCategoria(remoto.getCategoriaId());
        SubCategoriaDto sub = productClient.getSubcategoria(remoto.getCategoriaId(), remoto.getSubcategoriaId());

        entidad.setProductoId(dto.getProductoId());
        entidad.setCategoriaId(remoto.getCategoriaId());
        entidad.setCategoriaNombre(cat.getNombre());
        entidad.setSubcategoriaId(remoto.getSubcategoriaId());
        entidad.setSubcategoriaNombre(sub.getNombre());
        entidad.setCodigoBarras(remoto.getCodigoBarras());

        entidad.setNombreComercial(dto.getNombreComercial());
        entidad.setPvp(dto.getPvp());
        entidad.setIva(dto.getIva());
        entidad.setUpdatedAt(LocalDateTime.now());

        ProductoCatalogo actualizado = repo.save(entidad);
        return mapper.toDto(actualizado);
    }


    // Método enriquecerYMapear modificado para aceptar el mapa de productos remotos
    private ProductoCatalogoDto enriquecerYMapearDos(ProductoCatalogo entidad, HashMap<String, ProductoRemoteDto> productoRemotoMap) {
        ProductoRemoteDto remoto = productoRemotoMap.get(entidad.getProductoId()); // Obtener del mapa
        CategoriaDto cat = productClient.getCategoria(remoto.getCategoriaId());
        SubCategoriaDto sub = productClient.getSubcategoria(
                remoto.getCategoriaId(),
                remoto.getSubcategoriaId()
        );
        ProductoCatalogoDto dto = mapper.toDto(entidad);

        if (entidad.getIngredientesFlag() != null && entidad.getIngredientesFlag()) {
            dto.setIngredientesLista(remoto.getIngredientes());
        } else {
            dto.setIngredientesLista(Collections.emptyList());
        }
        dto.setCategoriaNombre(cat.getNombre());
        dto.setSubcategoriaNombre(sub.getNombre());

        return dto;
    }

    private ProductoCatalogoDto enriquecerYMapear(ProductoCatalogo entidad) {
        ProductoRemoteDto remoto = productClient.getById(entidad.getProductoId());
        CategoriaDto cat = productClient.getCategoria(remoto.getCategoriaId());
        SubCategoriaDto sub = productClient.getSubcategoria(
                remoto.getCategoriaId(),
                remoto.getSubcategoriaId()
        );
        ProductoCatalogoDto dto = mapper.toDto(entidad);

        if (entidad.getIngredientesFlag() != null && entidad.getIngredientesFlag()) {
            dto.setIngredientesLista(remoto.getIngredientes());
        } else {
            dto.setIngredientesLista(Collections.emptyList());
        }
        dto.setCategoriaNombre(cat.getNombre());
        dto.setSubcategoriaNombre(sub.getNombre());

        return dto;
    }
    public void delete(String catalogoId, String productoCatalogoId) {
        if (!catalogoRepo.existsById(catalogoId)) {
            throw new EntityNotFoundException("Catálogo no encontrado: " + catalogoId);
        }

        ProductoCatalogo entidad = repo.findById(productoCatalogoId).orElseThrow(
                () -> new EntityNotFoundException("Producto en catálogo no encontrado: " + productoCatalogoId)
        );

        repo.delete(entidad);
    }

}


