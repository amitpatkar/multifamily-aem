package com.brookfieldpropertiesprogram.core.models;

import java.util.ArrayList;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = { Resource.class })
public class CarouselComponentModel {

    @Inject
    @Optional
    private List<SliderConfig> sliderList;

    public List<SliderConfig> getSliderList() {
        return (sliderList != null) ? new ArrayList(sliderList) : null;
    }
}
