package org.choongang.tour.repositories;

import org.choongang.tour.entities.TourPlace2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TourPlace2Repository extends JpaRepository<TourPlace2, Long>, QuerydslPredicateExecutor<TourPlace2> {
}
