package org.choongang.tour.services;

import org.choongang.global.rests.gov.detailapi.DetailItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DetailServiceTest {
    @Autowired
    private TourDetailInfoService infoService;

    @Test
    void test() {
        DetailItem item = infoService.getDetail(126508L);
        System.out.println(item);
    }

}
