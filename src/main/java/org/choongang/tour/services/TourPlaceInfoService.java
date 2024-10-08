package org.choongang.tour.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.ListData;
import org.choongang.global.Pagination;
import org.choongang.global.rests.gov.api.ApiItem;
import org.choongang.global.rests.gov.api.ApiResult;
import org.choongang.tour.controllers.TourPlaceSearch;
import org.choongang.tour.entities.QTourPlace;
import org.choongang.tour.entities.TourPlace;
import org.choongang.tour.repositories.TourPlaceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
public class TourPlaceInfoService {
    @PersistenceContext
    private EntityManager em;

    private final RestTemplate restTemplate;
    private final TourPlaceRepository repository;
    private String serviceKey = "n5fRXDesflWpLyBNdcngUqy1VluCJc1uhJ0dNo4sNZJ3lkkaYkkzSSY9SMoZbZmY7/O8PURKNOFmsHrqUp2glA==";
    private final HttpServletRequest request;

    /**
     * 좌표, 거리 기반으로 검색
     *
     * @param search
     * @return
     */
    public List<TourPlace> getLocBasedList(TourPlaceSearch search) {
        double lat = search.getLatitude();
        double lon = search.getLongitude();
        int radius = search.getRadius();
        String url = String.format("https://apis.data.go.kr/B551011/KorService1/locationBasedList1?MobileOS=AND&MobileApp=test&mapX=%f&mapY=%f&radius=%d&numOfRows=5000&serviceKey=%s&_type=json", lon, lat, radius, serviceKey);
        try {
            ResponseEntity<ApiResult> response = restTemplate.getForEntity(URI.create(url), ApiResult.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody().getResponse().getHeader().getResultCode().equals("0000")) {

                List<Long> ids = response.getBody().getResponse().getBody().getItems().getItem().stream().map(ApiItem::getContentid).toList();
                if (!ids.isEmpty()) {
                    QTourPlace tourPlace = QTourPlace.tourPlace;
                    List<TourPlace> items = (List<TourPlace>) repository.findAll(tourPlace.contentId.in(ids), Sort.by(asc("contentId")));

                    return items;
                } // endif
            } // endif
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ListData<TourPlace> getTotalList(TourPlaceSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 10 : limit;
        int offset = (page - 1) * limit;

        Pagination pagination = new Pagination(page, (int) repository.count(), 0, limit, request);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QTourPlace tourPlace = QTourPlace.tourPlace;
        List<TourPlace> items = queryFactory.selectFrom(tourPlace)
                .orderBy(tourPlace.contentId.asc())
                .offset(offset)
                .limit(limit)
                .fetch();
        return new ListData<>(items, pagination);
    }

    public ListData<TourPlace> getSearchedList(TourPlaceSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset = page * limit + 1;

        /* 검색 조건 처리 S */
        QTourPlace tourPlace = QTourPlace.tourPlace;
        BooleanBuilder andBuilder = new BooleanBuilder();
        if (search.getContentType() != null) {
            andBuilder.and(tourPlace.contentTypeId.eq(search.getContentType().getId()));
        }
        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";

        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            BooleanExpression titleCond = tourPlace.title.contains(skey); // 제목 - subject LIKE '%skey%';
            BooleanExpression addressCond = tourPlace.address.contains(skey); // 내용 - content LIKE '%skey%';

            if (sopt.equals("TITLE")) { // 여행지 이름

                andBuilder.and(titleCond);

            } else if (sopt.equals("ADDRESS")) { // 주소

                andBuilder.and(addressCond);

            } else if (sopt.equals("TITLE_ADDRESS")) { // 제목 + 내용

                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(titleCond)
                        .or(addressCond);

                andBuilder.and(orBuilder);

            }

        }

        /* 검색 조건 처리 E */

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        int count = queryFactory.selectFrom(tourPlace)
                .where(andBuilder).fetch().size();
        JPAQuery<TourPlace> query = queryFactory.selectFrom(tourPlace)
                .orderBy(tourPlace.contentId.asc())
                .offset(offset)
                .limit(limit)
                .where(andBuilder);
        List<TourPlace> items = query.fetch();
        Pagination pagination = new Pagination(offset, count, 0, limit, request);
        return new ListData<>(items, pagination);
    }
}
