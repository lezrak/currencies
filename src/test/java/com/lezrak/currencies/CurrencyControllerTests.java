package com.lezrak.currencies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lezrak.currencies.currency.exchange.evaluation.ExchangeEvaluationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void exchangeRatesWorkEndToEnd() throws Exception {
        mockMvc.perform(get("/api/v1/currencies/{currency}", "BTC"))
                .andExpect(status().isOk());
    }


    @Test
    void exchangeEvaluationWorkEndToEnd() throws Exception {
        ExchangeEvaluationRequest request = new ExchangeEvaluationRequest();
        request.setAmount(BigDecimal.TEN);
        request.setFrom("BTC");
        request.setTo(Arrays.asList("USDT", "ETH"));

        mockMvc.perform(post("/api/v1/currencies/exchange")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


}
