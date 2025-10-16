package com.example.demo.price.formatter;

import com.example.demo.price.dto.Price;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("eng")
public class EnglishOutputFormatter implements OutPutFormatter {

    @Override
    public String format(Price price, int usage) {
        long totalBill = (long) price.getUnitPrice() * usage;
        return String.format("city: %s, sector: %s, unit price(won): %d, bill total(won): %d",
                price.getCity().trim(),
                price.getSector().trim(),
                price.getUnitPrice(),
                totalBill);
    }
}