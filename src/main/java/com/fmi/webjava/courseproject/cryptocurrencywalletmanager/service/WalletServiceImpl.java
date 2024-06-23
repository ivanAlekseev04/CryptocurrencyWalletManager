package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.CryptoInformation;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.BoughtCryptoOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.SoldCryptoOutput;
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

    public BoughtCryptoOutput buyCrypto(String assetID, Double amount) {
        CryptoInformation wantedAsset = getCryptoInformationIfAvailable(assetID);

        double assetCost = wantedAsset.price() * amount;
        User curUser = userRepository.findById(curUserId()).orElseThrow(() -> new IllegalArgumentException("User " + currUserName() + " is not presented"));
        if (curUser.getMoney() < assetCost) {
            log.info("User {} doesn't have enough amount of money", curUser.getUserName());
            throw new InsufficientFundsException("Error: Invalid operation. User " + currUserName() + " doesn't have enough amount of money");
        }

        var existedCryptoCurrency = checkIfCryptoIsAvailable(assetID, wantedAsset.price());

        var boughtCryptoCurrency = userCryptoRepository.findById(new UserCryptoId(curUser.getId(), assetID));

        var updatedCryptoBalance = depositCryptoToAccount(boughtCryptoCurrency.orElse(null),
                existedCryptoCurrency, amount, curUser);

        curUser.setMoney(curUser.getMoney() - assetCost);
        userRepository.save(curUser);
        log.info("Updating existing amount of money for user {}, after the transaction", curUser.getUserName());

        return new BoughtCryptoOutput(curUser.getUserName(), assetID, "You have successfully bought " + amount + " of " + assetID,
                amount, existedCryptoCurrency.getPrice());
    }

    public SoldCryptoOutput sellCrypto(String assetID, Double amount) {
        CryptoInformation wantedAsset = getCryptoInformationIfAvailable(assetID);

        Optional<UserCrypto> userCrypto = userCryptoRepository.findByCryptoNameAndUserId(assetID, curUserId());
        if (userCrypto.isEmpty()) {
            log.error("Error trying to sell asset {} without already bought it", assetID);
            throw new AssetNotFoundException("Error: You can not sell asset " + assetID + " without already bought it");
        }

        if (userCrypto.get().getAmount() < amount) {
            log.error("Error trying to sell more amount of asset {} than already have bought", assetID);
            throw new IllegalArgumentException("Error: You can not sell more amount of asset " + assetID + " than you have already bought");
        }

        Crypto existedCryptocurrency = checkIfCryptoIsAvailable(assetID, wantedAsset.price());
        User curUser = userRepository.findById(curUserId()).
                orElseThrow(() -> new IllegalArgumentException("User " + currUserName() + " is not presented"));

        Double currAssetPrice = wantedAsset.price();
        Double moneyEarn = currAssetPrice * amount;
        Double sellingProfit = amount * (currAssetPrice - userCrypto.get().getAverageCryptoBuyingPrice());

        transactionRepository.save(Transaction.builder()
                .crypto(existedCryptocurrency)
                .dateOfCommit(LocalDateTime.now())
                .user(curUser)
                .amount(amount)
                .type(TransactionType.SOLD.toString())
                .sellingProfit(sellingProfit)
                .build());

        log.info("Creating selling transaction for user {}, crypto {}, amount {}, profit {}",
                curUser.getUserName(), existedCryptocurrency.getName(), amount, sellingProfit);


        curUser.setMoney(curUser.getMoney() + moneyEarn);
        log.info("Updating user {} amount of money after selling {} of {}", curUser.getUserName(), amount, assetID);
        curUser.setOverallTransactionsProfit(curUser.getOverallTransactionsProfit() + sellingProfit);
        log.info("Updating user {} overall profit after selling {} of {}", curUser.getUserName(), amount, assetID);
        userRepository.save(curUser);

        if (userCrypto.get().getAmount().doubleValue() != amount) {
            userCrypto.get().setAmount(userCrypto.get().getAmount() - amount);
            log.info("Updating the amount of asset {} after selling {} of it", assetID, amount);
            userCryptoRepository.save(userCrypto.get());
        }
        else {
            userCryptoRepository.delete(userCrypto.get());
            log.info("Deleting user_crypto when selling all amount of {}", assetID);
        }

        return new SoldCryptoOutput(curUser.getUserName(), assetID, "You have successfully sold " + amount + " of " + assetID,
                amount, sellingProfit);
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

    private CryptoInformation getCryptoInformationIfAvailable(String assetID) {
        Optional<CryptoInformation> wantedAsset = coinApiService.getCrypto().stream()
                .filter(currAsset -> currAsset.assetID().equals(assetID))
                .findFirst();

        if (wantedAsset.isEmpty()) {
            log.info("Searching for unavailable asset with id {}", assetID);
            throw new AssetNotFoundException("Error: Asset with id " + assetID + " is not currently available");
        }

        return wantedAsset.get();
    }

    private Crypto checkIfCryptoIsAvailable(String assetID, Double price) {
        var existedCryptoCurrency = cryptoRepository.findByNameAndPrice(assetID, price);

        if (existedCryptoCurrency.isEmpty()) {
            log.info("There is not cryptocurrency with name {} and price {} in crypto table", assetID, price);
            Crypto newCrypto = Crypto.builder()
                    .name(assetID)
                    .price(price)
                    .build();

            existedCryptoCurrency = Optional.of(cryptoRepository.save(newCrypto));
            log.info("Saving new crypto {} to crypto table", newCrypto.toString());
        }

        return existedCryptoCurrency.get();
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
