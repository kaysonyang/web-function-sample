package com.wsj.ms.fuction.repository;

import com.wsj.ms.fuction.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author kayson Yang
 * @description
 * @create 2017-04-28 11:05
 */
public interface ProductRepository {
    Mono<Product> getProduct(int id);

    Flux<Product> allProduct();

    Mono<Void> saveProduct(Mono<Product> product);
}
