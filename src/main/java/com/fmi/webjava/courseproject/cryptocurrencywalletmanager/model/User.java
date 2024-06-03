package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.transaction.Transaction;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCrypto;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank(message = "User: userName need to have minimum 1 non-white space character")
    @Length(max = 30, message = "User: userName can have length maximum 30 symbols")
    @Column(name = "user_name", unique = true)
    private String userName;

    @NotNull
    @NotBlank(message = "User: password need to have minimum 1 non-white space character")
    @Length(max = 256, message = "User: password can have length maximum 256 symbols")
    private String password;

    @Min(value = 0, message = "User: money cannot be negative")
    private Double money = 0.0;

    @Valid // TODO: test cascade type
    @OneToMany(mappedBy = "id.user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "id.user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<UserCrypto> cryptoCurrencies = new HashSet<>();

    @Column(name = "overall_transactions_profit")
    private Double overallTransactionsProfit = 0.0;

    public User(String userName, String password) {
        this.userName = userName;
        this.password =  password;
    }
}
