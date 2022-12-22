package com.imp.monolithic.support;

import com.imp.monolithic.product.domain.Price;
import com.imp.monolithic.product.domain.Product;
import com.imp.monolithic.product.domain.Quantity;
import java.math.BigDecimal;

public enum ProductFixtures {
    PHONE("아이폰12", new Price(BigDecimal.valueOf(3000L))),
    ;

    private final String name;
    private final Price price;

    ProductFixtures(final String name, final Price price) {
        this.name = name;
        this.price = price;
    }

    public Product create(final Quantity quantity, final Long sellerId) {
        return new Product(name, price, quantity, sellerId);
    }
}
