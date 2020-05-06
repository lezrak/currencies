package com.lezrak.currencies.core.exchange.evaluation;


import java.util.Map;


public class ExchangeEvaluationListDTO {

    private String from;
    private Map<String, ExchangeEvaluation> to;


    public ExchangeEvaluationListDTO(String from, Map<String, ExchangeEvaluation> to) {
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
