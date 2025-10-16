package com.example.demo.common.dataparser;

import com.example.demo.account.dto.Account;
import com.example.demo.common.properties.FileProperties;
import com.example.demo.price.dto.Price;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "file.type", havingValue = "csv")
public class CsvDataParser implements DataParser { // "implements DataParser" 추가

    private final List<Account> accounts;

    public CsvDataParser(FileProperties fileProperties) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileProperties.getAccountPath());
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            this.accounts = reader.lines()
                    .skip(1) // CSV 헤더(첫 줄)를 건너뜁니다.
                    .map(this::mapToAccount)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + fileProperties.getAccountPath(), e);
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
        return null;
    }

    @Override
    public List<String> sectors(String city) {
        return null;
    }

    @Override
    public Price price(String city, String sector) {
        return null;
    }
}