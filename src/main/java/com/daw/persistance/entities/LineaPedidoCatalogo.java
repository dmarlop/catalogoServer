package com.daw.persistance.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.daw.persistance.enums.CatalogoStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "linea_pedido_catalogo")
public class LineaPedidoCatalogo {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoCatalogo pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_catalogo_id", nullable = false)
    private ProductoCatalogo productoCatalogo;

    @Column(name = "cantidad_pedida", precision = 10, scale = 2, nullable = false)
    private BigDecimal cantidadPedida;

    @Column(name = "cantidad_entregada", precision = 10, scale = 2)
    private BigDecimal cantidadEntregada = BigDecimal.ZERO;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    
}
