package com.brookfieldpropertiesprogram.core.models;

import java.util.ArrayList;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = { Resource.class })
public class SliderConfig {

    @Inject
    @Optional
    private String sliderImagePath;

    @Inject
    @Optional
    private String sliderImageLabel;

    @Inject
    @Optional
    private String title;

    @Inject
    @Optional
    private String metaDescription;

    @Inject
    @Optional
    private String addressLine1;

    @Inject
    @Optional
    private String addressLine2;

    @Inject
    @Optional
    private String featuredTitle;

    @Inject
    @Optional
    private String ctaLinkText;

    @Inject
    @Optional
    private String ctaLinkTarget;

    @Inject
    @Optional
    private List<SliderConfig> featuredPropertyList;

    public String getSliderImagePath() {
        return sliderImagePath;
    }

    public String getSliderImageLabel() {
        return sliderImageLabel;
    }

    public String getTitle() {
        return title;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getFeaturedTitle() {
        return featuredTitle;
    }

    public String getCtaLinkText() {
        return ctaLinkText;
    }

    public String getCtaLinkTarget() {
        return ctaLinkTarget;
    }

    public List<SliderConfig> getFeaturedPropertyList() {
        return (featuredPropertyList != null) ? new ArrayList(featuredPropertyList) : null;
    }
}
