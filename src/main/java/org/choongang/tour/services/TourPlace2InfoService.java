package org.choongang.tour.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.ListData;
import org.choongang.global.Pagination;
import org.choongang.global.exceptions.TourPlaceNotFoundException;
import org.choongang.tour.controllers.TourPlace2Search;
import org.choongang.tour.entities.QTourPlace2;
import org.choongang.tour.entities.TourPlace2;
import org.choongang.tour.repositories.TourPlace2Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourPlace2InfoService {
    private final TourPlace2Repository tourPlace2Repository;
    private final HttpServletRequest request;

    public ListData<TourPlace2> getList(TourPlace2Search search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        /*검색 처리 S*/
        QTourPlace2 tourPlace2 = QTourPlace2.tourPlace2;
        BooleanBuilder andBuilder = new BooleanBuilder();


        String sopt = search.getSopt();
        String skey = search.getSkey();
        String sido = search.getSido();
        String sigungu = search.getSigungu();

        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";
        //키워드가 있을때 조건별 검색
        if (StringUtils.hasText(skey) && StringUtils.hasText(skey.trim())) {
            /**
             * sopt
             * ALL - 통합 검색 - title, tel, address, description
             * TITLE, TEL, ADDRESS, DESCRIPTION
             */
            sopt = sopt.trim();
            skey = skey.trim();
            BooleanExpression condition = null;
            if (sopt.equals("ALL")) {// 통합검색
                condition = tourPlace2.title.concat(tourPlace2.tel).concat(tourPlace2.address).concat(tourPlace2.description).contains(skey);
            } else if (sopt.equals("TITLE")) {//여행지 명
                condition = tourPlace2.title.contains(skey);
            } else if (skey.equals("TEL")) {//여행지 연락처
                skey = skey.replaceAll("\\D", "");
                condition = tourPlace2.tel.contains(skey);
            } else if (sopt.equals("ADDRESS")) {
                condition = tourPlace2.address.contains(skey);
            } else if (sopt.equals("DESCRIPTION")) {
                condition = tourPlace2.description.contains(skey);
            }
            if (condition != null) andBuilder.and(condition);
            //시도 검색
            if (sido != null && StringUtils.hasText(sido.trim())) {
                andBuilder.and(tourPlace2.sido.eq(sido.trim()));
                if (sigungu != null && StringUtils.hasText(sigungu.trim())) {
                    andBuilder.and(tourPlace2.sigungu.eq(sigungu.trim()));
                }
            }
            //시군구 검색
            if (sigungu != null && StringUtils.hasText(sigungu.trim())) {
                andBuilder.and(tourPlace2.sigungu.like(sigungu.trim()));
            }
        }
        /*검색 처리 E*/
        //페이지 및 정렬 처리
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.desc("createdAt")));
        //데이터 조회
        Page<TourPlace2> data = tourPlace2Repository.findAll(andBuilder, pageable);
        Pagination pagination = new Pagination(page, (int) data.getTotalElements(), 10, limit, request);
        List<TourPlace2> items = data.getContent();
        return new ListData<>(items, pagination);
    }

    /**
     *
     * @param seq
     * @return
     */
    public TourPlace2 get(Long seq) {
        TourPlace2 item = tourPlace2Repository.findById(seq).orElseThrow(TourPlaceNotFoundException::new);
        //추가데이터 처리
        return null;
    }
}
