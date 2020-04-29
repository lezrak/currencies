package com.lezrak.currencies.core.exchange.rate;


import java.math.BigDecimal;
import java.util.Map;


public class ExchangeRateListDTO {

    private final String source;
    private final Map<String, BigDecimal> rates;


    public ExchangeRateListDTO(String source, Map<String, BigDecimal> rates) {
        this.source = source;
        this.rates = rates;
    }

    public String getSource() {
        return source;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }
}
