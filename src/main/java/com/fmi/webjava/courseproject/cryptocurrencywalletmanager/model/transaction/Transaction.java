package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
public class Transaction {
    @EmbeddedId
    @Valid
    private TransactionId id;

    @Column(name = "selling_profit")
    private Double sellingProfit;

    @NotNull
    @Column(name = "date_of_commit")
    private LocalDateTime dateOfCommit;
}
