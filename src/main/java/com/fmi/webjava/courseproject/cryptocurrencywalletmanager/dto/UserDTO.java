package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@ToString
public class UserDTO {
    @NotNull(message = "UserDTO: userName cannot be null")
    @NotBlank(message = "UserDTO: userName need to have minimum 1 non-white space character")
    @Length(max = 30, message = "UserDTO: userName can have length maximum 30 symbols")
    private String userName;

    @Min(value = 0, message = "UserDTO: money cannot be negative")
    @NotNull(message = "UserDTO: money cannot null") // TODO: check
    private Double money;

    @NotNull(message = "UserDTO: transactions cannot be null")
    @Valid
    private Set<TransactionDTO> transactions;

    @NotNull(message = "UserDTO: cryptoCurrencies cannot be null")
    @Valid
    private Set<UserCryptoDTO> cryptoCurrencies;

    @NotNull(message = "UserDTO: overallTransactionsProfit cannot be null")
    private Double overallTransactionsProfit;
}
