package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.CryptoInformation;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.CryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTOOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.exception.AssetNotFoundException;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class WalletServiceImpl implements WalletService {

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
    @Autowired
    private CoinApiService coinApiService;

    public User depositMoney(Double amount) {
        User curUser = userRepository.findById(curUserId()).get();
        curUser.setMoney(curUser.getMoney() + amount);

        userRepository.save(curUser);
        log.info("Successfully transferred " + amount + " to " + currUserName() + " balance");
        return curUser;
    }

    public Set<CryptoInformation> listOfferings(String assetType) {
        if (assetType.equals("crypto")) {
            log.info("User {} requested crypto assets", currUserName());
            return coinApiService.getCrypto().stream()
                    .filter(asset -> asset.isCrypto() == 1)
                    .collect(Collectors.toSet());
        }
        else if (assetType.equals("coins")) {
            log.info("User {} requested coins assets", currUserName());
            return coinApiService.getCrypto().stream()
                    .filter(asset -> asset.isCrypto() == 0)
                    .collect(Collectors.toSet());
        }

        log.info("User {} requested all type of assets", currUserName());
        return coinApiService.getCrypto();
    }

    public CryptoInformation listOfferingsCertainAsset(String assetID) {
        Optional<CryptoInformation> asset = coinApiService.getCrypto().stream()
                .filter(currAsset -> currAsset.assetID().equals(assetID))
                .findFirst();

        if (asset.isEmpty()) {
            log.info("Searching for unavailable asset with id {}", assetID);
            throw new AssetNotFoundException("Error: Asset with id " + assetID + " is not currently available");
        }

        log.info("Retrieving information for asset {}", assetID);
        return asset.get();
    }

    public UserCrypto buyCrypto(String assetID, Double amount) {
        Optional<CryptoInformation> wantedAsset = coinApiService.getCrypto().stream()
                .filter(currAsset -> currAsset.assetID().equals(assetID))
                .findFirst();

        if (wantedAsset.isEmpty()) {
            log.info("Searching for unavailable asset with id {}", assetID);
            throw new AssetNotFoundException("Error: Asset with id " + assetID + " is not currently available");
        }

        double assetCost = wantedAsset.get().price() * amount;
        User curUser = userRepository.findById(curUserId()).orElseThrow(() -> new IllegalArgumentException("User " + currUserName() + " is not presented"));
        if (curUser.getMoney() < assetCost) {
            log.info("User {} doesn't have enough amount of money", curUser.getUserName());
            throw new InsufficientFundsException("Error: Invalid operation. User " + currUserName() + " doesn't have enough amount of money");
        }

        var existedCryptoCurrency = cryptoRepository.findByNameAndPrice(assetID, wantedAsset.get().price());

        if (existedCryptoCurrency.isEmpty()) {
            log.info("There is not cryptocurrency with name {} and price {} in crypto table", assetID, wantedAsset.get().price());
            Crypto newCrypto = Crypto.builder()
                    .name(assetID)
                    .price(wantedAsset.get().price())
                    .build();

            existedCryptoCurrency = Optional.of(cryptoRepository.save(newCrypto));
            log.info("Saving new crypto {} to crypto table", newCrypto.toString());
        }

        var boughtCryptoCurrency = userCryptoRepository.findById(new UserCryptoId(curUser.getId(), assetID));

        var updatedCryptoBalance = depositCryptoToAccount(boughtCryptoCurrency.orElse(null),
                existedCryptoCurrency.get(), amount, curUser);

        curUser.setMoney(curUser.getMoney() - assetCost);
        userRepository.save(curUser);
        log.info("Updating existing amount of money for user {}, after the transaction", curUser.getUserName());

        return updatedCryptoBalance;
    }

//    public UserCrypto buyCrypto(CryptoDTO cryptoDTO, Double amount) {
//        User curUser = userRepository.findById(curUserId()).get();
//        Double overallPrice = amount * cryptoDTO.getPrice();
//
//        if(curUser.getMoney() < overallPrice) {
//            throw new InsufficientFundsException(String.format("User '%s'" +
//                    " doesn't have enough money to buy crypto", curUser.getUserName()));
//        }
//
//        // FIXME: according to normal flow this data will be got from Set with actual cryptocurrencies, but later
//        var existedCryptoCurrency = cryptoRepository.findByNameAndPrice(
//                cryptoDTO.getName(), cryptoDTO.getPrice());
//
//        if(existedCryptoCurrency.isEmpty()) {
//            existedCryptoCurrency = Optional.of(cryptoRepository.
//                    save(cryptoMapper.cryptoDTOToCrypto(cryptoDTO)));
//        }
//
//        var boughtCryptoCurrency = userCryptoRepository.findById(
//                new UserCryptoId(curUser.getId(), cryptoDTO.getName()));
//
//        var updatedCryptoBalance = depositCryptoToAccount(boughtCryptoCurrency.orElse(null),
//                existedCryptoCurrency.get(), amount, curUser);
//
//        curUser.setMoney(curUser.getMoney() - overallPrice);
//        userRepository.save(curUser);
//
//        return updatedCryptoBalance;
//    }

    // TODO: implement
    public void sellCrypto() {

    }

    private Long curUserId() {
        var curUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return curUser.getId();
    }

    private String currUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Double recalculateAverageBuyingPrice(UserCrypto userCrypto,
                                                 Crypto crypto, Double amount) {
        return (crypto.getPrice() * amount +
                (userCrypto.getAverageCryptoBuyingPrice() * userCrypto.getAmount())) / (amount + userCrypto.getAmount());
    }

    private UserCrypto depositCryptoToAccount(UserCrypto userCrypto, Crypto crypto
            , Double amount, User curUser) {

        if (userCrypto == null) {
            userCrypto = UserCrypto.builder()
                    .amount(0.0)
                    .averageCryptoBuyingPrice(0.0)
                    .id(new UserCryptoId(curUser.getId(), crypto.getName()))
                    .user(curUser)
                    .build();
            log.info("Creating new UserCrypto for the user {} and asset {}", curUser.getUserName(), crypto.getName());
        }

        userCrypto.setAverageCryptoBuyingPrice(recalculateAverageBuyingPrice(userCrypto, crypto, amount));
        log.info("Updating existing averageCryptoBuyingPrice for user {} and asset {}", curUser.getUserName(), crypto.getName());
        userCrypto.setAmount(userCrypto.getAmount() + amount);
        var result = userCryptoRepository.save(userCrypto);
        log.info("Updating the bought amount for user {} and asset {}", curUser.getUserName(), crypto.getName());


        transactionRepository.save(Transaction.builder()
                .crypto(crypto)
                .dateOfCommit(LocalDateTime.now())
                .user(curUser)
                .amount(amount)
                .type(TransactionType.BOUGHT.toString())
                .build());

        log.info("Creating buying transaction for user {}, crypto {}, amount {}", curUser.getUserName(), crypto.getName(), amount);

        return result;
    }
}
