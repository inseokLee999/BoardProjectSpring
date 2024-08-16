package org.choongang.tour.repositories;


import org.choongang.tour.entities.TourPlaceTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourPlaceTagRepository extends JpaRepository<TourPlaceTag, String> {
}