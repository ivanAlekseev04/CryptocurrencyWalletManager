package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTOOutput {

    private Long id;
    private String userName;
    private Double money;
    private Set<UserCryptoDTO> cryptocurrenciesDTO;
    private Double overallTransactionsProfit;
}
