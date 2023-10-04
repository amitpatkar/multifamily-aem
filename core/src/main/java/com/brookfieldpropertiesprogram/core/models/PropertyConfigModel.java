/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.brookfieldpropertiesprogram.core.models;

import com.brookfieldpropertiesprogram.core.utils.JsonLdUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class PropertyConfigModel {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyConfigModel.class);
    final Resource resource;

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @Inject
    @Named(JcrConstants.JCR_TITLE)
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String title;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String description;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String headline;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String featuredImage;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String logo;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String standaloneLogoType;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String logoPortfolio;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String legalText;
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String theme;

    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    @Named("activeVariation")
    private String activeVariation;

    //Details
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String googleAddress;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String address;

    //@Inject
    //@org.apache.sling.models.annotations.Optional
    //@Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String city;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String[] associatedAmenities;

    HashMap<String, String> amenities = null;

    @ResourcePath(path = "/content/siteconfig/amenities", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource amenitiesResource;

    @ResourcePath(path = "/content/siteconfig/cities", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource citiesFolder;

    @ResourcePath(path = "/content/siteconfig/states", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource statesFolder;

    @ResourcePath(path = "/content/siteconfig/neighborhoods", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource neighborhoodsFolder;

    @ResourcePath(path = "/content/siteconfig/metroareas", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource metroAreasFolder;

    @ResourcePath(path = "/content/siteconfig/collections", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource collectionsFolder;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    String associatedCity;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    String associatedState;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String zipCode;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String latitude;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String longitude;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String phoneNumber;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String websiteURL;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String residentPortalURL;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String specialsTeaser;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String specials;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String specialsDisclaimer;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private Date specialsExpiry;

    //social
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String facebookURL;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String instagramURL;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String twitterURL;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String custServiceEmailAddress;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String anyoneHomeEmailAddress;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String onesiteID;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String tourProviderUnit;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String panoskinTourID;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String engrainSightMapID;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String googleApiKey;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String matterportID;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String peekPropertyTourURL;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String peekKey;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String peekAgentID;

    /*
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String tourIdForProperty;
     */
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String unitTourIdMapping;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String unitTypeTourIdMapping;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String embedAnyoneHome;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    public String embedAnyoneHomeStyle;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String embedAnyoneHomeHyLy;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String rentgrataKey;

    @Inject
    @Named(value = com.day.cq.tagging.TagConstants.PN_TAGS)
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String[] cqTags;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private List<NavigationLink> menuLinksRight;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String locationId;

    List<Map<String, String>> belongsToNeighborhoods;

    List<Map<String, String>> belongsToMetroAreas;

    List<Map<String, String>> belongsToCollections;

    @Inject
    ResourceResolver resourceResolver;

    String stateCode;
    String stateName;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    String officeHoursMon;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    String officeHoursTue;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    String officeHoursWed;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)  
    String officeHoursThu;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    String officeHoursFri;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    String officeHoursSat;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    String officeHoursSun;

    List<String> officeHours;

    HashMap<String, String> unitIdTourMappingMap;
    HashMap<String, String> unitTypeIdTourMappingMap;

    private final ValueMap valueMap;

    public PropertyConfigModel(Resource resource) {
        this.resource = resource;
        if (resource.getChild(JcrConstants.JCR_CONTENT) == null) {
            LOG.warn("{} child resource is null {}",resource.getName(),resource.getPath());
            valueMap = null;
        }
        else {
            valueMap = resource.getChild(JcrConstants.JCR_CONTENT).adaptTo(ValueMap.class);
        }
    }

    @PostConstruct
    protected void init() {
        if (valueMap == null) return;
        //PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        /*
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(currentResource))
                .map(Page::getPath).orElse("");       
         */
        //associatedAmenities = valueMap.get("associatedAmenities", String[].class);  
        findAmenities(); //
        /*
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        if (tagManager == null) {
            return;
        }
        if (cqTags != null) {
            for (String aTag : cqTags) {
                Tag theTag = tagManager.resolve(aTag);
                if (theTag != null && theTag.getNamespace().getName().equals("states")) {
                    stateCode = theTag.getName();
                    stateName = theTag.getTitle();
                } else if (theTag != null && theTag.getNamespace().getName().equals("office-hours")) {
                    if (officeHours == null) {
                        officeHours = new ArrayList<>();
                    }
                    officeHours.add(theTag.getTitle());
                }
            }
        }
         */
        activeVariation = valueMap.get("activeVariation", String.class);
        theme = valueMap.get("theme", String.class);

        if (StringUtils.isNotBlank(unitTourIdMapping)) {
            unitIdTourMappingMap = new HashMap<>();
            String[] parts = unitTourIdMapping.split(System.lineSeparator());
            for (String aPart : parts) {
                if (aPart.contains("=")) {
                    String[] subParts = aPart.split("[=]");
                    if (subParts.length == 2) {
                        unitIdTourMappingMap.put(subParts[0], subParts[1].trim());
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(unitTypeTourIdMapping)) {
            unitTypeIdTourMappingMap = new HashMap<>();
            String[] parts = unitTypeTourIdMapping.split(System.lineSeparator());
            for (String aPart : parts) {
                if (aPart.contains("=")) {
                    String[] subParts = aPart.split("[=]");
                    if (subParts.length == 2) {
                        unitTypeIdTourMappingMap.put(subParts[0], subParts[1].trim());
                    }
                }
            }
        }

        //
        if (StringUtils.isNotEmpty(associatedCity)) {
            String parts[] = associatedCity.split("[/]");
            Resource resourceCity = citiesFolder.getChild(parts[parts.length - 1]);
            if (resourceCity != null) {
                Page pageCity = resourceCity.adaptTo(Page.class);
                if (pageCity != null) {
                    this.city = pageCity.getTitle();
                }
            }
        }

        if (StringUtils.isNotEmpty(associatedState)) {
            String parts[] = associatedState.split("[/]");
            Resource resourceState = statesFolder.getChild(parts[parts.length - 1]);
            if (resourceState != null) {
                Page pageState = resourceState.adaptTo(Page.class);
                if (pageState != null) {
                    this.stateCode = resourceState.getName().toUpperCase();
                    this.stateName = pageState.getTitle();
                }
            }
        }

        /**
         *
         */
        if (neighborhoodsFolder != null && neighborhoodsFolder.hasChildren()) {
            for (Resource aResource : neighborhoodsFolder.getChildren()) {
                Page nc = aResource.adaptTo(Page.class);
                if (nc != null && nc.getContentResource() != null) {
                    ValueMap ncv = nc.getContentResource().getValueMap();
                    String[] ps = ncv.get("associatedProperties", String[].class);
                    if (ps != null) {
                        for (String p : ps) {
                            if (p.equalsIgnoreCase(this.resource.getPath())) {
                                //this propeerty belongs to the following neighborhood
                                if (belongsToNeighborhoods == null) {
                                    belongsToNeighborhoods = new ArrayList<>();
                                }
                                Map<String, String> m = new HashMap<>();
                                m.put("id", aResource.getName());
                                m.put("title", nc.getTitle());                                
                                String associatedCityN = ncv.get("associatedCity", String.class);
                                String associatedStateN = ncv.get("associatedState", String.class);
                                m.put("state", (StringUtils.isNotEmpty(associatedStateN) ? associatedStateN.split("[/]")[associatedStateN.split("[/]").length-1] : ""));
                                m.put("city", (StringUtils.isNotEmpty(associatedCityN) ? associatedCityN.split("[/]")[associatedCityN.split("[/]").length-1] : ""));
                                belongsToNeighborhoods.add(m);
                            }
                        }
                    }
                }
            }

            if (metroAreasFolder != null && metroAreasFolder.hasChildren()) {
                for (Resource aResource : metroAreasFolder.getChildren()) {
                    Page nc = aResource.adaptTo(Page.class);
                    if (nc != null && nc.getContentResource() != null) {
                        String[] ps = nc.getContentResource().getValueMap().get("associatedProperties", String[].class);
                        if (ps != null) {
                            for (String p : ps) {
                                if (p.equalsIgnoreCase(this.resource.getPath())) {
                                    //this propeerty belongs to the following neighborhood
                                    if (belongsToMetroAreas == null) {
                                        belongsToMetroAreas = new ArrayList<>();
                                    }
                                    Map<String, String> m = new HashMap<>();
                                    m.put("id", aResource.getName());
                                    m.put("title", nc.getTitle());
                                    belongsToMetroAreas.add(m);
                                }
                            }
                        }
                    }
                }
            }
            /**
             *
             */
            if (collectionsFolder != null && collectionsFolder.hasChildren()) {
                for (Resource aResource : collectionsFolder.getChildren()) {
                    Page nc = aResource.adaptTo(Page.class);
                    if (nc != null) {
                        String[] ps = nc.getContentResource().getValueMap().get("associatedProperties", String[].class);
                        if (ps != null) {
                            for (String p : ps) {
                                if (p.equalsIgnoreCase(this.resource.getPath())) {
                                    //this propeerty belongs to the following neighborhood
                                    if (belongsToCollections == null) {
                                        belongsToCollections = new ArrayList<>();
                                    }
                                    Map<String, String> m = new HashMap<>();
                                    m.put("id", aResource.getName());
                                    m.put("title", nc.getTitle());
                                    belongsToCollections.add(m);
                                }
                            }
                        }
                    }
                }
            }
        }

        //
        officeHours = new ArrayList<>();
        if (StringUtils.isNotEmpty(officeHoursMon)) {
            officeHours.add("Monday: " + officeHoursMon);
        }
        if (StringUtils.isNotEmpty(officeHoursTue)) {
            officeHours.add("Tuesday: " + officeHoursTue);
        }
        if (StringUtils.isNotEmpty(officeHoursWed)) {
            officeHours.add("Wednesday: " + officeHoursWed);
        }
        if (StringUtils.isNotEmpty(officeHoursThu)) {
            officeHours.add("Thursday: " + officeHoursThu);
        }
        if (StringUtils.isNotEmpty(officeHoursFri)) {
            officeHours.add("Friday: " + officeHoursFri);
        }
        if (StringUtils.isNotEmpty(officeHoursSat)) {
            officeHours.add("Saturday: " + officeHoursSat);
        }
        if (StringUtils.isNotEmpty(officeHoursSun)) {
            officeHours.add("Sunday: " + officeHoursSun);
        }

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getHeadline() {
        return headline;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public String getLogo() {
        return logo;
    }

    public String getStandaloneLogoType() {
        return standaloneLogoType;
    }

    public String getLogoPortfolio() {
        return logoPortfolio;
    }

    public String getTheme() {
        return theme;
    }

    public String getActiveVariation() {
        return activeVariation;
    }

    public String getGoogleAddress() {
        return googleAddress;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public String getOfficeHoursMon() {
        return officeHoursMon;
    }

    public String getOfficeHoursTue() {
        return officeHoursTue;
    }

    public String getOfficeHoursWed() {
        return officeHoursWed;
    }

    public String getOfficeHoursThu() {
        return officeHoursThu;
    }

    public String getOfficeHoursFri() {
        return officeHoursFri;
    }

    public String getOfficeHoursSat() {
        return officeHoursSat;
    }

    public String getOfficeHoursSun() {
        return officeHoursSun;
    }

    public List<String> getOfficeHours() {
        return officeHours;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumberFormatted() {
        if (StringUtils.isNotEmpty(phoneNumber) && phoneNumber.length() == 10) {
            return phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        }
        return phoneNumber;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public String getResidentPortalURL() {
        return residentPortalURL;
    }

    public String getSpecialsTeaser() {
        return specialsTeaser;
    }

    public String getSpecials() {
        return specials;
    }

    public boolean isSpecialsExpired() {
        if (specialsExpiry == null) {
            return false; // if null then the specials has not expired.
        }
        Date currentDate = new Date();
        return currentDate.after(specialsExpiry); //return true if current date is before the specislasExpiry
    }

    public String getSpecialsDisclaimer() {
        return specialsDisclaimer;
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public String getInstagramURL() {
        return instagramURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }

    public String getCustServiceEmailAddress() {
        return custServiceEmailAddress;
    }

    public String getAnyoneHomeEmailAddress() {
        return anyoneHomeEmailAddress;
    }

    public String getOnesiteID() {
        return onesiteID;
    }

    public String getTourProviderUnit() {
        return tourProviderUnit;
    }

    public String getPanoskinTourID() {
        return panoskinTourID;
    }

    public String getEngrainSightMapID() {
        return engrainSightMapID;
    }

    public String getGoogleApiKey() {
        return googleApiKey;
    }

    public String getMatterportID() {
        return matterportID;
    }

    public String getPeekPropertyTourURL() {
        return peekPropertyTourURL;
    }

    public String getPeekKey() {
        return peekKey;
    }

    public String getPeekAgentID() {
        return peekAgentID;
    }

    public String getRentgrataKey() {
        return rentgrataKey;
    }

    /*
    public String getTourIdForProperty() {
        return tourIdForProperty;
    }
     */
    public String getEmbedAnyoneHome() {
        return embedAnyoneHome;
    }

    public String getEmbedAnyoneHomeHyLy() {
        return embedAnyoneHomeHyLy;
    }

    public String getLegalText() {
        return legalText;
    }

    public String getLocationId() {
        return locationId;
    }

    public ValueMap getValueMap() {
        return valueMap;
    }

    public List<NavigationLink> getMenuLinksRight() {
        return Optional.ofNullable(menuLinksRight).map(List::stream).orElseGet(Stream::empty).collect(Collectors.toList());
    }

    public HashMap<String, String> getUnitTypeIdTourMappingMap() {
        return unitTypeIdTourMappingMap;
    }

    public HashMap<String, String> getUnitIdTourMappingMap() {
        return unitIdTourMappingMap;
    }

    public Map<String, String> getAmenities() {
        if (amenities == null) {
            return null;
        }
        Set<Map.Entry<String, String>> entries = amenities.entrySet();
        return (HashMap<String, String>) entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    protected void findAmenities() {
        if (amenitiesResource == null || associatedAmenities == null || associatedAmenities.length == 0) {
            return;
        }
        KeyValuesModel km = amenitiesResource.adaptTo(KeyValuesModel.class);
        if (km == null || km.getKeyValues() == null || km.getKeyValues().isEmpty()) {
            return;
        }
        for (String a : associatedAmenities) {
            if (km.getKeyValues().containsKey(a)) {
                if (amenities == null) {
                    amenities = new HashMap<>();
                }
                amenities.put(a, km.getKeyValues().get(a));
            }
        }
    }

    public String getData() {
        return getDataAsObject().toString();
    }

    public JsonObject getDataAsObject() {
        JsonObject jsonObj = new JsonObject();
        //Analytics        
        jsonObj.addProperty("onesiteID", StringUtils.isEmpty(onesiteID) ? "" : onesiteID);
        jsonObj.addProperty("locationId", StringUtils.isEmpty(locationId) ? "" : locationId);
        jsonObj.addProperty("propertyId", resource.getName());
        jsonObj.addProperty(NameConstants.PN_DT_NAME, getTitle());
        jsonObj.addProperty("featuredImage", getFeaturedImage());
        jsonObj.addProperty("specialsTeaser", getSpecialsTeaser());
        jsonObj.addProperty("city", getCity());
        jsonObj.addProperty("stateCode", getStateCode());
        jsonObj.addProperty("stateName", getStateName());
        jsonObj.addProperty("streetAddress", getAddress());
        jsonObj.addProperty("postalCode", getZipCode());
        jsonObj.addProperty("href", "/properties/" + getName() + ".html");
        //neighborhoods
        if (belongsToNeighborhoods != null && !belongsToNeighborhoods.isEmpty()) {
            JsonArray jsonArray = new JsonArray();
            for (Map<String, String> nc : belongsToNeighborhoods) {
                JsonObject jo = new JsonObject();
                jo.addProperty("id", nc.get("id"));
                jo.addProperty("title", nc.get("title"));
                jo.addProperty("city", nc.get("city"));
                jo.addProperty("state", nc.get("state"));
                jo.addProperty("href", "/neighborhoods/" + nc.get("id") + ".html");
                jsonArray.add(jo);
            }
            jsonObj.add("neighborhoods", jsonArray);
        }
        
        if (belongsToMetroAreas != null && !belongsToMetroAreas.isEmpty()) {
            JsonArray jsonArray = new JsonArray();
            for (Map<String, String> nc : belongsToMetroAreas) {
                JsonObject jo = new JsonObject();
                jo.addProperty("id", nc.get("id"));
                jo.addProperty("title", nc.get("title"));
                jo.addProperty("href", "/metroareas/" + nc.get("id") + ".html");
                jsonArray.add(jo);
            }
            jsonObj.add("metroareas", jsonArray);
        }
        if (belongsToCollections != null && !belongsToCollections.isEmpty()) {
            JsonArray jsonArray = new JsonArray();
            for (Map<String, String> nc : belongsToCollections) {
                JsonObject jo = new JsonObject();
                jo.addProperty("id", nc.get("id"));
                jo.addProperty("title", nc.get("title"));
                jo.addProperty("href", "/collections/" + nc.get("id") + ".html");
                jsonArray.add(jo);
            }
            jsonObj.add("collections", jsonArray);
        }

        return jsonObj;
    }

    /**
     *
     *
     * @return JsonObject
     * @throws URISyntaxException
     * @throws java.net.MalformedURLException
     */
    public JsonObject getJsonLdObject() {
        Map<String, Object> ld = new HashMap<>();
        ld.put("@context", "https://schema.org/");
        ld.put("@type", "Place");
        if (!StringUtils.isEmpty(this.logo)) {
            ld.put("logo", "BASE_SERVER_URL" + this.logo);
        }
        if (StringUtils.isNotEmpty(this.getName())) {
            ld.put("name", this.getTitle());
        }
        if (StringUtils.isNotEmpty(this.getDescription())) {
            ld.put("description", this.getDescription());
        }
        if (StringUtils.isNotEmpty(this.getHeadline())) {
            ld.put("slogan", this.getHeadline());
        }
        if (StringUtils.isNotEmpty(this.getFeaturedImage())) {
            ld.put("image", "BASE_SERVER_URL" + this.getFeaturedImage());
        }
        if (StringUtils.isNotEmpty(this.getEmbedAnyoneHome())) {
            ld.put("tourBookingPage", "SELF#schedule-tour-popup-button");
        }

        Map<String, Object> postalAddress = new HashMap<>();
        postalAddress.put("addressCountry", "US");
        postalAddress.put("@type", "PostalAddress");
        if (StringUtils.isNotEmpty(this.address)) {
            postalAddress.put("streetAddress", this.address);
        }
        if (StringUtils.isNotEmpty(this.city)) {
            postalAddress.put("addressRegion", this.city);
        }
        if (StringUtils.isNotEmpty(this.zipCode)) {
            postalAddress.put("postalCode", this.zipCode);
        }
        if (StringUtils.isNotEmpty(this.phoneNumber)) {
            postalAddress.put("telephone", this.phoneNumber);
        }
        ld.put("address", postalAddress);

        if (StringUtils.isNotEmpty(this.phoneNumber)) {
            ld.put("telephone", this.phoneNumber);
        }

        return JsonLdUtils.getJsonLd(ld);
    }

    public String getName() {
        return resource.getName();
    }

    public Map<String, Object> getAdditionalMap() {
        Map<String, Object> additionalMap = new HashMap<>();
        if (getPhoneNumberFormatted() != null) {
            additionalMap.put("phoneNumberFormatted", getPhoneNumberFormatted());
        }
        if (!StringUtils.isEmpty(stateCode)) {
            additionalMap.put("stateCode", getStateCode());
        }
        if (!StringUtils.isEmpty(stateName)) {
            additionalMap.put("stateName", getStateName());
        }
        if (!StringUtils.isEmpty(city)) {
            additionalMap.put("city", getCity());
        }
        if (!StringUtils.isEmpty(title)) {
            additionalMap.put("title", getTitle());
        }
        try {
            additionalMap.put("mapLink", getMapLink());

        } catch (URISyntaxException e) {
            LOG.warn(null, e);
        }
        return additionalMap;
    }

    public String getMapLink() throws URISyntaxException {
        String query = address + "," + city + "," + stateCode;
        return "https://www.google.com/maps/search/?api=1&query=" + query.replaceAll(" ", "+").replaceAll(",", "%2C");
    }

}
