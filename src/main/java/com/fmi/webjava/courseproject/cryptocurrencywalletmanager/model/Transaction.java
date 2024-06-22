package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "selling_profit")
    private Double sellingProfit;

    @NotNull(message = "Transaction: dateOfCommit cannot be null")
    @Column(name = "date_of_commit")
    private LocalDateTime dateOfCommit;

    @NotNull(message = "Transaction: amount of crypto cannot be null")
    @Min(value = 0, message = "Transaction: cannot perform transaction with a negative crypto amount")
    private Double amount;

    @NotNull(message = "Transaction: transaction type cannot be null")
    @Length(min = 4, max = 6, message = "Transaction: type need to be between 4 and 6 characters")
    @Pattern(regexp = "BOUGHT|SOLD", message = "Transaction: type must be either 'BOUGHT' or 'SOLD'")
    private String type;

    @NotNull(message = "Transaction: crypto from the transaction cannot be null")
    @Valid
    @ManyToOne
    @JoinColumn(name = "crypto_id")
    private Crypto crypto;

    @NotNull(message = "Transaction: user_id cannot be null")
    @Valid
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
