package org.choongang.tour.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.global.ListData;
import org.choongang.global.Pagination;
import org.choongang.global.Utils;
import org.choongang.global.exceptions.BadRequestException;
import org.choongang.global.exceptions.ExceptionProcessor;
import org.choongang.global.rests.gov.detailapi.DetailItem;
import org.choongang.tour.entities.TourPlace;
import org.choongang.tour.repositories.TourPlaceRepository;
import org.choongang.tour.services.TourDetailInfoService;
import org.choongang.tour.services.TourPlaceInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController implements ExceptionProcessor {
    private final TourPlaceRepository tourPlaceRepository;
    private final TourPlaceInfoService placeInfoService;
    private final TourDetailInfoService detailInfoService;
    private final Utils utils;

    private void addListProcess(Model model,ListData<TourPlace> data){
        Pagination pagination = data.getPagination();
        //pagination.setBaseURL();
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination",pagination);
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        model.addAttribute("addCommonScript", List.of("map"));
        model.addAttribute("addScript", List.of("tour/view"));
        return "front/tour/view";
    }

    @GetMapping("/list")
    public String list(Model model, @ModelAttribute TourPlaceSearch search) {
        search.setContentType(null);
        ListData<TourPlace> data = placeInfoService.getTotalList(search);
        addListProcess(model, data);
        return "front/tour/list";
    }

    @GetMapping("/list/{type}")
    public String list(@PathVariable("type") String type, @ModelAttribute TourPlaceSearch search, Model model) {

        try {
            search.setContentType(utils.typeCode(type));
            ListData<TourPlace> data = placeInfoService.getSearchedList(search);

            addListProcess(model, data);
            return "front/tour/list";
        } catch (BadRequestException e) {
            e.printStackTrace();
            return "redirect:/tour/list";
        }
    }

    @GetMapping("/{type}/search")
    public String search(@PathVariable(value = "type",required = false) String type, Model model, @ModelAttribute TourPlaceSearch search) {
        if(type != null) {
            search.setContentType(utils.typeCode(type));
        }
        ListData<TourPlace> data = placeInfoService.getSearchedList(search);
        addListProcess(model, data);
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
}
