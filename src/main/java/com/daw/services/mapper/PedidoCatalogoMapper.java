package com.daw.services.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Component;

import com.daw.persistance.entities.Catalogo;
import com.daw.persistance.entities.PedidoCatalogo;
import com.daw.persistance.enums.PedidoCatalogoEstado;
import com.daw.services.dto.PagoDto;
import com.daw.services.dto.PedidoCatalogoCreateDto;
import com.daw.services.dto.PedidoCatalogoDto;
import com.daw.services.dto.PedidoProductoCatalogoDto;
import com.daw.services.dto.TotalDto;

@Component
public class PedidoCatalogoMapper {

    private final LineaPedidoCatalogoMapper lineaMapper;
    private final PagoPedidoMapper pagoMapper;

    public PedidoCatalogoMapper(LineaPedidoCatalogoMapper lineaMapper, 
                              PagoPedidoMapper pagoMapper) {
        this.lineaMapper = lineaMapper;
        this.pagoMapper = pagoMapper;
    }

    public PedidoCatalogo toEntity(PedidoCatalogoCreateDto dto) {
        if (dto == null) return null;
        
        PedidoCatalogo entity = new PedidoCatalogo();
        
        entity.setCodUsuario(dto.getCodUsuario());
        entity.setEstado(dto.getEstado() != null ? 
                       PedidoCatalogoEstado.valueOf(dto.getEstado()) : 
                       PedidoCatalogoEstado.Abierto);
        entity.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now());
        
        if (dto.getCatalogoId() != null) {
            Catalogo catalogo = new Catalogo();
            catalogo.setId(dto.getCatalogoId());
            entity.setCatalogo(catalogo);
        }
        
        return entity;
    }

    public PedidoCatalogoDto toDto(PedidoCatalogo entity) {
        if (entity == null) return null;
        
        PedidoCatalogoDto dto = new PedidoCatalogoDto();
        dto.setId(entity.getId());
        dto.setCodUsuario(entity.getCodUsuario());
        dto.setDireccion(entity.getDireccion());
        dto.setEstado(entity.getEstado().name());
        dto.setFecha(entity.getFecha());
        
        // Mapeo seguro de totales
        if (entity.getTotalBase() != null) {
            dto.setTotalPedidoBase(new TotalDto(
                entity.getTotalBase(),
                entity.getTotalBase(),
                entity.getTotalIva()
            ));
            dto.setTotalPedido(new TotalDto(
                entity.getTotalTotal(),
                entity.getTotalBase(),
                entity.getTotalIva()
            ));
            dto.setTotalEntrega(new TotalDto(
                entity.getTotalTotal(),
                entity.getTotalBase(),
                entity.getTotalIva()
            ));
        }

        // Mapeo seguro de líneas y pagos
        dto.setProductos(mapLineas(entity));
        dto.setPagos(mapPagos(entity));

        return dto;
    }

    private List<PedidoProductoCatalogoDto> mapLineas(PedidoCatalogo entity) {
        return entity.getLineas() == null ? 
            Collections.emptyList() :
            entity.getLineas().stream()
                .map(lineaMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<PagoDto> mapPagos(PedidoCatalogo entity) {
        return entity.getPagos() == null ? 
            Collections.emptyList() :
            entity.getPagos().stream()
                .map(pagoMapper::toDto)
                .collect(Collectors.toList());
    }
}