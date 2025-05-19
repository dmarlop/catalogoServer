package com.daw.controllers;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistance.entities.LineaPedidoCatalogo;
import com.daw.services.LineaPedidoCatalogoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lineas-pedido")
@RequiredArgsConstructor
public class LineaPedidoCatalogoController {

    private final LineaPedidoCatalogoService lineaPedidoCatalogoService;

    // Crear una nueva línea de pedido
    @PostMapping
    public ResponseEntity<LineaPedidoCatalogo> crearLinea(@RequestBody LineaPedidoCatalogo linea) {
        LineaPedidoCatalogo nuevaLinea = lineaPedidoCatalogoService.crearLineaDePedido(linea);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaLinea);
    }


    @PutMapping("/{lineaId}")
    public ResponseEntity<LineaPedidoCatalogo> actualizarLinea(@PathVariable String lineaId, @RequestBody BigDecimal nuevaCantidad) {
        LineaPedidoCatalogo lineaActualizada = lineaPedidoCatalogoService.actualizarLineaDePedido(lineaId, nuevaCantidad);
        return ResponseEntity.ok(lineaActualizada);
    }


    @DeleteMapping("/{lineaId}")
    public ResponseEntity<Void> eliminarLinea(@PathVariable String lineaId) {
        lineaPedidoCatalogoService.eliminarLineaDePedido(lineaId);
        return ResponseEntity.noContent().build();
    }
}
