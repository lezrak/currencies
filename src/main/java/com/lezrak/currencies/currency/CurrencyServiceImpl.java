package com.lezrak.currencies.currency;

import com.lezrak.currencies.currency.coinapi.CoinApiService;
import com.lezrak.currencies.currency.exchange.evaluation.ExchangeEvaluation;
import com.lezrak.currencies.currency.exchange.evaluation.ExchangeEvaluationRequest;
import com.lezrak.currencies.currency.exchange.evaluation.ExchangeEvaluationResponse;
import com.lezrak.currencies.currency.exchange.rate.ExchangeRate;
import com.lezrak.currencies.currency.exchange.rate.ExchangeRateListDTO;
import com.lezrak.currencies.exception.CurrencyNotFoundException;
import com.lezrak.currencies.exception.BlankCurrencyException;
import com.lezrak.currencies.exception.ThirdPartyApiException;
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
    public ExchangeRateListDTO getRates(String currency, List<String> filter) throws CurrencyNotFoundException, ThirdPartyApiException {

        List<ExchangeRate> ratesList = coinApiService.getExchangeRateList(currency);

        checkFilterCorrectness(filter, ratesList);

        ratesList.forEach(o -> o.setRate(o.getRate().setScale(SCALE, ROUNDING_MODE)));

        Map<String, BigDecimal> rates;
        if (filter != null) {
            HashSet filterSet = new HashSet<>(filter);
            rates = ratesList.stream()
                    .filter(exchangeRate -> filterSet.contains(exchangeRate.getAsset_id_quote()))
                    .collect(Collectors.toMap(ExchangeRate::getAsset_id_quote, ExchangeRate::getRate));
        } else {
            rates = ratesList.stream()
                    .collect(Collectors.toMap(ExchangeRate::getAsset_id_quote, ExchangeRate::getRate));
        }

        return new ExchangeRateListDTO(currency, rates);
    }


    @Override
    public ExchangeEvaluationResponse evaluateExchange(ExchangeEvaluationRequest exchangeEvaluationRequest) throws ThirdPartyApiException, CurrencyNotFoundException, WrongAmountException, BlankCurrencyException {

        checkExchangeEvaluationRequestCorrectness(exchangeEvaluationRequest);

        ExchangeRateListDTO rates = getRates(exchangeEvaluationRequest.getFrom(), exchangeEvaluationRequest.getTo());

        Map<String, ExchangeEvaluation> to = rates.getRates().entrySet().parallelStream()
                .map(entry -> new ExchangeEvaluation(entry.getKey(), entry.getValue(), exchangeEvaluationRequest.getAmount()))
                .map(ExchangeEvaluation::evaluate)
                .collect(Collectors.toMap(ExchangeEvaluation::getCurrency, Function.identity()));

        return new ExchangeEvaluationResponse(exchangeEvaluationRequest.getFrom(), to);
    }

    private void checkExchangeEvaluationRequestCorrectness(ExchangeEvaluationRequest exchangeEvaluationRequest) throws BlankCurrencyException, WrongAmountException {
        if (exchangeEvaluationRequest.getFrom() == null || exchangeEvaluationRequest.getFrom().trim().length() == 0) {
            throw new BlankCurrencyException();
        }
        if (exchangeEvaluationRequest.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            throw new WrongAmountException(exchangeEvaluationRequest.getAmount().toString());
        }
    }


    private void checkFilterCorrectness(List<String> filter, List<ExchangeRate> exchangeRateList) throws CurrencyNotFoundException {
        if (filter != null) {
            Set<String> currencies = exchangeRateList.stream()
                    .map(ExchangeRate::getAsset_id_quote)
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