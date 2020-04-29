package com.lezrak.currencies.currency.exchange.rate;

import java.util.List;

public class ExchangeRateList {

    private String asset_id_base;
    private List<ExchangeRate> rates;

    public String getAsset_id_base() {
        return asset_id_base;
    }

    public List<ExchangeRate> getRates() {
        return rates;
    }
}
