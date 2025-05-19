package com.daw.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.persistance.entities.ProductoCatalogo;

public interface ProductoCatalogoRepository extends JpaRepository<ProductoCatalogo, String> {
	List<ProductoCatalogo> findByCatalogoId(String catalogoId);

}
