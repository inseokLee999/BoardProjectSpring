package org.choongang.tour.controllers;

import lombok.Data;
import org.choongang.global.CommonSearch;

@Data
public class TourPlace2Search extends CommonSearch {
    /**
     * ALL - 통합 검색 - title, tel, address, description
     * TITLE, TEL, ADDRESS, DESCRIPTION
     */

    // SIDO : 시도
    private String sido;

    // SIDO + SIGUNGU : 시도 + 시군구
    private String sigungu;
}
