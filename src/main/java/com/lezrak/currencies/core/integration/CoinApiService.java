package com.lezrak.currencies.core.integration;

import com.lezrak.currencies.core.exchange.rate.ExchangeRate;
import com.lezrak.currencies.exception.CurrencyNotFoundException;
import com.lezrak.currencies.exception.ExternalServiceException;

import java.util.List;

public interface CoinApiService {

    List<ExchangeRate> getExchangeRateList(String currency) throws ExternalServiceException, CurrencyNotFoundException;

}
