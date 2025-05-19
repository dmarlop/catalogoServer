package com.daw.persistance.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.daw.persistance.enums.FormaPago;
import com.daw.persistance.enums.PagoPedidoEstado;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pago_pedido")
public class PagoPedido {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoCatalogo pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", nullable = false, columnDefinition = "enum('Metalico','Transferencia','Tarjeta','Bizum')")
    private FormaPago formaPago;

    @Column(name = "cod_pago_externo", length = 100)
    private String codPagoExterno;

    @Column(name = "cantidad_entregada", precision = 12, scale = 2)
    private BigDecimal cantidadEntregada;

    @Column(name = "cantidad_adeudada", precision = 12, scale = 2)
    private BigDecimal cantidadAdeudada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "enum('Abierto','Cancelado','Cerrado')")
    private PagoPedidoEstado estado;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // getters y setters...
}
