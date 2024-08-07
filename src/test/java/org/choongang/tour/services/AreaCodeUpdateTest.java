package org.choongang.tour.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AreaCodeUpdateTest {
    @Autowired
    private AreaCodeUpdateService service;
    @Test
    void test1(){
        service.update();
    }
}
