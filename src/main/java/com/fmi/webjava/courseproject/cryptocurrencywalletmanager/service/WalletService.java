package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.CryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.exception.InsufficientFundsException;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.CryptoMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Crypto;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Transaction;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCrypto;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCryptoId;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository.CryptoRepository;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository.TransactionRepository;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository.UserCryptoRepository;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository.UserRepository;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.security.userdetails.CustomUserDetails;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.util.TransactionType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class WalletService {
    // TODO: in-memory List/Set for crypto that are actual (fetched in last 5 minutes) -> do it in service
    @Autowired
    private CryptoRepository cryptoRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserCryptoRepository userCryptoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CryptoMapper cryptoMapper;

    public User depositMoney(Double amount) {
        // TODO: maybe we need to check on negativeness

        User curUser = userRepository.findById(curUserId()).get();
        curUser.setMoney(curUser.getMoney() + amount);

        return userRepository.save(curUser);
    }

    public UserCrypto buyCrypto(CryptoDTO cryptoDTO, Double amount) {
        User curUser = userRepository.findById(curUserId()).get();
        Double overallPrice = amount * cryptoDTO.getPrice();

        if(curUser.getMoney() < overallPrice) {
            throw new InsufficientFundsException(String.format("User '%s'" +
                    " doesn't have enough money to buy crypto", curUser.getUserName()));
        }

        // FIXME: according to normal flow this data will be got from Set with actual cryptocurrencies, but later
        var existedCryptoCurrency = cryptoRepository.findByNameAndPrice(
                cryptoDTO.getName(), cryptoDTO.getPrice());

        if(existedCryptoCurrency.isEmpty()) {
            existedCryptoCurrency = Optional.of(cryptoRepository.
                    save(cryptoMapper.cryptoDTOToCrypto(cryptoDTO)));
        }

        var boughtCryptoCurrency = userCryptoRepository.findById(
                new UserCryptoId(curUser.getId(), cryptoDTO.getName()));

        var updatedCryptoBalance = depositCryptoToAccount(boughtCryptoCurrency.orElse(null),
                existedCryptoCurrency.get(), amount, curUser);

        curUser.setMoney(curUser.getMoney() - overallPrice);
        userRepository.save(curUser);

        return updatedCryptoBalance;
    }

    // TODO: implement
    public void sellCrypto() {

    }

    private Long curUserId() {
        var curUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return curUser.getId();
    }

    private Double recalculateAverageBuyingPrice(UserCrypto userCrypto,
                                                 Crypto crypto, Double amount) {
        return (crypto.getPrice() * amount +
                (userCrypto.getAverageCryptoBuyingPrice() *
                        userCrypto.getAmount())) / (amount + userCrypto.getAmount());
    }

    private UserCrypto depositCryptoToAccount(UserCrypto userCrypto, Crypto crypto
            , Double amount, User curUser) {

        if(userCrypto == null) {
            userCrypto = UserCrypto.builder()
                    .amount(0.0)
                    .averageCryptoBuyingPrice(0.0)
                    .id(new UserCryptoId(curUser.getId(), crypto.getName()))
                    .build();
        }

        transactionRepository.save(Transaction.builder()
                .crypto(crypto)
                .dateOfCommit(LocalDateTime.now())
                .userId(curUser.getId())
                .amount(amount)
                .type(TransactionType.BOUGHT.toString())
                .build());

        userCrypto.setAverageCryptoBuyingPrice(
                recalculateAverageBuyingPrice(userCrypto, crypto, amount));
        userCrypto.setAmount(userCrypto.getAmount() + amount);

        return userCryptoRepository.save(userCrypto);
    }
}
