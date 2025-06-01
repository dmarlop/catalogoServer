package com.daw.persistance.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "producto_catalogo")
public class ProductoCatalogo {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogo_id", columnDefinition = "CHAR(36)", nullable = false)
    private Catalogo catalogo;

    @Column(name = "producto_id", length = 36, nullable = false)
    private String productoId;

    @Column(name = "nombre_comercial", length = 150, nullable = false)
    private String nombreComercial;

    @Column(name = "pvp", precision = 10, scale = 2, nullable = false)
    private BigDecimal pvp;

    @Column(name = "iva", precision = 5, scale = 2, nullable = false)
    private BigDecimal iva;

    @Column(name = "codigo_barras", length = 50)
    private String codigoBarras;

    @Column(name = "categoria_id", length = 36)
    private String categoriaId;

    @Column(name = "categoria_nombre", length = 100)
    private String categoriaNombre; // <-- NUEVO CAMPO

    @Column(name = "subcategoria_id", length = 36)
    private String subcategoriaId;

    @Column(name = "subcategoria_nombre", length = 100)
    private String subcategoriaNombre; // <-- NUEVO CAMPO

    @Column(name = "caracteristicas", columnDefinition = "LONGTEXT")
    private String caracteristicas;
    @Column(name = "unidad_venta")
    private String unidadDeVenta;
    @Column(name = "ingredientes_flag", nullable = false)
    private Boolean ingredientesFlag;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productoCatalogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaPedidoCatalogo> lineasPedido = new ArrayList<>();

	

    // Getters y setters omitidos por brevedad, pero deben incluir los nuevos campos
}

