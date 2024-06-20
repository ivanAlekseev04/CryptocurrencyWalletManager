package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
public class Crypto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank(message = "Asset: assetName need to have minimum 1 non-white space character")
    private String name;

    @NotNull
    @Min(value = 0, message = "Asset: price cannot be negative")
    private Double price;

//    public Crypto(String name, Double price) {
//        this.name = name;
//        this.price = price;
//    }
}
