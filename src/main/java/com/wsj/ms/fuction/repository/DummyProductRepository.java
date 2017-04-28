package com.wsj.ms.fuction.repository;

import com.wsj.ms.fuction.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kayson Yang
 * @description
 * @create 2017-04-28 11:16
 */
public class DummyProductRepository implements ProductRepository{
    private final Map<Integer, Product> product = new HashMap<>();

    public DummyProductRepository() {
        this.product.put(1, new Product(1l, "椅子","200*200","red","200",100));
        this.product.put(2, new Product(2l, "板凳","200*200","blue","100",200));
    }



    @Override
    public Mono<Product> getProduct(int id) {
        return Mono.justOrEmpty(this.product.get(id));
    }

    @Override
    public Flux<Product> allProduct() {
        return Flux.fromIterable(this.product.values());
    }

    @Override
    public Mono<Void> saveProduct(Mono<Product> productMono) {
        return productMono.doOnNext(pro -> {
            int id = product.size() + 1;
            product.put(id, pro);
            System.out.format("Saved %s with id %d%n", pro, id);
        }).thenEmpty(Mono.empty());
    }
}
