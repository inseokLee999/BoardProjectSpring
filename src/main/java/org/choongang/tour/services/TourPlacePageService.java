package org.choongang.tour.services;

import lombok.RequiredArgsConstructor;
import org.choongang.tour.entities.TourPlace;
import org.choongang.tour.repositories.TourPlaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourPlacePageService {
    private final TourPlaceRepository repository;
    public Page<TourPlace> findPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }
}
