package com.daw.persistance.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.daw.persistance.enums.PedidoCatalogoEstado;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_catalogo")
public class PedidoCatalogo {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", length = 36, nullable = false)
    private String id;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogo_id", columnDefinition = "CHAR(36)", nullable = false)
    private Catalogo catalogo;

    @Column(name = "cod_usuario", length = 36, nullable = false)
    private String codUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, 
            columnDefinition = "enum('Abierto','Cerrado','Montando','Enviado','Recepcionado','Pagado','Cancelado')")
    private PedidoCatalogoEstado estado;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total_base", precision = 12, scale = 2)
    private BigDecimal totalBase;

    @Column(name = "total_iva", precision = 12, scale = 2)
    private BigDecimal totalIva;

    @Column(name = "total_total", precision = 12, scale = 2)
    private BigDecimal totalTotal;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaPedidoCatalogo> lineas = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoPedido> pagos = new ArrayList<>();

	

    
}
