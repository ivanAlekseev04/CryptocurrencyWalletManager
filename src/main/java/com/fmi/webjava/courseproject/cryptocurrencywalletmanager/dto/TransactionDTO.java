package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TransactionDTO {
    private Long id;

    @NotNull
    private Double sellingProfit;

    @NotNull(message = "TransactionDTO: dateOfCommit cannot be null")
    private LocalDateTime dateOfCommit;

    @NotNull(message = "TransactionDTO: cryptoDTO from the transaction cannot be null")
    @Valid
    private CryptoDTO crypto;

    @NotNull(message = "TransactionDTO: amount of crypto cannot be null")
    @Min(value = 0, message = "TransactionDTO: cannot perform transaction with a negative crypto amount")
    private Double amount;

    @NotNull(message = "TransactionDTO: transaction type cannot be null")
    @Length(min = 4, max = 6, message = "TransactionDTO: type need to be between 4 and 6 characters")
    @Pattern(regexp = "BOUGHT|SOLD", message = "Transaction: type must be either 'BOUGHT' or 'SOLD'")
    private String type;
}
