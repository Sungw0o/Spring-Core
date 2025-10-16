package com.example.demo.price.service;

import com.example.demo.common.dataparser.DataParser;
import com.example.demo.exception.PriceNotFoundException;
import com.example.demo.price.dto.Price;
import com.example.demo.price.formatter.OutPutFormatter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PriceService {

    private final DataParser dataParser;
    private final OutPutFormatter outPutFormatter;

    public List<String> cities() {
        return dataParser.cities();
    }

    public List<String> sectors(String city) {
        return dataParser.sectors(city);
    }

    public Price price(String city, String sector) {
        return dataParser.price(city, sector);
    }

    public String billTotal(String city, String sector, int usage) {
        Price priceInfo = this.price(city, sector);
        if (priceInfo == null) {
            throw new PriceNotFoundException("해당하는 요금 정보를 찾을 수 없습니다.");
        }

        return outPutFormatter.format(priceInfo, usage);
    }
}