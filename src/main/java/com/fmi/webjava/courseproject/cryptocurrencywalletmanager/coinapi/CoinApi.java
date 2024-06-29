package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.exception.BadRequestToAPIException;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service.CoinApiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CoinApi {

    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "rest.coinapi.io";
    private static final String API_ENDPOINT_PATH = "/v1/assets";
    private static final String API_HEADER_TEXT = "X-CoinAPI-Key";
    private static final String API_KEY = ""; //Set your api key

    private static final Integer MAX_CRYPTOCURRENCIES = 200;
    private static final Gson GSON = new Gson();

    @Autowired
    private CoinApiService coinApiService;

    public String callCoinApi() {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH, null);
            HttpGet request = new HttpGet(uri);
            request.setHeader(API_HEADER_TEXT, API_KEY);

            return httpClient.execute(request, res -> {
                if (res.getCode() == HttpStatus.SC_OK) {
                    log.info("Successful request to the Coin API");
                    return EntityUtils.toString(res.getEntity(), "UTF-8");
                }

                log.info("Error: Bad request to the Coin API");
                throw new BadRequestToAPIException("There is a problem with your request to coin api");
            });

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    @Scheduled(fixedRateString = "5", timeUnit = TimeUnit.MINUTES)
    @SchedulerLock(name = "CoinApi", lockAtLeastFor = "PT5M", lockAtMostFor = "PT6M")
    public void getCryptocurrencies() {
        String response = callCoinApi();

        Type cryptoListType = new TypeToken<Set<CryptoInformation>>() { }.getType();
        Set<CryptoInformation> cryptoInformationSet = GSON.fromJson(response, cryptoListType);

        Set<CryptoInformation> CryptoInfo = cryptoInformationSet.stream()
                .filter(e -> e.price() != 0)
                .limit(MAX_CRYPTOCURRENCIES)
                .collect(Collectors.toSet());

        coinApiService.setCrypto(CryptoInfo);
        log.info("Extracting asset information from coin API");
    }
}
