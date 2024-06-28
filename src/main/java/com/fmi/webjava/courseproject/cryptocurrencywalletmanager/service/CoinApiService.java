package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.CryptoInformation;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CoinApiService {

    private Set<CryptoInformation> crypto = new HashSet<>();

    public synchronized void setCrypto(Set<CryptoInformation> cryptoInfo) {
        crypto.clear();
        this.crypto.addAll(cryptoInfo);
    }

    public synchronized Set<CryptoInformation> getCrypto() {
        return new HashSet<>(this.crypto);
    }
}
