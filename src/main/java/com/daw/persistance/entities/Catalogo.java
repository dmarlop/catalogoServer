package com.daw.persistance.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.daw.persistance.entities.*;
import com.daw.persistance.enums.CatalogoStatus;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "catalogo")
public class Catalogo {

    @Id
    @Column(name = "id",columnDefinition = "CHAR(36)", length = 36, nullable = false)
    private String id;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "enum('Previo','Activo','Cerrado')")
    private CatalogoStatus status;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "catalogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoCatalogo> productos = new ArrayList<>();

    @OneToMany(mappedBy = "catalogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoCatalogo> pedidos = new ArrayList<>();

    // getters y setters...
}
