package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import java.util.List;

public record UserDTO(String userName, Double money, List<TransactionDTO> transactions) {}
