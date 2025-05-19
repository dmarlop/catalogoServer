package com.daw.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.daw.persistance.entities.LineaPedidoCatalogo;
import com.daw.persistance.repository.LineaPedidoCatalogoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LineaPedidoCatalogoService {

    private final LineaPedidoCatalogoRepository lineaPedidoCatalogoRepository;


    public LineaPedidoCatalogo crearLineaDePedido(LineaPedidoCatalogo linea) {
        return lineaPedidoCatalogoRepository.save(linea);
    }


    public LineaPedidoCatalogo actualizarLineaDePedido(String lineaId, BigDecimal nuevaCantidad) {
        LineaPedidoCatalogo linea = lineaPedidoCatalogoRepository.findById(lineaId)
            .orElseThrow(() -> new RuntimeException("Línea de pedido no encontrada"));
        linea.setCantidadPedida(nuevaCantidad);
        return lineaPedidoCatalogoRepository.save(linea);
    }


    public void eliminarLineaDePedido(String lineaId) {
        LineaPedidoCatalogo linea = lineaPedidoCatalogoRepository.findById(lineaId)
            .orElseThrow(() -> new RuntimeException("Línea de pedido no encontrada"));
        lineaPedidoCatalogoRepository.delete(linea);
    }
}