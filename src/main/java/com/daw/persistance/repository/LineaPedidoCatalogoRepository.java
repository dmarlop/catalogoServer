package com.daw.persistance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.persistance.entities.LineaPedidoCatalogo;
import com.daw.persistance.entities.PedidoCatalogo;
import com.daw.persistance.entities.ProductoCatalogo;

public interface LineaPedidoCatalogoRepository extends JpaRepository<LineaPedidoCatalogo, String> {
	 Optional<LineaPedidoCatalogo> findByPedidoAndProductoCatalogo(PedidoCatalogo pedido, ProductoCatalogo productoCatalogo);
	    List<LineaPedidoCatalogo> findByPedidoId(String pedidoId);
}
