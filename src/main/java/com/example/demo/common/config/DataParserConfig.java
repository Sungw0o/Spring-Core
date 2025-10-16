package com.example.demo.common.config;

import com.example.demo.common.dataparser.CsvDataParser;
import com.example.demo.common.dataparser.DataParser;
import com.example.demo.common.dataparser.JsonDataParser;
import com.example.demo.common.properties.FileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataParserConfig {

    @Bean
    @ConditionalOnProperty(name = "file.type", havingValue = "csv")
    public DataParser csvDataParser(FileProperties fileProperties) {
        return new CsvDataParser(fileProperties);
    }

    @Bean
    @ConditionalOnProperty(name = "file.type", havingValue = "json")
    public DataParser jsonDataParser(FileProperties fileProperties) {
        return new JsonDataParser(fileProperties);
    }
}