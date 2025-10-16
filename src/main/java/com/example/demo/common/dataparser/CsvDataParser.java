package com.example.demo.common.dataparser;

import com.example.demo.account.dto.Account;
import com.example.demo.exception.DataParseException;
import com.example.demo.common.properties.FileProperties;
import com.example.demo.price.dto.Price;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class CsvDataParser implements DataParser {

    private final List<Account> accounts;
    private final List<Price> prices;

    public CsvDataParser(FileProperties fileProperties) {
        this.accounts = loadAccounts(fileProperties.getAccountPath());
        this.prices = loadPrices(fileProperties.getPricePath());
    }

    private List<Account> loadAccounts(String path) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            return reader.lines()
                    .skip(1)
                    .map(this::mapToAccount)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new DataParseException("Failed to parse CSV file: " + path, e);
        }
    }

    private List<Price> loadPrices(String path) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {

            return reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] parts = line.split(",", -1);
                        Price price = new Price();
                        price.setId(Long.parseLong(parts[0].trim()));
                        price.setCity(parts[1].trim());
                        price.setSector(parts[2].trim());

                        String unitPriceStr = parts[6].trim();
                        if (!unitPriceStr.isEmpty()) {
                            price.setUnitPrice(Integer.parseInt(unitPriceStr));
                        }
                        return price;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new DataParseException("Failed to parse CSV file: " + path, e);
        }
    }

    private Account mapToAccount(String line) {
        String[] parts = line.split(",");
        Account account = new Account();
        account.setId(Long.parseLong(parts[0].trim()));
        account.setPassword(parts[1].trim());
        account.setName(parts[2].trim());
        return account;
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