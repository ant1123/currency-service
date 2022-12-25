package com.currency.currencyservice.controller;
import com.currency.currencyservice.data.CurrenciesRepository;
import com.currency.currencyservice.data.Currency;
import com.currency.currencyservice.data.ExchangeValue;
import com.currency.currencyservice.data.ExchangeValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;


@Service
public class ExchangeValueService {
    private ExchangeValueRepository exchangeValueRepository;
    private CurrenciesRepository currenciesRepository;

    @Autowired
    public ExchangeValueService(ExchangeValueRepository exchangeValueRepository, CurrenciesRepository currenciesRepository) {
        this.exchangeValueRepository = exchangeValueRepository;
        this.currenciesRepository = currenciesRepository;
    }


    public void addExchangeValue(ExchangeValue exchangeValue) {
        exchangeValueRepository.save(exchangeValue);
    }


    public List<Currency> getAllCurrencies() {
        return currenciesRepository.findAll();
    }

    @Transactional
    public ExchangeValue convert(String fromName, String toName, String value) {
        Currency from = currenciesRepository.findById(fromName).orElseThrow(
                () -> new IllegalStateException("Not found initial currency "));
        Currency to = currenciesRepository.findById(toName).orElseThrow(
                () -> new IllegalStateException("Not found converted currency "));
        BigDecimal initialValue = new BigDecimal(value);
        BigDecimal fromRate = from.getRate();
        BigDecimal toRate = to.getRate();
        if (fromRate == null || toRate == null || BigDecimal.ZERO.equals(fromRate)) {
            throw new IllegalStateException("Incorrect rates information");
        }
        BigDecimal rate = toRate.divide(fromRate, RoundingMode.HALF_UP);
        BigDecimal convertedValue = initialValue.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        ExchangeValue exchangeValue = new ExchangeValue(fromName, toName, rate, initialValue, convertedValue);
        addExchangeValue(exchangeValue);
        System.out.println("exchangeValue " + exchangeValue);
        return exchangeValue;
    }
}
