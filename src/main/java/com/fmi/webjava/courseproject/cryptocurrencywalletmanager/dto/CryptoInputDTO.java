package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CryptoInputDTO {
    @NotNull
    @NotBlank
    private String assetID;

    @NotNull
    @Min(value = 0)
    private Double amount;

}
