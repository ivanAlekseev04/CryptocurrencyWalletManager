package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.CryptoInformation;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.BoughtCryptoOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.GetWalletOverallSummaryOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.GetWalletSummaryOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.SoldCryptoOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Transaction;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface WalletService {
    Set<CryptoInformation> listOfferings(String assetType);
    CryptoInformation listOfferingsCertainAsset(String assetID);
    User depositMoney(Double amount);
    BoughtCryptoOutput buyCrypto(String assetID, Double amount);
    SoldCryptoOutput sellCrypto(String assetID, Double amount);
    Set<GetWalletSummaryOutput> wallet_summary(String assetID);
    GetWalletOverallSummaryOutput wallet_overall_summary();
    List<Transaction> getTransactionHistory(String type, String assetId);
    List<Transaction> getTransactionHistoryWithinPeriod(LocalDateTime before, LocalDateTime after);
}
