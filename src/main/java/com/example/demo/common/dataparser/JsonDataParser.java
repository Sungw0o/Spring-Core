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

@Component
@ConditionalOnProperty(name = "file.type", havingValue = "json") // json일 때 활성화되도록 조건 추가
public class JsonDataParser implements DataParser { // "implements DataParser" 추가

    private final List<Account> accounts;

    public JsonDataParser(FileProperties fileProperties) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Account>> typeReference = new TypeReference<>() {};
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileProperties.getAccountPath())) {
            this.accounts = mapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON file: " + fileProperties.getAccountPath(), e);
        }
    }

    @Override
    public List<Account> accounts() {
        return this.accounts; // 파싱된 결과를 반환합니다.
    }

    // Price 관련 메서드는 아직 구현하지 않습니다.
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