package com.example.demo.common.dataparser;

import com.example.demo.account.dto.Account;
import com.example.demo.common.properties.FileProperties;
import com.example.demo.price.dto.Price;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "file.type", havingValue = "csv")
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
            throw new RuntimeException("Failed to parse CSV file: " + path, e);
        }
    }

    private List<Price> loadPrices(String path) {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        ObjectReader reader = csvMapper.readerFor(Price.class).with(schema);

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8")) {
            MappingIterator<Price> iterator = reader.readValues(inputStreamReader);
            return iterator.readAll();
        } catch (Exception e) {
            System.err.println("Failed to parse CSV file: " + path);
            e.printStackTrace();
            return Collections.emptyList();
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
                .filter(price -> price.getCity().equals(city) && price.getSector().equals(sector))
                .findFirst()
                .orElse(null);
    }
}