package com.wsj.ms.fuction.service;

import com.wsj.ms.fuction.model.Product;
import com.wsj.ms.fuction.repository.ProductRepository;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * @author kayson Yang
 * @description
 * @create 2017-04-28 11:11
 */
public class ProductHandler {
    private final ProductRepository repository;

    public ProductHandler(ProductRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getProduct(ServerRequest request) {

        int personId = Integer.valueOf(request.pathVariable("id"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Product> productMono = this.repository.getProduct(personId);
        return productMono
                .flatMap(product -> ServerResponse.ok().contentType(APPLICATION_JSON_UTF8).body(fromObject(product)))
                .switchIfEmpty(notFound);
    }


    public Mono<ServerResponse> createProduct(ServerRequest request) {
        Mono<Product> product = request.bodyToMono(Product.class);
        return ServerResponse.ok().build(this.repository.saveProduct(product));
    }

    public Mono<ServerResponse> listProduct(ServerRequest request) {
        Flux<Product> people = this.repository.allProduct();
        return ServerResponse.ok().contentType(APPLICATION_JSON_UTF8).body(people, Product.class);
    }
}
