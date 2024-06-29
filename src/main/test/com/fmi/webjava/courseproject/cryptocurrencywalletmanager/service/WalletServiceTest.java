package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.CryptoInformation;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.BoughtCryptoOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.GetWalletOverallSummaryOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.GetWalletSummaryOutput;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private CryptoRepository cryptoRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserCryptoRepository userCryptoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CryptoMapper cryptoMapper;
    @Mock
    private CoinApiService coinApiService;

    @InjectMocks
    private WalletServiceImpl walletService;

//    private User user;
//    private CryptoInformation cryptoInfo;
//    private UserCrypto userCrypto;
//    private Crypto crypto;
//    private Set<CryptoInformation> cryptoInfoList;
//    private SecurityContext securityContext;


    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder()
                                .id(1L)
                                .userName("testUser")
                                .password("password")
                                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                                .build(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );
    }

    @Test
    void testDepositMoney() {
        User user = User.builder()
                .id(1L)
                .userName("testUser")
                .money(100.0)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = walletService.depositMoney(50.0);

        assertNotNull(updatedUser, "Updated user should not be null");
        assertEquals(150.0, updatedUser.getMoney(), "User's money should be updated correctly");
        verify(userRepository).save(user);
    }

    @Test
    void testDepositMoneyUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> walletService.depositMoney(50.0),
                "Exception was expected to be thrown when depositing money to unknown user");
    }

    @Test
    void testListOfferings() {
        Set<CryptoInformation> cryptoSet = Set.of(
                new CryptoInformation("BTC", "Bitcoin",1, 60000.0),
                new CryptoInformation("ETH", "Ethereum", 1, 10000.0)
        );

        when(coinApiService.getCrypto()).thenReturn(cryptoSet);

        Set<CryptoInformation> result = walletService.listOfferings(null);

        assertNotNull(result, "Result should not be null");
        assertEquals(cryptoSet.size(), result.size(), String.format("Result size is invalid, expected %d, got %d", cryptoSet.size(), result.size()));
        assertTrue(cryptoSet.containsAll(result), "Returned assets differ from expected one");
        assertTrue(result.containsAll(cryptoSet), "Returned assets differ from expected one");
    }

    @Test
    void testListOfferingsCryptoOnly() {
        Set<CryptoInformation> cryptoSet = new HashSet<>(Set.of(
                new CryptoInformation("BTC", "Bitcoin", 1, 60000.0),
                new CryptoInformation("ETH", "Ethereum", 1, 10000.0),
                new CryptoInformation("BGN", "Bulgarian lev", 0, 0.96)
        ));

        when(coinApiService.getCrypto()).thenReturn(cryptoSet);

        Set<CryptoInformation> result = walletService.listOfferings("crypto");

        assertNotNull(result, "Result should not be null");
        cryptoSet.removeIf(asset -> asset.isCrypto() == 0);
        assertEquals(cryptoSet.size(), result.size(), String.format("Result size is invalid, expected %d, got %d", cryptoSet.size(), result.size()));
        assertTrue(cryptoSet.containsAll(result), "Returned assets differ from expected one");
        assertTrue(result.containsAll(cryptoSet), "Returned assets differ from expected one");
    }

    @Test
    void testListOfferingsCoinsOnly() {
        Set<CryptoInformation> cryptoSet = new HashSet<>(Set.of(
                new CryptoInformation("BTC", "Bitcoin", 1, 60000.0),
                new CryptoInformation("ETH", "Ethereum", 1, 10000.0),
                new CryptoInformation("BGN", "Bulgarian lev", 0, 0.96)
        ));

        when(coinApiService.getCrypto()).thenReturn(cryptoSet);

        Set<CryptoInformation> result = walletService.listOfferings("coins");

        assertNotNull(result, "Result should not be null");
        cryptoSet.removeIf(asset -> asset.isCrypto() == 1);
        assertEquals(cryptoSet.size(), result.size(), String.format("Result size is invalid, expected %d, got %d", cryptoSet.size(), result.size()));
        assertTrue(cryptoSet.containsAll(result), "Returned assets differ from expected one");
        assertTrue(result.containsAll(cryptoSet), "Returned assets differ from expected one");
    }

    @Test
    void testListOfferingsCertainAsset() {
        String assetID = "BTC";
        CryptoInformation cryptoInfo = new CryptoInformation(assetID, "Bitcoin",1, 60000.0);
        when(coinApiService.getCrypto()).thenReturn(Set.of(cryptoInfo));

        CryptoInformation result = walletService.listOfferingsCertainAsset(assetID);

        assertNotNull(result, "Result should not be null");
        assertEquals(assetID, result.assetID(), "Asset ID should match the expected ID");
    }

    @Test
    void testListOfferingsCertainAssetNotFound() {
        Set<CryptoInformation> cryptoSet = new HashSet<>(Set.of(
                new CryptoInformation("BTC", "Bitcoin", 1, 60000.0),
                new CryptoInformation("ETH", "Ethereum", 1, 10000.0),
                new CryptoInformation("BGN", "Bulgarian lev", 0, 0.96)
        ));
        when(coinApiService.getCrypto()).thenReturn(cryptoSet);

        assertThrows(AssetNotFoundException.class, () -> walletService.listOfferingsCertainAsset("DOGE"),
                "Expected AssetNotFoundException when listing unknown asset");
    }

    @Test
    void testBuyCryptoShouldReturnBoughtCryptoOutput() {
        CryptoInformation crypto = new CryptoInformation("BTC", "Bitcoin", 1, 60.000);
        User user = User.builder().id(1L).userName("testUser").money(100.000).build();
        Crypto cryptoEntity = Crypto.builder().name("Bitcoin").price(60.000).build();

        when(coinApiService.getCrypto()).thenReturn(Set.of(crypto));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cryptoRepository.findByNameAndPrice("BTC", 60.000)).thenReturn(Optional.of(cryptoEntity));
        when(userCryptoRepository.findById(any(UserCryptoId.class))).thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        BoughtCryptoOutput output = walletService.buyCrypto("BTC", 1.0);

        assertEquals("testUser", output.user(), "User name should match");
        assertEquals("BTC", output.cryptoID(), "Asset ID should match");
        assertEquals(1.0, output.amount(), "Amount should match");
        assertEquals(40.000, user.getMoney(), "Use's amount of money should match");
    }

    @Test
    void testBuyMoreCryptoFromAlreadyBought() {
        CryptoInformation crypto = new CryptoInformation("BTC", "Bitcoin", 1, 60000.0);
        User user = User.builder().id(1L).userName("testUser").money(100000.0).build();
        Crypto cryptoEntity = Crypto.builder().name("Bitcoin").price(60000.0).build();
        UserCrypto userCrypto = UserCrypto.builder().id(new UserCryptoId(1L,"BTC"))
                        .amount(2.0)
                        .averageCryptoBuyingPrice(30000.0)
                        .user(user)
                        .build();

        when(coinApiService.getCrypto()).thenReturn(Set.of(crypto));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cryptoRepository.findByNameAndPrice("BTC", 60000.0)).thenReturn(Optional.of(cryptoEntity));
        when(userCryptoRepository.findById(any(UserCryptoId.class))).thenReturn(Optional.of(userCrypto));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BoughtCryptoOutput output = walletService.buyCrypto("BTC", 1.0);
        double expectedAvgBuyingPrice = (60000 * 1.0 +
                (30000 * 2)) / (2 + 1);

        assertEquals("testUser", output.user(), "User name should match");
        assertEquals("BTC", output.cryptoID(), "Asset ID should match");
        assertEquals(1.0, output.amount(), "Amount should match");
        assertEquals(40000.0, user.getMoney(), "Use's amount of money should match");
        assertEquals(3, userCrypto.getAmount(), "UserCrypto Should be update after buying new amount");
        assertEquals(expectedAvgBuyingPrice, userCrypto.getAverageCryptoBuyingPrice(), "AvgBuyingPrice is incorrect");
    }

    @Test
    void testBuyCryptoInsufficientFunds() {
        CryptoInformation crypto = new CryptoInformation("BTC", "Bitcoin", 1, 60.000);
        User user = User.builder().id(1L).userName("testUser").money(10.0).build();

        when(coinApiService.getCrypto()).thenReturn(Set.of(crypto));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(InsufficientFundsException.class, () -> walletService.buyCrypto("BTC", 0.5),
                "Should throw InsufficientFundsException when the user has insufficient funds");
    }

    @Test
    void testSellCryptoSuccessfully() {
        CryptoInformation crypto = new CryptoInformation("BTC", "Bitcoin", 1, 60000);
        User user = User.builder().id(1L).userName("testUser").money(100.0).overallTransactionsProfit(0.0).build();
        UserCrypto userCrypto = UserCrypto.builder()
                .amount(2.0)
                .averageCryptoBuyingPrice(30000.0)
                .id(new UserCryptoId(1L, "BTC"))
                .user(user)
                .build();
        Crypto cryptoEntity = Crypto.builder().name("Bitcoin").price(60000.0).build();

        when(coinApiService.getCrypto()).thenReturn(Set.of(crypto));
        when(userCryptoRepository.findByCryptoNameAndUserId("BTC", 1L)).thenReturn(Optional.of(userCrypto));
        when(cryptoRepository.findByNameAndPrice("BTC", 60000.0)).thenReturn(Optional.of(cryptoEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        SoldCryptoOutput output = walletService.sellCrypto("BTC", 1.0);
        double expectedProfit = 1*(60000-30000);

        assertEquals("testUser", output.user(), "User name should match");
        assertEquals("BTC", output.cryptoID(), "Asset ID should match");
        assertEquals(1.0, output.amount(), "Amount should match");
        assertEquals(expectedProfit,user.getOverallTransactionsProfit(), "Profit is incorrect");
    }

    @Test
    void testSellCryptoWithoutBuying() {
        when(coinApiService.getCrypto()).thenReturn(Set.of(new CryptoInformation("BTC", "Bitcoin", 1, 60000)));
        when(userCryptoRepository.findByCryptoNameAndUserId("BTC", 1L)).thenReturn(Optional.empty());

        assertThrows(AssetNotFoundException.class, () -> walletService.sellCrypto("BTC", 1.0),
                "Should throw AssetNotFoundException when the user does not own the asset");
    }

    @Test
    void testSellCryptoMoreThanAvailable() {
        CryptoInformation crypto = new CryptoInformation("BTC", "Bitcoin", 1, 60000);
        User user = User.builder().id(1L).userName("testUser").money(100.0).overallTransactionsProfit(0.0).build();
        UserCrypto userCrypto = UserCrypto.builder()
                .amount(2.0)
                .averageCryptoBuyingPrice(30000.0)
                .id(new UserCryptoId(1L, "BTC"))
                .user(user)
                .build();

        when(coinApiService.getCrypto()).thenReturn(Set.of(crypto));
        when(userCryptoRepository.findByCryptoNameAndUserId("BTC", 1L)).thenReturn(Optional.of(userCrypto));

        assertThrows(IllegalArgumentException.class, () -> walletService.sellCrypto("BTC", 5.0),
                "Should throw AssetNotFoundException when the user does not own the asset");
    }

    @Test
    void testWalletSummaryNoAssetsBought() {
        when(userCryptoRepository.findByUserId(1L)).thenReturn(Optional.empty());

        Set<GetWalletSummaryOutput> summary = walletService.wallet_summary(null);

        assertTrue(summary.isEmpty(), "Should return an empty set when the user has no assets");
    }

    @Test
    void testWalletSummaryWithMultipleAssets() {
        User user = User.builder().id(1L).userName("testUser").build();
        CryptoInformation BTC = new CryptoInformation("BTC", "Bitcoin", 1, 30000);
        CryptoInformation ETH = new CryptoInformation("ETH", "Ethereum", 1, 2000);

        UserCrypto userCrypto1 = UserCrypto.builder()
                .amount(1.0)
                .averageCryptoBuyingPrice(25000.0)
                .id(new UserCryptoId(1L, "BTC"))
                .user(user)
                .build();

        UserCrypto userCrypto2 = UserCrypto.builder()
                .amount(5.0)
                .averageCryptoBuyingPrice(1500.0)
                .id(new UserCryptoId(1L, "ETH"))
                .user(user)
                .build();

        when(userCryptoRepository.findByUserId(1L)).thenReturn(Optional.of(Set.of(userCrypto1, userCrypto2)));
        when(coinApiService.getCrypto()).thenReturn(Set.of(BTC, ETH));

        Set<GetWalletSummaryOutput> summary = walletService.wallet_summary(null);

        assertEquals(2, summary.size(), "Should return all assets owned by the user");

        for (GetWalletSummaryOutput output : summary) {
            if (output.asset().equals("Bitcoin")) {
                assertEquals(1.0, output.amount(), "Bitcoin amount should match");
                assertEquals(30000.0, output.current_price(), "Bitcoin current price should match");
            } else if (output.asset().equals("Ethereum")) {
                assertEquals(5.0, output.amount(), "Ethereum amount should match");
                assertEquals(2000.0, output.current_price(), "Ethereum current price should match");
            }
        }
    }

    @Test
    void testWalletSummaryForCertainAsset() {
        User user = User.builder().id(1L).userName("testUser").build();
        CryptoInformation BTC = new CryptoInformation("BTC", "Bitcoin", 1, 30000);
        CryptoInformation ETH = new CryptoInformation("ETH", "Ethereum", 1, 2000);

        UserCrypto userCrypto1 = UserCrypto.builder()
                .amount(1.0)
                .averageCryptoBuyingPrice(25000.0)
                .id(new UserCryptoId(1L, "BTC"))
                .user(user)
                .build();

        UserCrypto userCrypto2 = UserCrypto.builder()
                .amount(5.0)
                .averageCryptoBuyingPrice(1500.0)
                .id(new UserCryptoId(1L, "ETH"))
                .user(user)
                .build();

        when(userCryptoRepository.findByCryptoNameAndUserId("BTC", 1L)).thenReturn(Optional.of(userCrypto1));
        when(coinApiService.getCrypto()).thenReturn(Set.of(BTC, ETH));

        Set<GetWalletSummaryOutput> summary = walletService.wallet_summary("BTC");

        assertEquals(1, summary.size(), "Should return all assets owned by the user");

        for (GetWalletSummaryOutput output : summary) {
            if (output.asset().equals("Bitcoin")) {
                assertEquals(1.0, output.amount(), "Bitcoin amount should match");
                assertEquals(30000.0, output.current_price(), "Bitcoin current price should match");
            }
        }
    }

    @Test
    void testWalletSummaryWithoutAssets() {
        User user = User.builder().id(1L).userName("testUser").overallTransactionsProfit(0.0).money(0.0).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userCryptoRepository.findByUserId(1L)).thenReturn(Optional.empty());

        GetWalletOverallSummaryOutput summary = walletService.wallet_overall_summary();

        assertEquals("testUser", summary.username(), "User name should match");
        assertEquals(0.0, summary.overall_profit(), "Overall profit should be 0");
        assertTrue(summary.active_assets().isEmpty(), "Active assets must be empty");
    }

    @Test
    void testWalletOverallSummary() {
        User user = User.builder().id(1L).userName("testUser").overallTransactionsProfit(3000.0).money(500.0).build();
        Crypto bitcoin = Crypto.builder().name("BTC").price(30000.0).build();
        CryptoInformation BTC = new CryptoInformation("BTC", "Bitcoin", 1, 30000);

        UserCrypto userCrypto = UserCrypto.builder()
                .amount(2.0)
                .averageCryptoBuyingPrice(25000.0)
                .id(new UserCryptoId(1L, "BTC"))
                .user(user)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userCryptoRepository.findByUserId(1L)).thenReturn(Optional.of(Set.of(userCrypto)));
        when(coinApiService.getCrypto()).thenReturn(Set.of(BTC));

        GetWalletOverallSummaryOutput summary = walletService.wallet_overall_summary();

        assertEquals("testUser", summary.username(), "User name should match");
        assertEquals(500.0, summary.money(), "User's money should match");
        assertEquals(3000.0, summary.overall_profit(), "Overall profit should match");
    }

    @Test
    void testGetTransactionsNoTransactions() {
        when(transactionRepository.findAllTransactionsForUser(1L)).thenReturn(Collections.emptyList());

        List<Transaction> transactions = walletService.getTransactionHistory(null, null);

        assertTrue(transactions.isEmpty(), "Should return an empty list when there are no transactions");
    }

    @Test
    void testGetTransactionHistory() {
        var user = User.builder().id(1L).userName("testUser").build();

        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .user(user)
                .crypto(Crypto.builder().name("BTC").build())
                .amount(1.0)
                .type("BOUGHT")
                .dateOfCommit(LocalDateTime.now())
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .user(user)
                .crypto(Crypto.builder().name("ETH").build())
                .amount(2.0)
                .type("SOLD")
                .dateOfCommit(LocalDateTime.now())
                .build();

        when(transactionRepository.findAllTransactionsForUser(1L)).thenReturn(List.of(transaction1, transaction2));

        List<Transaction> transactions = walletService.getTransactionHistory(null, null);

        assertEquals(2, transactions.size(), "Incorrect number of transactions");
        assertIterableEquals(List.of(transaction1, transaction2), transactions, "Error: should return all transactions");
    }

    @Test
    void testGetTransactionHistoryForCertainAsset() {
        var user = User.builder().id(1L).userName("testUser").build();

        CryptoInformation BTC = new CryptoInformation("BTC", "Bitcoin", 1, 30000);
        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .user(user)
                .crypto(Crypto.builder().name("BTC").build())
                .amount(1.0)
                .type("BOUGHT")
                .dateOfCommit(LocalDateTime.now())
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .user(user)
                .crypto(Crypto.builder().name("ETH").build())
                .amount(2.0)
                .type("SOLD")
                .dateOfCommit(LocalDateTime.now())
                .build();

        when(coinApiService.getCrypto()).thenReturn(Set.of(BTC));
        when(transactionRepository.findTransactionByAssetId(1L, "BTC")).thenReturn(List.of(transaction1));

        List<Transaction> transactions = walletService.getTransactionHistory(null, "BTC");

        assertEquals(1, transactions.size(), "Incorrect number of transactions");
        assertIterableEquals(List.of(transaction1), transactions, "Error: should return all transactions");
    }

    @Test
    void testGetTransactionsWithinPeriodNoTransactionsFound() {
        LocalDateTime start = LocalDateTime.of(2024, 6, 27, 17, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 29, 23, 59);

        when(transactionRepository.findTransactionBetween(1L, start, end)).thenReturn(Collections.emptyList());

        List<Transaction> transactions = walletService.getTransactionHistoryWithinPeriod(start, end);

        assertTrue(transactions.isEmpty(), "Should return an empty list when there are no transactions in the period");
    }

    @Test
    void testGetAllTransactionsWithinPeriod() {
        var user = User.builder().id(1L).userName("testUser").build();
        LocalDateTime start = LocalDateTime.of(2024, 6, 27, 17, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 29, 23, 59);

        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .user(user)
                .crypto(Crypto.builder().name("BTC").build())
                .amount(1.0)
                .type("BOUGHT")
                .dateOfCommit(LocalDateTime.of(2024, 6, 27, 18, 33))
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .user(user)
                .crypto(Crypto.builder().name("ETH").build())
                .amount(2.0)
                .type("SOLD")
                .dateOfCommit(LocalDateTime.of(2024, 3, 1, 0, 0))
                .build();

        when(transactionRepository.findTransactionBetween(1L, start, end)).thenReturn(List.of(transaction1));

        List<Transaction> transactions = walletService.getTransactionHistoryWithinPeriod(start, end);

        assertEquals(1, transactions.size(), "Should return all transactions within the specified period");
        assertIterableEquals(List.of(transaction1), transactions, "Should return all transactions within the specified period");
    }

    @Test
    void testGetAllTransactionsBeforeDate() {
        var user = User.builder().id(1L).userName("testUser").build();

        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .user(user)
                .crypto(Crypto.builder().name("BTC").build())
                .amount(1.0)
                .type("BOUGHT")
                .dateOfCommit(LocalDateTime.of(2024, 6, 27, 18, 33))
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .user(user)
                .crypto(Crypto.builder().name("ETH").build())
                .amount(2.0)
                .type("SOLD")
                .dateOfCommit(LocalDateTime.of(2024, 3, 1, 0, 0))
                .build();

        LocalDateTime date = LocalDateTime.of(2024, 6, 15, 23, 59);
        when(transactionRepository.findTransactionBefore(1L,date)).thenReturn(List.of(transaction2));

        List<Transaction> transactions = walletService.getTransactionHistoryWithinPeriod(date, null);

        assertEquals(1, transactions.size(), "Should return all transactions within the specified period");
        assertIterableEquals(List.of(transaction2), transactions, "Should return all transactions within the specified period");
    }
}