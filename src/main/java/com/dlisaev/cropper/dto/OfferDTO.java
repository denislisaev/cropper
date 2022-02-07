package com.dlisaev.cropper.dto;

import com.dlisaev.cropper.annotations.OnlyPositiveConstraint;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OfferDTO {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String crop;
    @NotNull
    private Boolean typeOffer; // false - продажа, true - покупка
    @NotNull
    @Column(nullable = true)
    private Integer volume;
    @NotNull
    @Column(nullable = true)
    private Integer pricePerTon; //в рублях

    private String location;

    private String date;

    @Column(columnDefinition = "text")
    private String info;
}
