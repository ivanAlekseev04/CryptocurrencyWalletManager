package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserCryptoDTO {
    @Valid
    private UserCryptoIdDTO id;

    @NotNull(message = "UserCryptoDTO: amount cannot be null")
    @Min(value = 0, message = "UserCryptoDTO: crypto amount cannot be negative")
    private Double amount;

    @Column(name = "average_crypto_buying_price")
    @NotNull(message = "UserCryptoDTO: averageCryptoBuyingPrice cannot be null")
    @Min(value = 0, message = "UserCryptoDTO: average crypto buying price cannot be negative")
    private Double averageCryptoBuyingPrice;
}
