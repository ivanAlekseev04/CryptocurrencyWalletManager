package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CryptoInputDTO {
    @NotNull(message = "assetID cannot be null")
    @NotBlank(message = "assetID need to have minimum 1 non-whitespace symbol")
    private String assetID;

    @NotNull(message = "Amount of crypto cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount of crypto need to be greater than 0")
    private Double amount;
}
