package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCryptoId implements Serializable {
    // This column refers user_id column in user table
    @NotNull(message = "UserCryptoId: user cannot be null")
    private Long userId;

    @NotNull(message = "UserCryptoId: cryptoName cannot be null")
    @NotBlank(message = "UserCrypto: cryptoName need to have minimum 1 non-white space character")
    @Column(name = "crypto_name")
    private String cryptoName;
}
