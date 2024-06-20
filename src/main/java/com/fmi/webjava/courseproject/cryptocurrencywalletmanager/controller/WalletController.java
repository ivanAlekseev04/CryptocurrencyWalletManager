package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.controller;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Crypto;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository.CryptoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private CryptoRepository cryptoRepository;
    // TODO: in-memory List/Set for crypto that are actual (fetched in last 5 minutes) -> do it in service

    @PostMapping("/crypto")
    public ResponseEntity<Crypto> addCrypto(@RequestBody @Valid Crypto crypto) {
        return new ResponseEntity<>(cryptoRepository.save(Crypto.builder()
                .name(crypto.getName())
                .price(crypto.getPrice())
                .build()), HttpStatus.CREATED);
    }
}
