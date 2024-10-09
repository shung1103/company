package org.hanghae99.company.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class ProductRequestDto {
    @JsonIgnore
    private Long reviewCount = 0L;

    @JsonIgnore
    private Double score = 0.0;
}
