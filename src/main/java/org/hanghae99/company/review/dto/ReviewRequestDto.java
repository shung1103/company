package org.hanghae99.company.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ReviewRequestDto {
    @NotBlank(message = "필수 입력 값입니다.")
    private Long userId;

    @NotBlank(message = "1점 ~ 5점")
    @Min(1)
    @Max(5)
    private Integer score;

    @NotBlank(message = "필수 입력 값입니다.")
    private String content;

    @Setter
    private String imageUrl;
}
