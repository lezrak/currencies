package com.lezrak.currencies.core;

import com.lezrak.currencies.core.exchange.evaluation.ExchangeEvaluationList;
import com.lezrak.currencies.core.exchange.evaluation.ExchangeEvaluationListDTO;
import com.lezrak.currencies.core.exchange.rate.ExchangeRateListDTO;
import com.lezrak.currencies.exception.CurrencyNotFoundException;
import com.lezrak.currencies.exception.BlankCurrencyException;
import com.lezrak.currencies.exception.ExternalServiceException;
import com.lezrak.currencies.exception.WrongAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping("/{currency}")
    public ExchangeRateListDTO getRates(@PathVariable String currency, @RequestParam(name = "filter[]", required = false) Set<String> filter) {
        try {
            return currencyService.getRates(currency, filter);
        } catch (CurrencyNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (ExternalServiceException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }

    @PostMapping("/exchange")
    public ExchangeEvaluationListDTO evaluateExchange(@RequestBody ExchangeEvaluationList exchangeEvaluationList) {
        try {
            return currencyService.evaluateExchange(exchangeEvaluationList);
        } catch (CurrencyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ExternalServiceException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        } catch (WrongAmountException | BlankCurrencyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
