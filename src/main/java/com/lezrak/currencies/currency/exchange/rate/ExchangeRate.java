package com.lezrak.currencies.currency.exchange.rate;

import java.math.BigDecimal;

public class ExchangeRate {

    private String asset_id_quote;
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public String getAsset_id_quote() {
        return asset_id_quote;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
