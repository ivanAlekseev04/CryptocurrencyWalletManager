package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import java.time.LocalDateTime;

public record TransactionDTO(String type, Double profit, Double count, LocalDateTime dateOfCommit) {}
