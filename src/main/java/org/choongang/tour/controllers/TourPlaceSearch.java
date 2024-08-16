package org.choongang.tour.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.global.RequestPage;
import org.choongang.tour.constants.ContentType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourPlaceSearch extends RequestPage {

    /**
     * 필터 옵션
     *
     * contentType : 관광지 타입
     *
     *
     */
    private Double latitude;
    private Double longitude;
    private ContentType contentType;
    /**
     * 검색 옵션
     * TITLE : 여행지 이름
     * ADDRESS : 주소
     * TITLE_ADDRESS : 이름 + 주소
     * ALL:
     */
    private String sopt; // 검색 옵션
    private String skey; // 검색 키워드

    private List<Long> seq; // 게시글 번호
    private Integer radius = 1000;
}
