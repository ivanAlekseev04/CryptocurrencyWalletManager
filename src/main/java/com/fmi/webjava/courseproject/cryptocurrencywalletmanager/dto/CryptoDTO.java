package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CryptoDTO {
    @NotNull
    @NotBlank(message = "CryptoDTO: name need to have minimum 1 non-white space character")
    private String name;

    @NotNull
    @Min(value = 0, message = "CryptoDTO: price cannot be negative")
    private Double price;
}
