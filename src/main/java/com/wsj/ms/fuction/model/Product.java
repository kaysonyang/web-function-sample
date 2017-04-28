package com.wsj.ms.fuction.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author kayson Yang
 * @description
 * @create 2017-04-28 11:00
 */
@Data
public class Product {
    private Long id;
    private String name;
    private String specification;
    private String color;
    private String size;
    private int serial;

    public Product(@JsonProperty("id") Long id,
                   @JsonProperty("name") String name,
                   @JsonProperty("specification") String specification,
                   @JsonProperty("color") String color,
                   @JsonProperty("size") String size,
                   @JsonProperty("serial") int serial) {
        this.id = id;
        this.specification = specification;
        this.color = color;
        this.size = size;
        this.name = name;
        this.serial = serial;
    }

}
