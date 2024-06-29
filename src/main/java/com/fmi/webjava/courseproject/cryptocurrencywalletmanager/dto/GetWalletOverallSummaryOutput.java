package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import java.util.Set;

public record GetWalletOverallSummaryOutput(Long id, String username, Double money, Double overall_profit, Set<GetWalletSummaryOutput> active_assets) { }

