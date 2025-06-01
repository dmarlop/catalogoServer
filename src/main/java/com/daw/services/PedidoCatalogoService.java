package com.daw.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.daw.persistance.entities.Catalogo;
import com.daw.persistance.entities.LineaPedidoCatalogo;
import com.daw.persistance.entities.PedidoCatalogo;
import com.daw.persistance.entities.ProductoCatalogo;
import com.daw.persistance.enums.PedidoCatalogoEstado;
import com.daw.persistance.repository.CatalogoRepository;
import com.daw.persistance.repository.LineaPedidoCatalogoRepository;
import com.daw.persistance.repository.PedidoCatalogoRepository;
import com.daw.persistance.repository.ProductoCatalogoRepository;
import com.daw.services.dto.PedidoCatalogoCreateDto;
import com.daw.services.dto.PedidoCatalogoDto;
import com.daw.services.dto.PedidoCompletoCreateDto;
import com.daw.services.dto.PedidoCompletoCreateDto.LineaPedidoCreateDto;
import com.daw.services.dto.*;
import com.daw.services.dto.PedidoProductoCatalogoDto;
import com.daw.services.mapper.LineaPedidoCatalogoMapper;
import com.daw.services.mapper.PedidoCatalogoMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PedidoCatalogoService {

    private final PedidoCatalogoRepository pedidoCatalogoRepository;
    private final LineaPedidoCatalogoRepository lineaPedidoCatalogoRepository;
    private final ProductoCatalogoRepository productoCatalogoRepository;
    private final CatalogoRepository catalogoRepository;
    private final PedidoCatalogoMapper mapper;
    private final LineaPedidoCatalogoMapper lineaMapper;

    // CRUD Básico (existente)
    public PedidoCatalogo crearPedido(PedidoCatalogoCreateDto createDto) {
        Catalogo catalogo = catalogoRepository.findById(createDto.getCatalogoId())
            .orElseThrow(() -> new RuntimeException("Catálogo no encontrado con ID: " + createDto.getCatalogoId()));

        PedidoCatalogo pedido = new PedidoCatalogo();
        
        pedido.setId(UUID.randomUUID().toString());
        pedido.setCatalogo(catalogo);
        pedido.setCodUsuario(createDto.getCodUsuario());
        pedido.setEstado(PedidoCatalogoEstado.valueOf(createDto.getEstado()));
        pedido.setFecha(createDto.getFecha() != null ? createDto.getFecha() : LocalDateTime.now());
        return pedidoCatalogoRepository.save(pedido);
    }

    public PedidoCatalogo obtenerPedidoPorId(String pedidoId) {
        return pedidoCatalogoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<PedidoCatalogoDto> obtenerPorCatalogo(String  catalogo) {
        List<PedidoCatalogo> entidades = pedidoCatalogoRepository.findByCatalogoId(catalogo);
        
        
        return entidades.stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }
    
    public List<PedidoCatalogoDto> obtenerPedidosPorEstados(PedidoCatalogoEstado  estado) {
        List<PedidoCatalogo> entidades = pedidoCatalogoRepository.findByEstado(estado);
        
        
        return entidades.stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    public PedidoCatalogo actualizarEstadoPedido(String pedidoId, EstadoDto nuevoEstado) {
        PedidoCatalogo pedido = obtenerPedidoPorId(pedidoId);
        String estadoComoTexto = nuevoEstado.getNuevoEstado();

        PedidoCatalogoEstado estadoEnum = Arrays.stream(PedidoCatalogoEstado.values())
            .filter(e -> e.name().equalsIgnoreCase(estadoComoTexto)) // ⚠️ ignora mayúsculas/minúsculas
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Estado no válido: " + estadoComoTexto));

        pedido.setEstado(estadoEnum);
        return pedidoCatalogoRepository.save(pedido);
    }

    public void eliminarPedido(String pedidoId) {
        PedidoCatalogo pedido = obtenerPedidoPorId(pedidoId);
        pedidoCatalogoRepository.delete(pedido);
    }

    public List<PedidoCatalogo> obtenerPedidosPorUsuario(String codUsuario) {
        return pedidoCatalogoRepository.findByCodUsuario(codUsuario);
    }

    


    private PedidoCatalogoDto mapearPedidoCompleto(PedidoCatalogo pedido) {
        PedidoCatalogoDto dto = mapper.toDto(pedido);
        
        
        List<PedidoProductoCatalogoDto> lineasDto = pedido.getLineas().stream()
            .map(linea -> {
                PedidoProductoCatalogoDto lineaDto = lineaMapper.toDto(linea);
                lineaDto.setNombreProducto(linea.getProductoCatalogo().getNombreComercial());
                lineaDto.setPrecioUnitario(linea.getPrecioUnitario());
                lineaDto.setSubtotal(linea.getPrecioUnitario().multiply(linea.getCantidadPedida()));
                return lineaDto;
            })
            .collect(Collectors.toList());
        
        dto.setProductos(lineasDto);
        return dto;
    }

    @Transactional
    public PedidoCatalogoDto agregarProductoALinea(String pedidoId, PedidoProductoCatalogoDto productoDto) {
        PedidoCatalogo pedido = obtenerPedidoPorId(pedidoId);
        ProductoCatalogo producto = productoCatalogoRepository.findById(productoDto.getCodProductoCatalogo())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Buscar o crear línea de pedido
        LineaPedidoCatalogo linea = lineaPedidoCatalogoRepository.findByPedidoAndProductoCatalogo(pedido, producto)
            .orElseGet(() -> {
                LineaPedidoCatalogo nuevaLinea = new LineaPedidoCatalogo();
                nuevaLinea.setId(UUID.randomUUID().toString());
                nuevaLinea.setPedido(pedido);
                nuevaLinea.setProductoCatalogo(producto);
                nuevaLinea.setPrecioUnitario(producto.getPvp());
                nuevaLinea.setCantidadEntregada(BigDecimal.ZERO);
                return nuevaLinea;
            });

        
        linea.setCantidadPedida(linea.getCantidadPedida().add(productoDto.getCantidadPedida()));
        lineaPedidoCatalogoRepository.save(linea);

        
        actualizarTotalesPedido(pedido);
        PedidoCatalogo pedidoActualizado = pedidoCatalogoRepository.save(pedido);
        return mapearPedidoCompleto(pedidoActualizado);
    }

    public void actualizarTotalesPedido(PedidoCatalogo pedido) {
        BigDecimal totalBase = pedido.getLineas().stream()
            .map(linea -> linea.getPrecioUnitario().multiply(linea.getCantidadPedida()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIva = totalBase.multiply(BigDecimal.valueOf(0.21));
        BigDecimal totalTotal = totalBase.add(totalIva);

        pedido.setTotalBase(totalBase);
        pedido.setTotalIva(totalIva);
        pedido.setTotalTotal(totalTotal);
    }

    
    public void eliminarLineaDePedido(String pedidoId, String lineaId) {
        LineaPedidoCatalogo linea = lineaPedidoCatalogoRepository.findById(lineaId)
            .orElseThrow(() -> new RuntimeException("Línea de pedido no encontrada"));
        
        if (!linea.getPedido().getId().equals(pedidoId)) {
            throw new RuntimeException("La línea no pertenece al pedido especificado");
        }

        lineaPedidoCatalogoRepository.delete(linea);
        actualizarTotalesPedido(linea.getPedido());
    }

    public List<PedidoProductoCatalogoDto> obtenerLineasDePedido(String pedidoId) {
        return lineaPedidoCatalogoRepository.findByPedidoId(pedidoId).stream()
            .map(lineaMapper::toDto)
            .collect(Collectors.toList());
    }

   
    public List<PedidoCatalogoDto> findAllDtos() {
        return pedidoCatalogoRepository.findAll().stream()
            .map(this::mapearPedidoCompleto)
            .collect(Collectors.toList());
    }

    public List<PedidoCatalogo> getAll() {
        return pedidoCatalogoRepository.findAll();
    }
    
    public PedidoCatalogoDto crearPedidoCompleto(PedidoCompletoCreateDto createDto) {

        if (createDto.getLineas() == null || createDto.getLineas().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto");
        }


        Catalogo catalogo = catalogoRepository.findById(createDto.getCatalogoId())
            .orElseThrow(() -> new RuntimeException("Catálogo no encontrado"));

        PedidoCatalogo pedido = new PedidoCatalogo();
        pedido.setId(UUID.randomUUID().toString());
        pedido.setCodUsuario(createDto.getCodUsuario());
        pedido.setEstado(PedidoCatalogoEstado.Abierto);
        pedido.setFecha(LocalDateTime.now());
        pedido.setCatalogo(catalogo);
        pedido.setDireccion(createDto.getDireccion());
        pedido.setEntrega(createDto.getEntrega());

       
        List<LineaPedidoCatalogo> lineas = new ArrayList<>();
        BigDecimal totalBase = BigDecimal.ZERO;

        for (LineaPedidoCreateDto lineaDto : createDto.getLineas()) {
            ProductoCatalogo producto = productoCatalogoRepository.findById(lineaDto.getCodProductoCatalogo())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + lineaDto.getCodProductoCatalogo()));

            LineaPedidoCatalogo linea = new LineaPedidoCatalogo();
            linea.setId(UUID.randomUUID().toString());
            linea.setPedido(pedido);
            linea.setProductoCatalogo(producto);
            linea.setCantidadPedida(lineaDto.getCantidadPedida());
            linea.setCantidadEntregada(lineaDto.getCantidadPedida()); 
            linea.setPrecioUnitario(producto.getPvp());

            lineas.add(linea);
            totalBase = totalBase.add(producto.getPvp().multiply(lineaDto.getCantidadPedida()));
        }


        BigDecimal totalIva = totalBase.multiply(new BigDecimal("0.21"));
        BigDecimal totalTotal = totalBase.add(totalIva);

        pedido.setTotalBase(totalBase);
        pedido.setTotalIva(totalIva);
        pedido.setTotalTotal(totalTotal);
        pedido.setLineas(lineas);

        PedidoCatalogo pedidoGuardado = pedidoCatalogoRepository.save(pedido);
        lineaPedidoCatalogoRepository.saveAll(lineas);

        return mapper.toDto(pedidoGuardado);
    }
  
    @Transactional
    public PedidoCatalogoDto actualizarPedidoCompleto(String pedidoId, PedidoCompletoCreateDto dto) {
        PedidoCatalogo pedido = pedidoCatalogoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // 🔥 Eliminar todas las líneas anteriores del pedido
        lineaPedidoCatalogoRepository.deleteAllByPedidoId(pedidoId);

        // 🔄 Actualizar datos básicos del pedido
        pedido.setDireccion(dto.getDireccion());
        pedido.setEntrega(dto.getEntrega());

        List<LineaPedidoCatalogo> nuevasLineas = new ArrayList<>();
        BigDecimal totalBase = BigDecimal.ZERO;

        for (PedidoCompletoCreateDto.LineaPedidoCreateDto lineaDto : dto.getLineas()) {
            ProductoCatalogo producto = productoCatalogoRepository.findById(lineaDto.getCodProductoCatalogo())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + lineaDto.getCodProductoCatalogo()));

            LineaPedidoCatalogo linea = new LineaPedidoCatalogo();
            linea.setId(UUID.randomUUID().toString());
            linea.setPedido(pedido);
            linea.setProductoCatalogo(producto);
            linea.setCantidadPedida(lineaDto.getCantidadPedida());
            linea.setCantidadEntregada(lineaDto.getCantidadPedida());
            linea.setPrecioUnitario(producto.getPvp());

            nuevasLineas.add(linea);
            totalBase = totalBase.add(producto.getPvp().multiply(lineaDto.getCantidadPedida()));
        }

        // 🧮 Calcular totales
        BigDecimal totalIva = totalBase.multiply(new BigDecimal("0.21"));
        BigDecimal totalTotal = totalBase.add(totalIva);

        pedido.setTotalBase(totalBase);
        pedido.setTotalIva(totalIva);
        pedido.setTotalTotal(totalTotal);

        // 💾 Guardar el pedido actualizado
        PedidoCatalogo actualizado = pedidoCatalogoRepository.save(pedido);

        // 💾 Guardar las nuevas líneas
        lineaPedidoCatalogoRepository.saveAll(nuevasLineas);

        // 🔁 Devolver DTO actualizado
        return mapper.toDto(actualizado);
    }






    
    public boolean tieneUsuarioPedidoAbierto(String usuarioId, String catalogoId) {
        if (usuarioId == null || catalogoId == null) {
            return false;
        }
        
        List<PedidoCatalogo> pedidos = pedidoCatalogoRepository
            .findByCodUsuarioAndCatalogoIdAndEstado(
                usuarioId, 
                catalogoId, 
                PedidoCatalogoEstado.Abierto
            );
        
        // Si hay múltiples pedidos abiertos, es un error de datos
        if (pedidos.size() > 1) {
            log.error("El usuario {} tiene {} pedidos abiertos para el catálogo {}", 
                usuarioId, pedidos.size(), catalogoId);
        }
        
        return !pedidos.isEmpty();
    }
    
    @Transactional
    public void actualizarVariasCantidades(String pedidoId, List<ActualizarCantidadDto> actualizaciones) {
        PedidoCatalogo pedido = obtenerPedidoPorId(pedidoId);
        if (pedido.getEstado() != PedidoCatalogoEstado.Abierto) {
            throw new IllegalStateException("Solo se pueden modificar pedidos en estado Abierto");
        }
        // Actualizar cantidades
        for (ActualizarCantidadDto dto : actualizaciones) {
            for (LineaPedidoCatalogo linea : pedido.getLineas()) {
                if (linea.getProductoCatalogo().getId().equals(dto.getProductoCatalogoId())) {
                    linea.setCantidadPedida(dto.getNuevaCantidad());
                }
            }
        }

        // Actualizar totales
        actualizarTotalesPedido(pedido);

        // Cambiar estado si está Abierto
        if (pedido.getEstado() == PedidoCatalogoEstado.Abierto) {
            pedido.setEstado(PedidoCatalogoEstado.Montando);
        }

        pedidoCatalogoRepository.save(pedido);
    }
    
    public List<PedidoCatalogoDto> obtenerPedidosPorEstados(Set<String> estadosStr) {
        Set<PedidoCatalogoEstado> estadosEnum = estadosStr.stream()
            .map(PedidoCatalogoEstado::valueOf)
            .collect(Collectors.toSet());

        List<PedidoCatalogo> pedidos = pedidoCatalogoRepository.findByEstadoIn(estadosEnum);

        return pedidos.stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }


}
