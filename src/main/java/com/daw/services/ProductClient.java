package com.daw.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.daw.services.dto.CategoriaDto;
import com.daw.services.dto.ProductoRemoteDto;
import com.daw.services.dto.SubCategoriaDto;

import java.util.List;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProductClient(
        RestTemplate restTemplate,
        @Value("${product.server.url}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl       = baseUrl;
        System.out.println(">>> ProductClient.baseUrl = " + baseUrl);
    }

    public ProductoRemoteDto getById(String productoId) {
        String url = baseUrl + "/productos/" + productoId;
        System.out.println(">>> GET Producto en URL: " + url);
        return restTemplate.getForObject(url, ProductoRemoteDto.class, productoId);
    }

     // Nuevo método para obtener productos por lista de IDs
    public List<ProductoRemoteDto> getByIds(List<String> productoIds) {
        String url = baseUrl + "/productos/batch";
        System.out.println(">>> POST Productos (batch) en URL: " + url);

        // Construir la petición POST con la lista de IDs en el cuerpo
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(productoIds);

        // Definir el tipo de respuesta esperado
        ParameterizedTypeReference<List<ProductoRemoteDto>> responseType =
                new ParameterizedTypeReference<List<ProductoRemoteDto>>() {};

        // Realizar la petición POST y obtener la respuesta
        ResponseEntity<List<ProductoRemoteDto>> responseEntity =
                restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);

        // Devolver la lista de productos
        return responseEntity.getBody();
    }


    public CategoriaDto getCategoria(String categoriaId) {
        String url = baseUrl + "/categorias/" + categoriaId;
        System.out.println(">>> GET Categoria en URL: " + url);
        return restTemplate.getForObject(url, CategoriaDto.class, categoriaId);
    }

    public SubCategoriaDto getSubcategoria(String categoriaId, String subcategoriaId) {
        String url = baseUrl
            + "/categorias/" + categoriaId
            + "/subcategorias/" + subcategoriaId;
        System.out.println(">>> GET Subcategoria en URL: " + url);
        return restTemplate.getForObject(
            url, SubCategoriaDto.class, categoriaId, subcategoriaId
        );
    }


}
