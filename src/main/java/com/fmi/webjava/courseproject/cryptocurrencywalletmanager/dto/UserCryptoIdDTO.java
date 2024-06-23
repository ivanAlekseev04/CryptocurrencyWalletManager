package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCryptoIdDTO {
    @NotNull
    private Long userId;

    @NotNull
    @NotBlank
    private String cryptoName;
}
