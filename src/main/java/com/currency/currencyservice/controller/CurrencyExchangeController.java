package com.currency.currencyservice.controller;
import com.currency.currencyservice.data.ExchangeValue;
import com.currency.currencyservice.data.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
public class CurrencyExchangeController {
    private ExchangeValueService exchangeValueService;

    @Autowired
    public CurrencyExchangeController(ExchangeValueService exchangeValueService) {
        this.exchangeValueService = exchangeValueService;
    }

    @GetMapping("/currencies")
    public List<Currency> getCurrencies()
    {
        return exchangeValueService.getAllCurrencies();
    }

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(
            @PathVariable String from,
            @PathVariable String to,
            @RequestParam(required = false, name="value", defaultValue = "1" ) String value)
    {
        return exchangeValueService.convert(from, to, value);
    }
}