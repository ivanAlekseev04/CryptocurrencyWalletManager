package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.CryptoInformation;

import java.util.Set;

public interface WalletService {
    Set<CryptoInformation> listOfferings(String assetType);
}
