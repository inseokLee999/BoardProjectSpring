package org.choongang.tour.repositories;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.choongang.tour.entities.QTourPlace;
import org.choongang.tour.entities.TourPlace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TourPlaceRepositoryTest {
    @Autowired
    private TourPlaceRepository tourPlaceRepository;
    @PersistenceContext
    private EntityManager em;
    @Test
    @DisplayName("repository 연결 테스트")
    public void test(){
        Optional<TourPlace> items = tourPlaceRepository.findById(2688186L);
        items.ifPresent(System.out::println);

    }
    @Test
    @DisplayName("queryDsl 테스트1")
    public void test1() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QTourPlace tourPlace = QTourPlace.tourPlace;
        JPAQuery<TourPlace> query = queryFactory.selectFrom(tourPlace).where(tourPlace.contentTypeId.eq(15l))
                .orderBy(tourPlace.contentId.asc());
        List<TourPlace> items = query.fetch();
        items.forEach(System.out::println);
    }
}
