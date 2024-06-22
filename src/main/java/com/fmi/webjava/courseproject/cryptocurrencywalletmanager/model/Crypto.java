package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Crypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crypto_id")
    private Long id;

    @NotNull(message = "Crypto: name can't be null")
    @NotBlank(message = "Crypto: name need to have minimum 1 non-white space character")
    private String name;

    @NotNull(message = "Crypto: price can't be null")
    @Min(value = 0, message = "Crypto: price cannot be negative")
    private Double price;

    @Valid
    @OneToMany(mappedBy = "crypto")
    @Builder.Default
    private Set<Transaction> transactions = new HashSet<>();
}
