package com.lezrak.currencies.core;

import com.lezrak.currencies.core.integration.CoinApiService;
import com.lezrak.currencies.core.exchange.evaluation.ExchangeEvaluation;
import com.lezrak.currencies.core.exchange.evaluation.ExchangeEvaluationList;
import com.lezrak.currencies.core.exchange.evaluation.ExchangeEvaluationListDTO;
import com.lezrak.currencies.core.exchange.rate.ExchangeRate;
import com.lezrak.currencies.core.exchange.rate.ExchangeRateListDTO;
import com.lezrak.currencies.exception.CurrencyNotFoundException;
import com.lezrak.currencies.exception.BlankCurrencyException;
import com.lezrak.currencies.exception.ExternalServiceException;
import com.lezrak.currencies.exception.WrongAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    public final static int SCALE = 16;
    public final static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public final static BigDecimal PROVISION = BigDecimal.valueOf(0.01);

    private final CoinApiService coinApiService;

    @Autowired
    public CurrencyServiceImpl(CoinApiService coinApiService) {
        this.coinApiService = coinApiService;
    }


    @Override
    public ExchangeRateListDTO getRates(String currency, List<String> filter) throws CurrencyNotFoundException, ExternalServiceException {

        List<ExchangeRate> ratesList = coinApiService.getExchangeRateList(currency);

        validate(filter, ratesList);

        ratesList.forEach(o -> o.setRate(o.getRate().setScale(SCALE, ROUNDING_MODE)));

        Map<String, BigDecimal> rates;
        if (filter != null) {
            Set filterSet = new HashSet<>(filter);
            rates = ratesList.stream()
                    .filter(exchangeRate -> filterSet.contains(exchangeRate.getAssetIdQuote()))
                    .collect(toMap(ExchangeRate::getAssetIdQuote, ExchangeRate::getRate));
        } else {
            rates = ratesList.stream()
                    .collect(toMap(ExchangeRate::getAssetIdQuote, ExchangeRate::getRate));
        }

        return new ExchangeRateListDTO(currency, rates);
    }


    @Override
    public ExchangeEvaluationListDTO evaluateExchange(ExchangeEvaluationList exchangeEvaluationList) throws ExternalServiceException, CurrencyNotFoundException, WrongAmountException, BlankCurrencyException {

        validate(exchangeEvaluationList);

        ExchangeRateListDTO rates = getRates(exchangeEvaluationList.getFrom(), exchangeEvaluationList.getTo());

        Map<String, ExchangeEvaluation> to = rates.getRates().entrySet().parallelStream()
                .map(entry -> new ExchangeEvaluation(entry.getKey(), entry.getValue(), exchangeEvaluationList.getAmount()))
                .map(ExchangeEvaluation::evaluate)
                .collect(toMap(ExchangeEvaluation::getCurrency, Function.identity()));

        return new ExchangeEvaluationListDTO(exchangeEvaluationList.getFrom(), to);
    }

    private void validate(ExchangeEvaluationList exchangeEvaluationList) throws BlankCurrencyException, WrongAmountException {
        if (exchangeEvaluationList.getFrom() == null || exchangeEvaluationList.getFrom().trim().length() == 0) {
            throw new BlankCurrencyException();
        }
        if (exchangeEvaluationList.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            throw new WrongAmountException(exchangeEvaluationList.getAmount().toString());
        }
    }


    private void validate(List<String> filter, List<ExchangeRate> exchangeRateList) throws CurrencyNotFoundException {
        if (filter != null) {
            Set<String> currencies = exchangeRateList.stream()
                    .map(ExchangeRate::getAssetIdQuote)
                    .collect(Collectors.toSet());

            List<String> notFoundCurrencies = filter.stream()
                    .filter(s -> !currencies.contains(s))
                    .collect(Collectors.toList());

            if (notFoundCurrencies.size() > 1) {
                throw new CurrencyNotFoundException(notFoundCurrencies.toString());
            } else if (notFoundCurrencies.size() == 1) {
                throw new CurrencyNotFoundException(notFoundCurrencies.get(0));
            }
        }
    }


}
