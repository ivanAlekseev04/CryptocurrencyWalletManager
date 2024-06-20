package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Crypto;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class UserCryptoId implements Serializable {
    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "crypto_id")
    private Crypto crypto;
}
