package com.lezrak.currencies.core.integration;

import com.lezrak.currencies.core.exchange.rate.ExchangeRate;
import com.lezrak.currencies.core.exchange.rate.ExchangeRateList;
import com.lezrak.currencies.exception.CurrencyNotFoundException;
import com.lezrak.currencies.exception.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CoinApiServiceImpl implements CoinApiService {

    private static final String THIRD_PARTY_NAME = "coinapi.io";
    private static final String URL_PREFIX = "https://rest.coinapi.io/v1/exchangerate/";
    private static final String HEADER_NAME = "X-CoinAPI-Key";

    @Value("${coinapi.apikey}")
    private String HEADER_VALUE;
    private Logger logger = LoggerFactory.getLogger(CoinApiServiceImpl.class);

    @Override
    public List<ExchangeRate> getExchangeRateList(String currency) throws ExternalServiceException, CurrencyNotFoundException {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME, HEADER_VALUE);

        ResponseEntity<ExchangeRateList> response;
        try {
            response = new RestTemplate().exchange(
                    URL_PREFIX + currency, HttpMethod.GET, new HttpEntity(headers), ExchangeRateList.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.error(String.format("Third party api authorization failed for: %s", THIRD_PARTY_NAME));
            throw new ExternalServiceException();
        }

        ExchangeRateList exchangeRateList = response.getBody();

        evaluateExchangeRateList(exchangeRateList, currency);

        return exchangeRateList.getRates();
    }

    private void evaluateExchangeRateList(ExchangeRateList exchangeRateList, String currency) {
        if (exchangeRateList == null) {
            logger.error(String.format("Third party api unexpected behaviour: %s", THIRD_PARTY_NAME));
            throw new ExternalServiceException();
        }

        if (exchangeRateList.getRates().size() == 0) {
            throw new CurrencyNotFoundException(currency);
        }
    }


}
