package com.example.demo.common.dataparser;

import com.example.demo.account.dto.Account;
import com.example.demo.common.properties.FileProperties;
import com.example.demo.price.dto.Price;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "file.type", havingValue = "json")
public class JsonDataParser implements DataParser {

    private final List<Account> accounts;
    private final List<Price> prices;

    public JsonDataParser(FileProperties fileProperties) {
        this.accounts = loadAccounts(fileProperties.getAccountPath());
        this.prices = loadPrices(fileProperties.getPricePath());
    }

    private List<Account> loadAccounts(String path) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Account>> typeReference = new TypeReference<>() {};
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            return mapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON file: " + path, e);
        }
    }

    private List<Price> loadPrices(String path) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Price>> typeReference = new TypeReference<>() {};
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            return mapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON file: " + path, e);
        }
    }

    @Override
    public List<Account> accounts() {
        return this.accounts;
    }

    @Override
    public List<String> cities() {
        return this.prices.stream()
                .map(Price::getCity)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> sectors(String city) {
        return this.prices.stream()
                .filter(price -> price.getCity().equals(city))
                .map(Price::getSector)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Price price(String city, String sector) {
        return this.prices.stream()
                .filter(p -> p.getCity().equals(city) && p.getSector().equals(sector))
                .findFirst()
                .orElse(null);
    }

}