package com.daw.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.persistance.entities.PagoPedido;

public interface PagoPedidoRepository extends JpaRepository<PagoPedido, String> {
    List<PagoPedido> findByPedidoId(String pedidoId);
}
