package org.choongang.tour.constants;

import java.util.List;

public enum ContentType {
    TourSpot(12L, "관광지"),
    CultureFacility(14L, "문화시설"),
    Festival(15L,"행사/공연/축제"),
    TourCourse(25L,"여행코스"),
    Leports(28L,"레포츠"),
    Accommodation(32L,"숙박"),
    Shopping(38L,"쇼핑"),
    Restaurant(39L,"음식점");

    private final long id;
    private final String type;

    ContentType(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<ContentType> getList() {
        return List.of(TourSpot, CultureFacility);
    }
}
