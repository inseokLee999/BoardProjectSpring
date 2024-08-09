package org.choongang.tour.services;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.choongang.global.rests.gov.api.ApiItem;
import org.choongang.global.rests.gov.api.ApiResult;
import org.choongang.tour.constants.ContentType;
import org.choongang.tour.controllers.TourPlaceSearch;
import org.choongang.tour.entities.QTourPlace;
import org.choongang.tour.entities.TourPlace;
import org.choongang.tour.repositories.TourPlaceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public List<TourPlace> getSearchedList(ContentType contentType) {

        return getSearchedList(contentType,1,10);
    }
    public List<TourPlace> getSearchedList(ContentType contentType,int page,int limit){
        int offset = (page - 1) * limit;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QTourPlace tourPlace = QTourPlace.tourPlace;
        JPAQuery<TourPlace> query = queryFactory.selectFrom(tourPlace).where(tourPlace.contentTypeId.eq(contentType.getId()))
                .orderBy(tourPlace.contentId.asc())
                .offset(offset)
                .limit(limit);
        List<TourPlace> items = query.fetch();
        return items;
    }
}
