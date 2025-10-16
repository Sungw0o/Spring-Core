package com.example.demo.common.dataparser;

import com.example.demo.account.dto.Account;
import com.example.demo.common.properties.FileProperties;
import com.example.demo.price.dto.Price;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

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
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> sectors(String city) {
        return this.prices.stream()
                .filter(price -> price.getCity() != null && price.getCity().trim().equals(city))
                .map(price -> price.getSector().trim())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Price price(String city, String sector) {
        return this.prices.stream()
                .filter(price -> price.getCity() != null && price.getCity().trim().equals(city)
                        && price.getSector() != null && price.getSector().trim().equals(sector))
                .findFirst()
                .orElse(null);
    }

}