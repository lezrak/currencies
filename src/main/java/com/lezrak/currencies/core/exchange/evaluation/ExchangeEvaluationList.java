package com.lezrak.currencies.core.exchange.evaluation;


import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class ExchangeEvaluationList {

    private String from;
    private Set<String> to;
    private BigDecimal amount;

    public ExchangeEvaluationList() {
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(Set<String> to) {
        this.to = to;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public Set<String> getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
