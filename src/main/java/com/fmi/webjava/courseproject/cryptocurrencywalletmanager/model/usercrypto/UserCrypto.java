package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "user_crypto")
@Getter
@Setter
@ToString
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class UserCrypto {
    @EmbeddedId
    @NotNull
    @Valid
    private UserCryptoId id;

    @NotNull
    @Min(value = 0, message = "Asset: crypto amount cannot be negative")
    private Double amount;

    @Column(name = "average_crypto_buying_price")
    @NotNull
    @Min(value = 0, message = "Wallet: average crypto buying price cannot be negative")
    private Double averageCryptoBuyingPrice;
}
