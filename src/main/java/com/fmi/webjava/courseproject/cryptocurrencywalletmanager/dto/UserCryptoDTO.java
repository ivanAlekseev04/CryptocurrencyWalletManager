package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCryptoDTO {

    @NotNull
    private UserCryptoIdDTO id;

    @NotNull
    @Min(value = 0)
    private Double amount;
}
