package com.dlisaev.cropper.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OfferListDTO {
    private List<OfferDTO> offers;
    @NotNull
    private int size;
}
