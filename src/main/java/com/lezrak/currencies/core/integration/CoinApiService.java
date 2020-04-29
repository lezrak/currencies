package com.lezrak.currencies.core.integration;

import com.lezrak.currencies.core.exchange.rate.ExchangeRate;
import com.lezrak.currencies.exception.CurrencyNotFoundException;

import java.util.List;

public interface CoinApiService {

    List<ExchangeRate> getExchangeRateList(String currency) throws CurrencyNotFoundException;

}