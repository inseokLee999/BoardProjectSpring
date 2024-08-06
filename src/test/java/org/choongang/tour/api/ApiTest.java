package org.choongang.tour.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.choongang.global.rests.gov.api.ApiResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTest {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test1() {
        String url = "https://apis.data.go.kr/B551011/KorService1/locationBasedList1?MobileOS=AND&MobileApp=test&_type=json&mapX=126.94&mapY=37.55&radius=10000&serviceKey=n5fRXDesflWpLyBNdcngUqy1VluCJc1uhJ0dNo4sNZJ3lkkaYkkzSSY9SMoZbZmY7%2FO8PURKNOFmsHrqUp2glA%3D%3D";
        URI uri = URI.create(url);

        ResponseEntity<ApiResult> response = restTemplate.getForEntity(uri, ApiResult.class);
        System.out.println(response.getBody());
    }
}
