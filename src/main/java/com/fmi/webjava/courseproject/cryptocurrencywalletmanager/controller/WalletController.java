package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.controller;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.CryptoInformation;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.BuyCryptoInputDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.CryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserCryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTOOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.UserCryptoMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.UserMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCrypto;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service.WalletServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletServiceImpl walletService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserCryptoMapper userCryptoMapper;

    @GetMapping("/list_offerings")
    public ResponseEntity<Set<CryptoInformation>> listOfferings(@RequestParam(value = "asset_type", required = false) String assetType) {
        String type = "";
        if (assetType != null && assetType.equals("coins")) {
            type = "coins";
        }
        else if (assetType != null && assetType.equals("crypto")) {
            type = "crypto";
        }
        else if (assetType != null) {
            log.info("Invalid option for asset_type: valid ones - crypto/coins, chosen {}", assetType);
            throw new IllegalArgumentException("Invalid options for asset_type: valid ones are crypto/coins");
        }

        Set<CryptoInformation> assets = walletService.listOfferings(type);
        log.info("Returning assets to user {}", SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

    @GetMapping("/list_offerings/asset/{asset_id}")
    public ResponseEntity<CryptoInformation> getCertainAsset(@PathVariable("asset_id") String assetID) {
        if (assetID == null || assetID.isBlank()) {
            log.error("Invalid asset_id declaration");
            throw new IllegalArgumentException("Error: invalid assetID");
        }

        CryptoInformation asset = walletService.listOfferingsCertainAsset(assetID);
        return new ResponseEntity<>(asset, HttpStatus.OK);
    }

    @PatchMapping("/deposit_money")
    public ResponseEntity<UserDTOOutput> depositMoney(@RequestBody Map<String, Double> depositedMoney) {
        if (!depositedMoney.containsKey("money")) {
            throw new IllegalArgumentException("Error: invalid request body. Body should contain money declaration");
        }

        Double money = depositedMoney.get("money");
        if (money <= 0.0) {
            throw new IllegalArgumentException("Error: you can not deposit negative amount of money");
        }

        User updatedUser = walletService.depositMoney(money);
        UserDTOOutput out = userMapper.userToUserDTOOutput(updatedUser);
        return new ResponseEntity<>(out, HttpStatus.OK);
    }

    @PostMapping("/buy")
    public ResponseEntity<UserCryptoDTO> buyCrypto(@RequestBody @Valid BuyCryptoInputDTO input) {
        if (input.getAmount() <= 0.0) {
            throw new IllegalArgumentException("Error: invalid amount, must be greater than 0.0");
        }

        UserCrypto bought = walletService.buyCrypto(input.getAssetID(), input.getAmount());
        return new ResponseEntity<>(userCryptoMapper.userCryptoToUserCryptoDTO(bought), HttpStatus.OK);
    }
}
