package com.lezrak.currencies.currency.exchange.rate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExchangeRateList {

    @JsonProperty("asset_id_base")
    private String assetIdBase;
    private List<ExchangeRate> rates;

    public String getAssetIdBase() {
        return assetIdBase;
    }

    public List<ExchangeRate> getRates() {
        return rates;
    }
}
