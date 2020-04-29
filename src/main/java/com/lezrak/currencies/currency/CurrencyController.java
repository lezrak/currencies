package com.lezrak.currencies.currency;

import com.lezrak.currencies.currency.exchange.evaluation.ExchangeEvaluationRequest;
import com.lezrak.currencies.currency.exchange.evaluation.ExchangeEvaluationResponse;
import com.lezrak.currencies.currency.exchange.rate.ExchangeRateListDTO;
import com.lezrak.currencies.exception.CurrencyNotFoundException;
import com.lezrak.currencies.exception.BlankCurrencyException;
import com.lezrak.currencies.exception.ThirdPartyApiException;
import com.lezrak.currencies.exception.WrongAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping("/{currency}")
    public ExchangeRateListDTO getRates(@PathVariable String currency, @RequestParam(name = "filter[]", required = false) List<String> filter) {
        try {
            return currencyService.getRates(currency, filter);
        } catch (CurrencyNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (ThirdPartyApiException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }

    @PostMapping("/exchange")
    public ExchangeEvaluationResponse evaluateExchange(@RequestBody ExchangeEvaluationRequest exchangeEvaluationRequest) {
        try {
            return currencyService.evaluateExchange(exchangeEvaluationRequest);
        } catch (CurrencyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ThirdPartyApiException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        } catch (WrongAmountException | BlankCurrencyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}