package com.imp.monolithic.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Quantity {
    @Column(name = "quantity")
    private Long value;

    public Quantity(final Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Long value) {
        if (value < 0L) {
            throw new IllegalArgumentException("재고 수량은 0보다 작을 수 없다.");
        }
    }
}
