package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCrypto {

    @EmbeddedId
    @Valid
    private UserCryptoId id;

    @NotNull(message = "UserCrypto: amount cannot be null")
    @Min(value = 0, message = "Asset: crypto amount cannot be negative")
    private Double amount;

    @Column(name = "average_crypto_buying_price")
    @NotNull(message = "UserCrypto: averageCryptoBuyingPrice cannot be null")
    @Min(value = 0, message = "Wallet: average crypto buying price cannot be negative")
    private Double averageCryptoBuyingPrice;

    @Valid
    @NotNull(message = "UserCrypto: user_id cannot be null")
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
