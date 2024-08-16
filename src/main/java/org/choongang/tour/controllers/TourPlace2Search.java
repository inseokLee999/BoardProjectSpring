package org.choongang.tour.controllers;

import lombok.Data;
import org.choongang.global.RequestPage;

@Data
public class TourPlace2Search extends RequestPage {
    /**
     * ALL - 통합 검색 - title, tel, address, description
     * TITLE, TEL, ADDRESS, DESCRIPTION
     */

    // SIDO : 시도
    private String sido;

    // SIDO + SIGUNGU : 시도 + 시군구
    private String sigungu;
}
