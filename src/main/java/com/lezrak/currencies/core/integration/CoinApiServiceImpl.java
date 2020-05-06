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
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;

@Service
public class CoinApiServiceImpl implements CoinApiService {

    private static final String THIRD_PARTY_NAME = "coinapi.io";
    private static final String URL_PREFIX = "https://rest.coinapi.io/v1/exchangerate/";
    private static final String HEADER_NAME = "X-CoinAPI-Key";
    private static final Logger LOG = LoggerFactory.getLogger(CoinApiServiceImpl.class);

    private final RestTemplate restTemplate;

    @Value("${coinapi.apikey}")
    private String HEADER_VALUE;

    public CoinApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ExchangeRate> getExchangeRateList(String currency) throws ExternalServiceException, CurrencyNotFoundException {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME, HEADER_VALUE);

        ExchangeRateList exchangeRateList;
        try {
            exchangeRateList = restTemplate.exchange(
                    URL_PREFIX + currency, GET, new HttpEntity(headers), ExchangeRateList.class)
                    .getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            LOG.error(String.format("Third party api authorization failed for: %s", THIRD_PARTY_NAME));
            throw new ExternalServiceException();
        }

        validate(exchangeRateList, currency);

        return exchangeRateList.getRates();
    }

    private void validate(ExchangeRateList exchangeRateList, String currency) {
        Optional.ofNullable(exchangeRateList)
                .map(ExchangeRateList::getRates)
                .orElseThrow(CoinApiServiceImpl::logAndThrow)
                .stream()
                .findAny()
                .orElseThrow(() -> new CurrencyNotFoundException(currency));
    }

    private static ExternalServiceException logAndThrow() {
        LOG.error(String.format("Third party api unexpected behaviour: %s", THIRD_PARTY_NAME));
        return new ExternalServiceException();
    }


}
