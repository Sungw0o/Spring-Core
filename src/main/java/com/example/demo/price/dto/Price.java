package com.example.demo.price.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Price {
    @JsonProperty("순번")
    long id;
    @JsonProperty("지자체명")
    String city;
    @JsonProperty("업종")
    String sector;
    @JsonProperty("단계") // 누락된 "단계" 필드 추가
    int stage;
    @JsonProperty("구간시작(세제곱미터)") // 누락된 "구간시작" 필드 추가
    long startUsage;
    @JsonProperty("구간끝(세제곱미터)") // 누락된 "구간끝" 필드 추가
    long endUsage;
    @JsonProperty("구간금액(원)")
    int unitPrice;
    @JsonProperty("단계별 기본요금(원)") // 누락된 "기본요금" 필드 추가
    int baseFare;
}