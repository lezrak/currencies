package com.lezrak.currencies.currency.exchange.evaluation;


import java.math.BigDecimal;
import java.util.List;

public class ExchangeEvaluationRequest {

    private String from;
    private List<String> to;
    private BigDecimal amount;

    public ExchangeEvaluationRequest() {
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
