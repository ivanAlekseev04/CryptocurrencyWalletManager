package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.CryptoInformation;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCrypto;

import java.util.Set;

public interface WalletService {
    Set<CryptoInformation> listOfferings(String assetType);
    CryptoInformation listOfferingsCertainAsset(String assetID);
    User depositMoney(Double amount);
    UserCrypto buyCrypto(String assetID, Double amount);
}
