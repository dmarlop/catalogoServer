package com.daw.services.mapper;

import org.springframework.stereotype.Component;

import com.daw.persistance.entities.PagoPedido;
import com.daw.persistance.enums.FormaPago;
import com.daw.persistance.enums.PagoPedidoEstado;
import com.daw.services.dto.PagoCreateDto;
import com.daw.services.dto.PagoDto;
@Component
public class PagoPedidoMapper {

    public PagoPedido toEntity(PagoCreateDto dto) {
        if (dto == null) {
            return null;
        }
        PagoPedido entity = new PagoPedido();
        entity.setCodPagoExterno(dto.getCodPago());
        entity.setCantidadEntregada(dto.getCantidadEntregada());
        entity.setCantidadAdeudada(dto.getCantidadAdeudada());
        entity.setEstado(PagoPedidoEstado.valueOf(dto.getEstado()));
        entity.setFormaPago(FormaPago.valueOf(dto.getFormaPago()));
        return entity;
    }

    public PagoDto toDto(PagoPedido entity) {
        if (entity == null) {
            return null;
        }
        PagoDto dto = new PagoDto();
        dto.setId(entity.getId());
        dto.setCodPago(entity.getCodPagoExterno());
        dto.setCantidadEntregada(entity.getCantidadEntregada());
        dto.setCantidadAdeudada(entity.getCantidadAdeudada());
        dto.setEstado(entity.getEstado().name());
        dto.setFormaPago(entity.getFormaPago().name());
        return dto;
    }
}