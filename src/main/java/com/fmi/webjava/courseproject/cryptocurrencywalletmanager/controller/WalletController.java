package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.controller;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.CryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserCryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.UserCryptoMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.UserMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserCryptoMapper userCryptoMapper;

    @PostMapping("/deposit_money/{amount}")
    public ResponseEntity<Map<String, Double>> depositMoney(@PathVariable("amount") Double amount) {
        var incrementedBalance = Map.of("money", walletService.depositMoney(amount).getMoney());

        return new ResponseEntity<>(incrementedBalance, HttpStatus.OK);
    }

    @PostMapping("/buy/{amount}")
    public ResponseEntity<UserCryptoDTO> buyCrypto(@PathVariable("amount") Double amount, // TODO: UserCryptoDTO
                                                   @RequestBody @Valid CryptoDTO cryptoDTO) {

        return new ResponseEntity<>(userCryptoMapper.userCryptoToUserCryptoDTO(
                walletService.buyCrypto(cryptoDTO, amount)), HttpStatus.OK);
    }
}
