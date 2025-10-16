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
        return String.format("City: %s, Sector: %s, Usage: %d, Total Bill: $%,d",
                price.getCity(), price.getSector(), usage, totalBill);
    }
}