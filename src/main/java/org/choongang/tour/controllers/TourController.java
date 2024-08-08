package org.choongang.tour.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.global.exceptions.ExceptionProcessor;
import org.choongang.tour.entities.TourPlace;
import org.choongang.tour.repositories.TourPlaceRepository;
import org.choongang.tour.services.TourPlacePageService;
import org.springframework.data.domain.Page;
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
    private final TourPlacePageService pageService;

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        model.addAttribute("addCommonScript", List.of("map"));
        model.addAttribute("addScript", List.of("tour/view"));
        return "front/tour/view";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<TourPlace> itemPage = pageService.findPaginated(page, size);
        model.addAttribute("item", itemPage);
        return "front/tour/list";
    }

}
