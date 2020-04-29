package com.lezrak.currencies.currency.exchange.rate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ExchangeRate {

    @JsonProperty("asset_id_quote")
    private String assetIdQuote;
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public String getAssetIdQuote() {
        return assetIdQuote;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
