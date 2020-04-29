package com.lezrak.currencies.currency.coinapi;

import com.lezrak.currencies.currency.exchange.rate.ExchangeRate;
import com.lezrak.currencies.exception.CurrencyNotFoundException;

import java.util.List;

public interface CoinApiService {

    List<ExchangeRate> getExchangeRateList(String currency) throws CurrencyNotFoundException;

}
