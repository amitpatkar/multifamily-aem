package com.brookfieldpropertiesprogram.core.models;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = {Resource.class})
public class MapModel {

    @Inject
    @org.apache.sling.models.annotations.Optional
    private String mapId;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private List<Poi> pointsOfInterest;

    public String getMapId() {
        return mapId;
    }

    public List<Poi> getPointsOfInterest() {
        return new ArrayList<>(pointsOfInterest);
    }

    public List<PoiWithCategory> getCategories() {
        Map<String, List<Poi>> groups = pointsOfInterest.stream().collect(Collectors.groupingBy(Poi::getCategory));
        return pointsOfInterest.stream()
                .map(poi -> poi.category)
                .distinct()
                .map(category -> new PoiWithCategory(category, groups.get(category)))
                .collect(Collectors.toList());
    }


    @Model(adaptables = {Resource.class})
    public static class Poi {

        @Inject
        @org.apache.sling.models.annotations.Optional
        private String title;

        @Inject
        @org.apache.sling.models.annotations.Optional
        private String addressLine1;

        @Inject
        @org.apache.sling.models.annotations.Optional
        private String addressLine2;

        @Inject
        @org.apache.sling.models.annotations.Optional
        private String lat;

        @Inject
        @org.apache.sling.models.annotations.Optional
        private String lng;

        @Inject
        @org.apache.sling.models.annotations.Optional
        private String category;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }

        public String getCategory() {
            return category;
        }

        public String getMapLink() {
            return "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng;
        }

    }

    public static class PoiWithCategory {
        private final String category;
        private final List<Poi> points;

        public PoiWithCategory(String category, List<Poi> points) {
            this.category = category;
            this.points = new ArrayList<>(points);
        }

        public String getCategory() {
            return category;
        }

        public List<Poi> getPoints() {
            return new ArrayList<>(points);
        }

        public boolean getShowMorePredicate() {
            return getPoints().size() > 3;
        }

        public int getShowMoreCount() {
            return getPoints().size() - 3;
        }
    }
}
