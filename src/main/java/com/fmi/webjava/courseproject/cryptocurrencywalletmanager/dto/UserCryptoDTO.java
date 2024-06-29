package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCryptoDTO {

    @NotNull
    private UserCryptoIdDTO id;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double amount;
}
