package com.daw.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistance.entities.PedidoCatalogo;
import com.daw.persistance.enums.PedidoCatalogoEstado;
import com.daw.services.PedidoCatalogoService;
import com.daw.services.dto.PedidoCatalogoCreateDto;
import com.daw.services.dto.PedidoCatalogoDto;
import com.daw.services.dto.PedidoCompletoCreateDto;
import com.daw.services.dto.PedidoProductoCatalogoDto;
import com.daw.services.mapper.PedidoCatalogoMapper;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoCatalogoController {
	
	@Autowired
    private PedidoCatalogoService pedidoCatalogoService;
	
	@Autowired
	private PedidoCatalogoMapper mapper;
    
	 @GetMapping
	    public ResponseEntity<List<PedidoCatalogo>> obtenerPedidosConLineas(
	            @RequestParam(required = false) Set<String> estados) {
	        
	       
	        Set<String> estadosFiltro = estados != null ? estados : Set.of("Abierto", "Montando", "Enviado");
	        
	        List<PedidoCatalogo> pedidos = pedidoCatalogoService.getAll();
	        return ResponseEntity.ok(pedidos);
	    }
	

    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoCatalogoDto> obtenerPedido(@PathVariable String pedidoId) {
        PedidoCatalogo pedido = pedidoCatalogoService.obtenerPedidoPorId(pedidoId);
        PedidoCatalogoDto dto = this.mapper.toDto(pedido);
        return ResponseEntity.ok(dto);
    }
   

    @PutMapping("/{pedidoId}/estado")
    public ResponseEntity<PedidoCatalogo> actualizarEstadoPedido(
        @PathVariable String pedidoId, @RequestBody String nuevoEstado) {
        PedidoCatalogo pedido = pedidoCatalogoService.actualizarEstadoPedido(pedidoId, nuevoEstado);
        return ResponseEntity.ok(pedido);
    }
    @GetMapping("/estado/{estado}")
    public List<PedidoCatalogoDto> obtenerPedidosPorEstado(@PathVariable PedidoCatalogoEstado estado) {
        return pedidoCatalogoService.obtenerPedidosPorEstados(estado);
    }

    @PostMapping("/{pedidoId}/productos")
    public ResponseEntity<PedidoCatalogoDto> agregarProductoALinea(
        @PathVariable String pedidoId, @RequestBody PedidoProductoCatalogoDto productoDto) {
        PedidoCatalogoDto pedido = pedidoCatalogoService.agregarProductoALinea(pedidoId, productoDto);
        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable String pedidoId) {
        pedidoCatalogoService.eliminarPedido(pedidoId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/catalogo/{catalogoId}")
    public ResponseEntity<List<PedidoCatalogoDto>> obtenerPorCatalogo(@PathVariable String catalogoId) {
        List<PedidoCatalogoDto> pedidos = pedidoCatalogoService.obtenerPorCatalogo(catalogoId);
        return ResponseEntity.ok(pedidos);
    }
    
   
    
    @GetMapping("/usuario/{codUsuario}")
    public ResponseEntity<List<PedidoCatalogo>> obtenerPedidosPorUsuario(@PathVariable String codUsuario) {
        List<PedidoCatalogo> pedidos = pedidoCatalogoService.obtenerPedidosPorUsuario(codUsuario);
        return ResponseEntity.ok(pedidos);
    }
    
    private final PedidoCatalogoService pedidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoCatalogoDto crearPedidoCompleto(@RequestBody PedidoCompletoCreateDto createDto) {
        return pedidoService.crearPedidoCompleto(createDto);
    }

    
    @GetMapping("/verificar-estado")
    public ResponseEntity<Map<String, Boolean>> verificarPedidoEnProgreso(
            @RequestParam String usuarioId,
            @RequestParam String catalogoId) {
        
        boolean tienePedidoAbierto = pedidoService.tieneUsuarioPedidoAbierto(usuarioId, catalogoId);
        return ResponseEntity.ok(Collections.singletonMap("tienePedidoAbierto", tienePedidoAbierto));
    }

   
}

