package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserCryptoIdDTO {

    @NotNull(message = "UserCryptoIdDTO: cryptoName cannot be null")
    @NotBlank(message = "UserCryptoIdDTO: cryptoName need to have minimum 1 non-white space character")
    private String cryptoName;
}
