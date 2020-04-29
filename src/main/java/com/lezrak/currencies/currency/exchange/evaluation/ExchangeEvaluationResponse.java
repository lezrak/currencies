package com.lezrak.currencies.currency.exchange.evaluation;


import java.util.Map;


public class ExchangeEvaluationResponse {

    private String from;
    private Map<String, ExchangeEvaluation> to;

    public ExchangeEvaluationResponse() {
    }

    public ExchangeEvaluationResponse(String from, Map<String, ExchangeEvaluation> to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public Map<String, ExchangeEvaluation> getTo() {
        return to;
    }
}
