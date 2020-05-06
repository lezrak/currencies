package com.lezrak.currencies.core;

import com.lezrak.currencies.core.exchange.evaluation.ExchangeEvaluationList;
import com.lezrak.currencies.core.exchange.evaluation.ExchangeEvaluationListDTO;
import com.lezrak.currencies.core.exchange.rate.ExchangeRateListDTO;
import com.lezrak.currencies.exception.CurrencyNotFoundException;
import com.lezrak.currencies.exception.BlankCurrencyException;
import com.lezrak.currencies.exception.ExternalServiceException;
import com.lezrak.currencies.exception.WrongAmountException;

import java.util.List;

public interface CurrencyService {

    ExchangeRateListDTO getRates(String currency, List<String> filter) throws CurrencyNotFoundException, ExternalServiceException;

    ExchangeEvaluationListDTO evaluateExchange(ExchangeEvaluationList exchangeEvaluationList) throws ExternalServiceException, CurrencyNotFoundException, WrongAmountException, BlankCurrencyException;
}
