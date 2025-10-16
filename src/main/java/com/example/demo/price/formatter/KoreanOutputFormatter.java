package com.example.demo.price.formatter;

import com.example.demo.price.dto.Price;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("kor")
public class KoreanOutputFormatter implements OutPutFormatter {

    @Override
    public String format(Price price, int usage) {
        long totalBill = (long) price.getUnitPrice() * usage;
        return String.format("지자체명: %s, 업종: %s, 구간금액(원): %d, 총금액(원): %d",
                price.getCity().trim(),
                price.getSector().trim(),
                price.getUnitPrice(),
                totalBill);
    }
}