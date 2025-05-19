package com.daw.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.persistance.entities.Catalogo;

public interface CatalogoRepository extends JpaRepository<Catalogo, String> {

}
