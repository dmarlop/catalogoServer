package com.daw.services.mapper;

import org.springframework.stereotype.Component;

import com.daw.persistance.entities.LineaPedidoCatalogo;
import com.daw.services.dto.PedidoProductoCatalogoCreateDto;
import com.daw.services.dto.PedidoProductoCatalogoDto;
@Component
public class LineaPedidoCatalogoMapper {

    public PedidoProductoCatalogoDto toDto(LineaPedidoCatalogo entity) {
        if (entity == null) {
            return null;
        }
        PedidoProductoCatalogoDto dto = new PedidoProductoCatalogoDto();
        dto.setId(entity.getId());
        dto.setCodProductoCatalogo(entity.getProductoCatalogo().getId());
        dto.setNombreProducto(entity.getProductoCatalogo().getNombreComercial());
        dto.setCantidadPedida(entity.getCantidadPedida());
        dto.setCantidadEntregada(entity.getCantidadEntregada());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        dto.setSubtotal(entity.getPrecioUnitario().multiply(entity.getCantidadPedida()));
        
        return dto;
    }
    
    
}