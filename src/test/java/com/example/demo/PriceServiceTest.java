package com.example.demo;

import com.example.demo.common.dataparser.DataParser;
import com.example.demo.price.dto.Price;
import com.example.demo.price.formatter.KoreanOutputFormatter;
import com.example.demo.price.formatter.OutPutFormatter;
import com.example.demo.price.service.PriceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

    @InjectMocks
    private PriceService priceService;

    @Mock
    private DataParser mockDataParser;

    @Spy
    private OutPutFormatter spyOutPutFormatter = new KoreanOutputFormatter();

    @Test
    @DisplayName("요금 정보가 없을 때 예외 메시지를 잘 반환하는지 테스트 (Mock 사용)")
    void billTotal_with_no_price_info_should_return_error_message() {

        when(mockDataParser.price("없는도시", "없는업종")).thenReturn(null);

        String result = priceService.billTotal("없는도시", "없는업종", 10);

        assertEquals("해당하는 요금 정보를 찾을 수 없습니다.", result);
    }

    @Test
    @DisplayName("정상적인 경우 요금 계산 결과를 잘 반환하는지 테스트 (Spy 사용)")
    void billTotal_should_return_formatted_string() {
        Price fakePrice = new Price();
        fakePrice.setCity("동두천시");
        fakePrice.setSector("가정용");
        fakePrice.setUnitPrice(690);

        when(mockDataParser.price("동두천시", "가정용")).thenReturn(fakePrice);
        String expectedResult = spyOutPutFormatter.format(fakePrice, 10);
        String actualResult = priceService.billTotal("동두천시", "가정용", 10);

        assertEquals(expectedResult, actualResult);
        assertTrue(actualResult.contains("6900"));
    }
}