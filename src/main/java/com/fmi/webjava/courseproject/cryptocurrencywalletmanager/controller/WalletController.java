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

    // FIXME: only for debugging
    @PostMapping("/crypto")
    public ResponseEntity<Crypto> addCrypto(@RequestBody @Valid Crypto crypto) {
        return new ResponseEntity<>(cryptoRepository.save(new Crypto(crypto.getName()
                , crypto.getPrice())), HttpStatus.CREATED);
    }
}
