package com.daw.persistance.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.persistance.entities.PedidoCatalogo;
import com.daw.persistance.enums.PedidoCatalogoEstado;
import com.daw.services.dto.PedidoCatalogoDto;

@Repository
public interface PedidoCatalogoRepository extends JpaRepository<PedidoCatalogo, String> {

    // 1. Buscar pedidos por usuario
    List<PedidoCatalogo> findByCodUsuario(String codUsuario);

    // 2. Buscar pedidos por estado (versión corregida)
    List<PedidoCatalogo> findByEstado(PedidoCatalogoEstado estado);

    // 3. Buscar pedidos por múltiples estados (versión corregida)
    List<PedidoCatalogo> findByEstadoIn(Set<PedidoCatalogoEstado> estados);
    
    List<PedidoCatalogo> findByCatalogoId(String id);
    // 4. Consulta optimizada para obtener pedidos con líneas y productos
    @Query("SELECT DISTINCT p FROM PedidoCatalogo p " +
           "LEFT JOIN FETCH p.lineas l " +
           "LEFT JOIN FETCH l.productoCatalogo " +
           "WHERE p.estado IN :estados")
    List<PedidoCatalogo> findByEstadoInWithLineasAndProductos(@Param("estados") Set<PedidoCatalogoEstado> estados);

    // 5. Buscar pedido específico por usuario, catálogo y estado
    List<PedidoCatalogo> findByCodUsuarioAndCatalogoIdAndEstado(
            String codUsuario,
            String catalogoId,
            PedidoCatalogoEstado estado);

    // 6. Verificar existencia de pedido abierto (nuevo método recomendado)
    boolean existsByCodUsuarioAndCatalogoIdAndEstado(
            String codUsuario,
            String catalogoId,
            PedidoCatalogoEstado estado);
}
