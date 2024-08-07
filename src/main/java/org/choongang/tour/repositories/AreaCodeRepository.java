package org.choongang.tour.repositories;

import org.choongang.tour.entities.AreaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AreaCodeRepository extends JpaRepository<AreaCode,String>, QuerydslPredicateExecutor<AreaCode> {
}
