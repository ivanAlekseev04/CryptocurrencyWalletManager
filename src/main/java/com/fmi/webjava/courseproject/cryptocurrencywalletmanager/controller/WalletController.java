package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.controller;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.CryptoInformation;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.BoughtCryptoOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.CryptoInputDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.GetWalletOverallSummaryOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.GetWalletSummaryOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.SoldCryptoOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.TransactionDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTOOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.TransactionMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.UserCryptoMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.UserMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service.WalletService;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.util.TransactionType;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserCryptoMapper userCryptoMapper;
    @Autowired
    private TransactionMapper transactionMapper;

    @GetMapping("/offerings")
    public ResponseEntity<Set<CryptoInformation>> listOfferings(@RequestParam(value = "asset_type", required = false) String assetType) {
        var allowedTypes = List.of("coins", "crypto");

        if (assetType != null && Collections.disjoint(allowedTypes, List.of(assetType))) {
            log.info("Invalid option for asset_type: valid ones - crypto/coins, chosen {}", assetType);
            throw new IllegalArgumentException("Invalid options for asset_type: valid ones are crypto/coins");
        }

        Set<CryptoInformation> assets = walletService.listOfferings(assetType);
        log.info("Returning assets to user {}", SecurityContextHolder.getContext().getAuthentication().getName());

        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

    @GetMapping("/offerings/{asset_id}")
    public ResponseEntity<CryptoInformation> getCertainAsset(@PathVariable("asset_id") String assetID) {
        if (assetID == null || assetID.isBlank()) {
            log.error("Invalid asset_id declaration");
            throw new IllegalArgumentException("Error: invalid assetID");
        }

        CryptoInformation asset = walletService.listOfferingsCertainAsset(assetID);
        return new ResponseEntity<>(asset, HttpStatus.OK);
    }

    @PatchMapping("/money/deposit")
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
    public ResponseEntity<BoughtCryptoOutput> buyCrypto(@RequestBody @Valid CryptoInputDTO input) {
        var bought = walletService.buyCrypto(input.getAssetID(), input.getAmount());

        return new ResponseEntity<>(bought, HttpStatus.OK);
    }

    @PostMapping("/sell")
    public ResponseEntity<SoldCryptoOutput> sellCrypto(@RequestBody @Valid CryptoInputDTO input) {
        var sold = walletService.sellCrypto(input.getAssetID(), input.getAmount());

        return new ResponseEntity<>(sold, HttpStatus.OK);
    }

    @GetMapping("/summary")
    public ResponseEntity<Set<GetWalletSummaryOutput>> getWalletSummary(@RequestParam(
            value = "asset_id", required = false) String assetId) {
        Set<GetWalletSummaryOutput> wallet = walletService.wallet_summary(assetId);

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @GetMapping("/summary/overall")
    public ResponseEntity<GetWalletOverallSummaryOutput> getWalletOverallSummary() {
        GetWalletOverallSummaryOutput wallet = walletService.wallet_overall_summary();

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionDTO>> getTransactionInfo(@RequestParam(value = "type", required = false) String type,
                                                             @RequestParam(value = "asset_id", required = false) String assetId) {
        var allowedTypes = Arrays.stream(TransactionType.values())
                .map(Enum::toString).toList();

        if (type != null && Collections.disjoint(allowedTypes, List.of(type))) {
            log.info("Invalid option for type: valid ones - bought/sold, chosen {}", type);
            throw new IllegalArgumentException("Invalid options for type: valid ones are bought/sold");
        }

        return new ResponseEntity<>(transactionMapper.
                transactionListToTransactionDTOList(walletService.
                        getTransactionHistory(type, assetId)), HttpStatus.OK);
    }

    @GetMapping("/history/period")
    public ResponseEntity<List<TransactionDTO>> getTransactionInfo(@RequestParam(value = "before", required = false) LocalDateTime before,
                                                                   @RequestParam(value = "after", required = false) LocalDateTime after) {

        return new ResponseEntity<>(transactionMapper.
                transactionListToTransactionDTOList(walletService.
                        getTransactionHistoryWithinPeriod(before, after)), HttpStatus.OK);
    }
}
