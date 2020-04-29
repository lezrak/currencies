package com.lezrak.currencies.currency.exchange.evaluation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

import static com.lezrak.currencies.currency.CurrencyServiceImpl.*;

public class ExchangeEvaluation {

    @JsonIgnore
    private String currency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal result;
    private BigDecimal fee;

    public ExchangeEvaluation() {
    }

    public ExchangeEvaluation(String currency, BigDecimal rate, BigDecimal amount) {
        this.currency = currency;
        this.rate = rate;
        this.amount = amount;
    }

    public ExchangeEvaluation evaluate() {
        fee = amount.multiply(PROVISION).setScale(SCALE, ROUNDING_MODE);
        result = amount.subtract(fee).multiply(rate).setScale(SCALE, ROUNDING_MODE);
        amount = amount.setScale(SCALE, ROUNDING_MODE);
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getResult() {
        return result;
    }

    public BigDecimal getFee() {
        return fee;
    }
}
