package org.choongang.tour.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.choongang.global.exceptions.ExceptionProcessor;
import org.choongang.global.rests.gov.detailapi.DetailItem;
import org.choongang.tour.constants.ContentType;
import org.choongang.tour.entities.TourPlace;
import org.choongang.tour.repositories.TourPlaceRepository;
import org.choongang.tour.services.TourDetailInfoService;
import org.choongang.tour.services.TourPlaceInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController implements ExceptionProcessor {
    private final TourPlaceRepository tourPlaceRepository;
    private final TourPlaceInfoService placeInfoService;
    private final TourDetailInfoService detailInfoService;

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        model.addAttribute("addCommonScript", List.of("map"));
        model.addAttribute("addScript", List.of("tour/view"));
        return "front/tour/view";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("title")));
        Page<TourPlace> itemPage = tourPlaceRepository.findAll(pageable);
        model.addAttribute("item", itemPage);
        return "front/tour/list";
    }

    @GetMapping("/detail/{contentId}")
    public String detail(@PathVariable("contentId") Long contentId, Model model) {
        DetailItem item = detailInfoService.getDetail(contentId);
        model.addAttribute("addCommonScript", List.of("map"));
        model.addAttribute("addScript", List.of("tour/detailview"));
        model.addAttribute("items", item);
        return "front/tour/detail";
    }

    @GetMapping("/fest")
    public String festlist(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "10") int size, Model model) {
        List<TourPlace> items = placeInfoService.getSearchedList(ContentType.Festival, page, size);
        model.addAttribute("items", items);
        return "front/tour/fest";
    }

    @GetMapping("/stay")
    public String accomolist(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "10") int size, Model model) {
        List<TourPlace> items = placeInfoService.getSearchedList(ContentType.Accommodation,page,size);
        model.addAttribute("items", items);
        return "front/tour/stay";
    }

}
