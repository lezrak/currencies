package com.lezrak.currencies.currency;

import com.lezrak.currencies.currency.exchange.evaluation.ExchangeEvaluationRequest;
import com.lezrak.currencies.currency.exchange.evaluation.ExchangeEvaluationResponse;
import com.lezrak.currencies.currency.exchange.rate.ExchangeRateListDTO;
import com.lezrak.currencies.exception.CurrencyNotFoundException;
import com.lezrak.currencies.exception.BlankCurrencyException;
import com.lezrak.currencies.exception.ThirdPartyApiException;
import com.lezrak.currencies.exception.WrongAmountException;

import java.util.List;

public interface CurrencyService {

    ExchangeRateListDTO getRates(String currency, List<String> filter) throws CurrencyNotFoundException, ThirdPartyApiException;

    ExchangeEvaluationResponse evaluateExchange(ExchangeEvaluationRequest exchangeEvaluationRequest) throws ThirdPartyApiException, CurrencyNotFoundException, WrongAmountException, BlankCurrencyException;
}
