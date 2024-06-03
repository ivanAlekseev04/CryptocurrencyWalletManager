package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.transaction;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Crypto;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@Embeddable
public class TransactionId implements Serializable {
    @ManyToOne
    @NotNull
    @JoinColumn(name = "crypto_id")
    private Crypto crypto;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "type")
    @Length(min = 4, max = 6, message = "Transaction: type need to be between 4 and 6 characters")
    @Pattern(regexp = "bought|sold", message = "Transaction: type must be either 'bought' or 'sold'")
    private String type;
}
