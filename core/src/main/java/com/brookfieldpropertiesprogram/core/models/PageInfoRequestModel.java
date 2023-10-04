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

import com.brookfieldpropertiesprogram.core.constants.PathConstants;
import com.brookfieldpropertiesprogram.core.dto.ConfigPath;
import static com.brookfieldpropertiesprogram.core.models.PageInfoModel.LOG;
import com.brookfieldpropertiesprogram.core.services.PeekToursProvider;
import com.brookfieldpropertiesprogram.core.utils.ImageUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {SlingHttpServletRequest.class})
public class PageInfoRequestModel {

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    private final SlingHttpServletRequest request;
    private final String PROPERTIES_ROOT_PATH = "/content/portfolio/en/properties/";
    private final String COMMONCONTENT = "/commoncontent.";

    Page siteConfig = null;
    Page propertyConfig = null;
    Page cityConfig = null;
    Page stateConfig = null;
    Page neighborhoodConfig = null;
    Page metroConfig = null;
    Page collectionConfig = null;

    SiteConfigModel siteConfigModel = null;
    PageInfoModel pageInfoModel;
    PropertyConfigModel propertyConfigModel = null;
    CityConfigModel cityConfigModel = null;
    StateConfigModel stateConfigModel = null;
    NeighborhoodConfigModel neighborhoodConfigModel = null;
    private MetroAreaConfigModel metroAreaConfigModel = null;
    CollectionConfigModel collectionConfigModel = null;

    UnitTypeImagesModel unitTypeImagesModel = null;
    UnitImagesModel unitImagesModel = null;

    String contentFragmentPathForCommonContent;
    String contentFragmentPath;
    String contentFragmentPathGallery;
    String contentFragmentPathHero;

    String unitTypeKey; //normalized toLowercase and escape
    String unitKey;//normalized toLowercase and escape
    String unitTypeTourId;//normalized toLowercase and escape
    String unitTourId;//normalized toLowercase and escape
    String peekURL;
    Boolean isPortfolio;
    String refineResultPath;
    String backToPropertyPath;

    Map<String, Object> valueMapFinal;

    public PageInfoRequestModel(SlingHttpServletRequest request) {
        this.request = request;
    }

    //@Inject
    //@Source("script-bindings")
    //Resource resource;
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Page currentPage;

    @Inject
    @Optional
    private PeekToursProvider peekToursProvider;

    @PostConstruct
    protected void init() {
        //this.currentPage = resource.adaptTo(Page.class);
        if (currentPage == null) {
            //lets try to find the page here
            currentPage = request.getResource().adaptTo(Page.class);
            if (currentPage == null) {
                LOG.warn("Unable to find the current page from resource path:{}", request.getResource().getPath());
                return;
            }
        }
        if (request.getAttribute("pageInfoModel") != null) {
            pageInfoModel = (PageInfoModel) request.getAttribute("pageInfoModel");
            siteConfigModel = (SiteConfigModel) request.getAttribute("siteConfigModel");
            propertyConfigModel = (PropertyConfigModel) request.getAttribute("propertyConfigModel");

            cityConfigModel = (CityConfigModel) request.getAttribute("cityConfigModel");
            stateConfigModel = (StateConfigModel) request.getAttribute("stateConfigModel");
            neighborhoodConfigModel = (NeighborhoodConfigModel) request.getAttribute("neighborhoodConfigModel");
            metroAreaConfigModel = (MetroAreaConfigModel) request.getAttribute("metroAreaConfigModel");
            collectionConfigModel = (CollectionConfigModel) request.getAttribute("collectionConfigModel");

            valueMapFinal = (Map<String, Object>) request.getAttribute("valueMapFinal");

            contentFragmentPath = (String) request.getAttribute("contentFragmentPath");
            contentFragmentPathForCommonContent = (String) request.getAttribute("contentFragmentPathForCommonContent");

            unitTypeImagesModel = (UnitTypeImagesModel) request.getAttribute("unitTypeImagesModel");
            unitImagesModel = (UnitImagesModel) request.getAttribute("unitImagesModel");

            unitTypeKey = (String) request.getAttribute("unitTypeKey");
            unitKey = (String) request.getAttribute("unitKey");
            unitTypeTourId = (String) request.getAttribute("unitTypeTourId");
            unitTourId = (String) request.getAttribute("unitTourId");
            peekURL = (String) request.getAttribute("peekURL");

            request.setAttribute("returnedFromRequestAttribute", "true");
            isPortfolio = ImageUtils.isPortFolioSite(currentPage.getPath());
            refineResultPath = PROPERTIES_ROOT_PATH + currentPage.getName() + COMMONCONTENT;
            String currentPagePath = currentPage.getPath();
            backToPropertyPath = currentPagePath.substring(0, currentPagePath.lastIndexOf("/")) + ".html";
            return;
        }
        pageInfoModel = currentPage.adaptTo(PageInfoModel.class);
        resolveConfigurations(); //rest of the configurations
        valueMapFinal = new HashMap<>();
        //populate with page. prefix        
        if (contentFragmentPathForCommonContent != null) { //this means its contentcommon page processing and the experience fragment is the real page
            Resource localR = request.getResourceResolver().getResource(contentFragmentPathForCommonContent + "/" + JcrConstants.JCR_CONTENT);
            if (localR != null) {
                populateValueMap(localR.getName(), localR.adaptTo(ValueMap.class), "page.", null);
            }
        } else {
            populateValueMap(currentPage.getName(), currentPage.getContentResource().adaptTo(ValueMap.class), "page.", null);
        }
        //populate with site. prefix
        if (siteConfig != null) {
            populateValueMap(siteConfig.getName(), siteConfig.getContentResource().adaptTo(ValueMap.class), "site.", null);
        }

        if (propertyConfig != null) {
            populateValueMap(propertyConfig.getName(), propertyConfig.getContentResource().adaptTo(ValueMap.class), null, propertyConfigModel.getAdditionalMap());
        } else if (cityConfig != null) {
            populateValueMap(cityConfig.getName(), cityConfig.getContentResource().adaptTo(ValueMap.class), null, null);
        } else if (stateConfig != null) {
            populateValueMap(stateConfig.getName(), stateConfig.getContentResource().adaptTo(ValueMap.class), null, null);
        } else if (neighborhoodConfig != null) {
            populateValueMap(neighborhoodConfig.getName(), neighborhoodConfig.getContentResource().adaptTo(ValueMap.class), null, null);
        } else if (metroConfig != null) {
            populateValueMap(metroConfig.getName(), metroConfig.getContentResource().adaptTo(ValueMap.class), null, null);
        }

        if (unitTypeImagesModel != null) {
            populateValueMap("unitType", null, "unitType.gallery.", unitTypeImagesModel.getGalleryImagePaths());
            populateValueMap("unitType", null, "unitType.floorPlans.", unitTypeImagesModel.getFloorPlanImagePaths());
        }

        if (unitImagesModel != null) {
            populateValueMap("unit", null, "unit.gallery.", unitImagesModel.getGalleryImagePaths());
            populateValueMap("unit", null, "unit.floorPlans.", unitImagesModel.getFloorPlanImagePaths());
        }
        request.setAttribute("pageInfoModel", pageInfoModel);
        request.setAttribute("siteConfigModel", siteConfigModel);
        request.setAttribute("propertyConfigModel", propertyConfigModel);
        request.setAttribute("cityConfigModel", cityConfigModel);
        request.setAttribute("stateConfigModel", stateConfigModel);
        request.setAttribute("neighborhoodConfigModel", neighborhoodConfigModel);
        request.setAttribute("metroAreaConfigModel", metroAreaConfigModel);
        request.setAttribute("collectionConfigModel", collectionConfigModel);
        request.setAttribute("valueMapFinal", valueMapFinal);
        request.setAttribute("contentFragmentPath", contentFragmentPath);
        request.setAttribute("contentFragmentPathForCommonContent", contentFragmentPathForCommonContent);

        request.setAttribute("unitTypeImagesModel", unitTypeImagesModel);
        request.setAttribute("unitImagesModel", unitImagesModel);

        if (unitTypeKey != null) {
            request.setAttribute("unitTypeKey", unitTypeKey);
        }
        if (unitKey != null) {
            request.setAttribute("unitKey", unitKey);
        }
        if (unitTypeTourId != null) {
            request.setAttribute("unitTypeTourId", unitTypeTourId);
            valueMapFinal.put("unitTypeTourId", unitTypeTourId);
        }

        if (unitTourId != null) {
            request.setAttribute("unitTourId", unitTourId);
            valueMapFinal.put("unitTourId", unitTourId);
        }
        if (peekURL != null) {
            request.setAttribute("peekURL", peekURL);
        }

        request.setAttribute("originalPagePath", currentPage.getPath());

    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public PageInfoModel getPageInfoModel() {
        return pageInfoModel;
    }

    public Map<String, Object> getValueMapFinal() {
        return valueMapFinal;
    }

    public ValueMap getCombinedProps() {
        return new ValueMapDecorator(valueMapFinal);
    }

    public PropertyConfigModel getPropertyConfigModel() {
        return propertyConfigModel;
    }

    public CityConfigModel getCityConfigModel() {
        return cityConfigModel;
    }

    public StateConfigModel getStateConfigModel() {
        return stateConfigModel;
    }

    public NeighborhoodConfigModel getNeighborhoodConfigModel() {
        return neighborhoodConfigModel;
    }

    public MetroAreaConfigModel getMetroAreaConfigModel() {
        return metroAreaConfigModel;
    }

    public CollectionConfigModel getCollectionConfigModel() {
        return collectionConfigModel;
    }

    public SiteConfigModel getSiteConfigModel() {
        return siteConfigModel;
    }

    public UnitTypeImagesModel getUnitTypeImagesModel() {
        return unitTypeImagesModel;
    }

    public UnitImagesModel getUnitImagesModel() {
        return unitImagesModel;
    }

    public String getUnitTypeKey() {
        return unitTypeKey;
    }

    public String getUnitKey() {
        return unitKey;
    }

    public String getUnitTypeTourId() {
        return unitTypeTourId;
    }

    public String getUnitTourId() {
        return unitTourId;
    }

    public String getPeekURL() {
        return peekURL;
    }

    public Boolean getIsPortfolio() {
        return isPortfolio;
    }

    public String getRefineResultPath() {
        return refineResultPath;
    }

    public String getBackToPropertyPath() {
        return backToPropertyPath;
    }

    public String fetchPeekURLForUnit(String unitNumber, String oneSiteId) {
        if (propertyConfigModel == null || peekToursProvider == null) {
            return null;
        }
        if (StringUtils.isBlank(propertyConfigModel.getPeekKey()) || StringUtils.isBlank(propertyConfigModel.getPeekAgentID())) {
            return null;
        }
        String peekAgentId = this.detectPeekAgentId(oneSiteId);
        if (peekAgentId == null) {
            return null;
        }
        PeekToursProvider.Credentials creds = new PeekToursProvider.Credentials(propertyConfigModel.getPeekKey(), peekAgentId);
        try {
            JsonObject peekTour = peekToursProvider.getTourByNumber(creds, unitNumber);
            if (peekTour != null) {
                LOG.info("returned peek url: " + peekTour.get("url").getAsString());
                return peekTour.get("url").getAsString();
            }
        } catch (IOException e) {
            LOG.error(null, e);
        }

        return null;
    }

    public String fetchPeekURLForUnitType(String unitType, String oneSiteId) {
        if (propertyConfigModel == null || peekToursProvider == null) {
            return null;
        }
        if (StringUtils.isBlank(propertyConfigModel.getPeekKey()) || StringUtils.isBlank(propertyConfigModel.getPeekAgentID())) {
            return null;
        }
        String peekAgentId = this.detectPeekAgentId(oneSiteId);
        if (peekAgentId == null) {
            return null;
        }
        PeekToursProvider.Credentials creds = new PeekToursProvider.Credentials(propertyConfigModel.getPeekKey(), peekAgentId);
        try {
            JsonObject peekTour = peekToursProvider.getTourByFloorplan(creds, unitType);
            if (peekTour != null) {
                LOG.info("returned peek url: " + peekTour.get("url").getAsString());
                return peekTour.get("url").getAsString();
            }
        } catch (IOException e) {
            LOG.error(null, e);
        }

        return null;
    }

    private String detectPeekAgentId(String oneSiteId) {
        String peekIDComposite = propertyConfigModel.getPeekAgentID();
        if (StringUtils.isBlank(peekIDComposite)) {
            return null;
        }
        String[] peekIDPairs = peekIDComposite.split(",");
        String peekAgentId;
        if (StringUtils.isBlank(oneSiteId) || (peekIDPairs.length == 1 && peekIDPairs[0].split(":").length == 1)) {
            peekAgentId = peekIDPairs[0].split(":")[0].trim();
        } else {
            String pair = Arrays.stream(peekIDPairs).filter(p -> {
                String[] parts = p.split(":");
                if (parts.length > 1) {
                    return parts[1].equals(oneSiteId);
                }
                return false;
            }).findAny().orElse("");
            peekAgentId = pair.split(":")[0].trim();
        }
        if (StringUtils.isBlank(peekAgentId)) {
            return null;
        }
        return peekAgentId;
    }

    public List<PropertyConfigModel> getLocationSpecificPropertyConfigModels() {
        if (getCityConfigModel() != null) {
            return getCityConfigModel().getPropertyConfigModels();
        } else if (getStateConfigModel() != null) {
            List<CityConfigModel> lstCities = getStateConfigModel().getCityConfigModels();
            List<PropertyConfigModel> lstPropertyConfigModels = null;
            if (lstCities == null || lstCities.isEmpty()) {
                return null;
            }
            for (CityConfigModel cm : lstCities) {
                if (cm.getPropertyConfigModels() != null && !cm.getPropertyConfigModels().isEmpty()) {
                    if (lstPropertyConfigModels == null) {
                        lstPropertyConfigModels = new ArrayList<>();
                    }
                    lstPropertyConfigModels.addAll(cm.getPropertyConfigModels());
                }
            }
            return lstPropertyConfigModels;
        } else if (getNeighborhoodConfigModel() != null) {
            return (getNeighborhoodConfigModel().getAssociatedPropertiesConfigModel() != null ? getNeighborhoodConfigModel().getAssociatedPropertiesConfigModel().getPropertyConfigModels() : null);
        } else if (getMetroAreaConfigModel()!= null) {
            return (getMetroAreaConfigModel().getAssociatedPropertiesConfigModel() != null ? getMetroAreaConfigModel().getAssociatedPropertiesConfigModel().getPropertyConfigModels() : null);
        } else {
            PropertyConfigListModel plm = request.getResource().adaptTo(PropertyConfigListModel.class);
            return (plm != null && plm.getPropertyConfigModels() != null ? plm.getPropertyConfigModels() : null);
        }
    }

    public String getBaseURI() {
        String baseURIString = "";
        try {
            URI baseURI = new URI(request.getScheme(), null, request.getServerName(), request.getServerPort(), null, null, null);
            baseURIString = baseURI.toString();
        } catch (URISyntaxException e) {
            LOG.warn(null, e);
        }
        return baseURIString;
    }

    public String getData() {
        JsonObject jsonObj = new JsonObject();
        //currentPage
        String currentPagePath = currentPage.getPath();
        /*
        if (currentPagePath.startsWith("/content/portfolio")) {
            siteName = "portfolio";
        } else if (currentPagePath.startsWith("/content/standalone")) {
            String[] parts = currentPagePath.trim().split("/", 0);
            siteName = parts[3];
            siteType = "standalone";
        }
         */
        //pageName
        String pageName = currentPagePath.replaceAll("/content/", "");
        pageName = pageName.replaceAll("standalone/", "");
        pageName = pageName.replaceAll("/", "-");

        if (pageInfoModel == null) {
            return "{}";
        }
        //Analytics        
        JsonArray jsonObjProductInfoArr = new JsonArray();
        JsonObject jsonObjProductInfo = null; //new JsonObject();
        

        JsonObject jsonObjPageInfo = new JsonObject();
        jsonObjPageInfo.addProperty("currentPagePath", currentPagePath);
        jsonObjPageInfo.addProperty("pageName", pageName);
        jsonObjPageInfo.addProperty("pageType", currentPage.getName());
        //jsonObjPageInfo.addProperty("siteName", pageInfoModel.getSiteName());
        //jsonObjPageInfo.addProperty("siteType", pageInfoModel.getSiteType());

        jsonObjPageInfo.addProperty("serverName", request.getServerName());
        if (getCollectionConfigModel() != null) {
            jsonObjPageInfo.addProperty("collectionId", getCollectionConfigModel().getName());
            jsonObjPageInfo.addProperty("collectionName", getCollectionConfigModel().getTitle());
            JsonObject tmpObject = new JsonObject();
            tmpObject.addProperty("collectionId", getCollectionConfigModel().getName());
            tmpObject.addProperty("collectionName", getCollectionConfigModel().getName());
            if (jsonObjProductInfo == null) jsonObjProductInfo = new JsonObject();
            jsonObjProductInfo.add("collection", tmpObject);
        }
        if (getPropertyConfigModel() != null) {
            jsonObjPageInfo.addProperty("locationId", getPropertyConfigModel().getLocationId());
            jsonObjPageInfo.addProperty("locationName", getPropertyConfigModel().getTitle());
            JsonObject tmpObject = new JsonObject();
            tmpObject.addProperty("locationId", getPropertyConfigModel().getLocationId());
            tmpObject.addProperty("locationName", getPropertyConfigModel().getName());
            if (jsonObjProductInfo == null) jsonObjProductInfo = new JsonObject();
            jsonObjProductInfo.add("location", tmpObject);

            JsonObject tmpObject1 = new JsonObject();
            tmpObject1.addProperty("name", getPropertyConfigModel().getTitle());
            if (getPropertyConfigModel().getOnesiteID() != null) {
                tmpObject1.addProperty("productID", getPropertyConfigModel().getOnesiteID());
            }
            if (jsonObjProductInfo == null) jsonObjProductInfo = new JsonObject();
            jsonObjProductInfo.add("productInfo", tmpObject1);
        }
        if (getIsPortfolio()) {
            jsonObjPageInfo.addProperty("siteType", "multi-family");
            jsonObjPageInfo.addProperty("siteName", "multi-family");
        } else {
            jsonObjPageInfo.addProperty("siteType", "property");
            if (getSiteConfigModel() != null) {
                jsonObjPageInfo.addProperty("siteName", getSiteConfigModel().getName());
            }
        }

        if (getUnitTypeImagesModel() != null) {
            if (getUnitTypeImagesModel().getFloorPlanId() != null) {
                jsonObjPageInfo.addProperty("floorPlanId", getUnitTypeImagesModel().getFloorPlanId());
            }
            if (getUnitTypeImagesModel().getUnitTypeName() != null) {
                jsonObjPageInfo.addProperty("floorPlanName", getUnitTypeImagesModel().getUnitTypeName());
            }
            JsonObject tmpObject = new JsonObject();
            tmpObject.addProperty("floorPlanId", getUnitTypeImagesModel().getFloorPlanId());
            tmpObject.addProperty("floorPlanName", getUnitTypeImagesModel().getUnitTypeName());
            jsonObjProductInfo.add("floorPlan", tmpObject);
        }        
        else if (getUnitImagesModel() != null) {
            if (getUnitImagesModel().getFloorPlanId() != null) {
                jsonObjPageInfo.addProperty("floorPlanId", getUnitImagesModel().getFloorPlanId());
            }
            if (getUnitImagesModel().getUnitTypeName() != null) {
                jsonObjPageInfo.addProperty("floorPlanName", getUnitImagesModel().getUnitTypeName());
            }
            JsonObject tmpObject = new JsonObject();
            tmpObject.addProperty("floorPlanId", getUnitImagesModel().getFloorPlanId());
            tmpObject.addProperty("floorPlanName", getUnitImagesModel().getUnitTypeName());
            jsonObjProductInfo.add("floorPlan", tmpObject);
        }
        
        jsonObj.add("pageInfo", jsonObjPageInfo);
        if (jsonObjProductInfo != null) {
            jsonObjProductInfoArr.add(jsonObjProductInfo);
            jsonObj.add("product", jsonObjProductInfoArr);
        }
        

        if (getSiteConfigModel() != null && getSiteConfigModel().getData() != null) {
            jsonObj.add("siteInfo", new JsonParser().parse(getSiteConfigModel().getData()).getAsJsonObject());
        }
        if (getPropertyConfigModel() != null && getPropertyConfigModel().getData() != null) {
            jsonObj.add("propertyInfo", new JsonParser().parse(getPropertyConfigModel().getData()).getAsJsonObject());
        }
        if (getCollectionConfigModel() != null && getCollectionConfigModel().getData() != null) {
            jsonObj.add("collectionInfo", new JsonParser().parse(getCollectionConfigModel().getData()).getAsJsonObject());
        }
        if (getCityConfigModel() != null && getCityConfigModel().getData() != null) {
            jsonObj.add("cityInfo", new JsonParser().parse(getCityConfigModel().getData()).getAsJsonObject());
        }
        if (getNeighborhoodConfigModel() != null && getNeighborhoodConfigModel().getData() != null) {
            jsonObj.add("neighborhoodInfo", new JsonParser().parse(getNeighborhoodConfigModel().getData()).getAsJsonObject());
        }
        if (getMetroAreaConfigModel()!= null && getMetroAreaConfigModel().getData() != null) {
            jsonObj.add("metroareaInfo", new JsonParser().parse(getMetroAreaConfigModel().getData()).getAsJsonObject());
        }
        if (getStateConfigModel() != null && getStateConfigModel().getData() != null) {
            jsonObj.add("stateInfo", new JsonParser().parse(getCityConfigModel().getData()).getAsJsonObject());
        }
        return jsonObj.toString();
    }

    protected void resolveConfigurations() {
        //Step 1. StandAlone versus Portfolio
        ConfigPath cp = null;
        if (currentPage.getPath().startsWith("/content/portfolio")) {
            cp = resolvePortfolioConfigurations(request.getRequestPathInfo().getSuffix());
        } else if (currentPage.getPath().startsWith("/content/standalone")) {
            String[] parts = currentPage.getPath().trim().split("/", 0);
            LOG.info("Standalone SiteName:{}", parts[3]);
            cp = resolveStandaloneConfigurations(parts[3]);
        }
        if (cp == null) {
            return; //this is an ERROR
        } else {
            LOG.info("Configuration Paths:{}", cp.toString());
        }
        //now which of the paths are really valid ??
        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);

        //Validate Paths
        siteConfig = (cp.getSiteConfigPath() != null ? pageManager.getPage(cp.getSiteConfigPath()) : null);
        if (siteConfig == null) {
            siteConfig = (cp.getSiteConfigPathSecondary() != null ? pageManager.getPage(cp.getSiteConfigPathSecondary()) : null);
        }
        propertyConfig = (cp.getPropertyConfigPath() != null ? pageManager.getPage(cp.getPropertyConfigPath()) : null);
        cityConfig = (cp.getCityConfigPath() != null ? pageManager.getPage(cp.getCityConfigPath()) : null);
        stateConfig = (cp.getStateConfigPath() != null ? pageManager.getPage(cp.getStateConfigPath()) : null);
        neighborhoodConfig = (cp.getNeighborhoodConfigPath() != null ? pageManager.getPage(cp.getNeighborhoodConfigPath()) : null);
        metroConfig = (cp.getMetroConfigPath() != null ? pageManager.getPage(cp.getMetroConfigPath()) : null);
        collectionConfig = (cp.getCollectionConfigPath() != null ? pageManager.getPage(cp.getCollectionConfigPath()) : null);

        //Now Adapt To
        siteConfigModel = (siteConfig != null ? siteConfig.adaptTo(SiteConfigModel.class) : null);
        propertyConfigModel = (propertyConfig != null ? propertyConfig.adaptTo(PropertyConfigModel.class) : null);
        cityConfigModel = (cityConfig != null ? cityConfig.adaptTo(CityConfigModel.class) : null);
        stateConfigModel = (stateConfig != null ? stateConfig.adaptTo(StateConfigModel.class) : null);
        neighborhoodConfigModel = (neighborhoodConfig != null ? neighborhoodConfig.adaptTo(NeighborhoodConfigModel.class) : null);
        metroAreaConfigModel = (metroConfig != null ? metroConfig.adaptTo(MetroAreaConfigModel.class) : null);
        collectionConfigModel = (collectionConfig != null ? collectionConfig.adaptTo(CollectionConfigModel.class) : null);
        //resolve the content fragment Path

        //content variation
        String firstChoice = "/master";
        Page firstChoicePage = null;
        if (request.getRequestPathInfo().getSuffix() != null) {
            firstChoicePage = pageManager.getPage(cp.getContentFragmentPath() + request.getRequestPathInfo().getSuffix());
        }
        if (firstChoicePage == null) { //suffix is null then check if activeVariation is there
            if (siteConfigModel != null && StringUtils.isNotEmpty(siteConfigModel.getActiveVariation())) {
                firstChoicePage = pageManager.getPage(cp.getContentFragmentPath() + "/" + siteConfigModel.getActiveVariation());
                if (firstChoicePage != null) {
                    firstChoice = "/" + siteConfigModel.getActiveVariation();
                }
            }
        } else {
            firstChoice = request.getRequestPathInfo().getSuffix();
        }
        //it is important to ensure if this fragment is not available we don't send it to the component        
        contentFragmentPath = cp.getContentFragmentPath() + firstChoice;

        //deal with property-type and property-detail
        if (contentFragmentPath != null && pageManager.getPage(contentFragmentPath) == null) {
            contentFragmentPath = null; //no need to send it 
        }
        String pageNameToCheck = currentPage.getName();
        if (1 == request.getRequestPathInfo().getSelectors().length && pageNameToCheck.equalsIgnoreCase(PathConstants.COMMON_CONTENT_PAGE_NAME)) {
            //lets find the resource and make sure its the target 
            String commonResourcePath = PathConstants.COMMON_CONTENT_XF_PATH + request.getRequestPathInfo().getSelectorString() + "/master";
            //if this resource exists then switch pageNameToCheck
            Resource rr = request.getResourceResolver().getResource(commonResourcePath);
            if (rr != null) {
                pageNameToCheck = request.getRequestPathInfo().getSelectorString();
                contentFragmentPathForCommonContent = rr.getPath();
            }

        }
        if (null != pageNameToCheck && request.getRequestPathInfo().getSuffix() != null) {
            if (propertyConfigModel != null) {
                request.setAttribute("propertyId", propertyConfigModel.getName());
                request.setAttribute(NameConstants.PN_DT_NAME, propertyConfigModel.getTitle());
                request.setAttribute("unitTypeOrUnit", request.getRequestPathInfo().getSuffix());
            }
            if (pageNameToCheck.equalsIgnoreCase("apartment-type") && propertyConfigModel != null) {                
                //find unit type images
                //first suffix is groupname and second name is the unit                
                unitTypeImagesModel = request.adaptTo(UnitTypeImagesModel.class);
                if (unitTypeImagesModel != null) {
                    unitTypeKey = unitTypeImagesModel.getUnitTypeKey();
                    peekURL = fetchPeekURLForUnitType(unitTypeImagesModel.getUnitTypeName(), request.getParameter("oneSiteId"));
                }
                if (peekURL == null && getPropertyConfigModel() != null && getPropertyConfigModel().getUnitTypeIdTourMappingMap() != null && !getPropertyConfigModel().getUnitTypeIdTourMappingMap().isEmpty() && getPropertyConfigModel().getUnitTypeIdTourMappingMap().containsKey(unitTypeKey)) {
                    unitTypeTourId = getPropertyConfigModel().getUnitTypeIdTourMappingMap().get(unitTypeKey);
                }  
                //find by unit type name instead of key
                if (unitTypeTourId == null 
                        && peekURL == null 
                        && getPropertyConfigModel() != null 
                        && getPropertyConfigModel().getUnitTypeIdTourMappingMap() != null 
                        && !getPropertyConfigModel().getUnitTypeIdTourMappingMap().isEmpty() && getPropertyConfigModel().getUnitTypeIdTourMappingMap().containsKey(unitTypeImagesModel.getUnitTypeName())) {
                    unitTypeTourId = getPropertyConfigModel().getUnitTypeIdTourMappingMap().get(unitTypeImagesModel.getUnitTypeName());
                }
            } else if (pageNameToCheck.equalsIgnoreCase("apartment") && propertyConfigModel != null) {                           
                unitImagesModel = request.adaptTo(UnitImagesModel.class);
                if (unitImagesModel != null) {
                    peekURL = fetchPeekURLForUnit(unitImagesModel.getUnitName(), request.getParameter("oneSiteId"));
                    unitTypeKey = unitImagesModel.getUnitTypeKey();
                    unitKey = unitImagesModel.getUnitKey();
                    if (peekURL == null && getPropertyConfigModel() != null
                            && getPropertyConfigModel().getUnitIdTourMappingMap() != null
                            && !getPropertyConfigModel().getUnitIdTourMappingMap().isEmpty()) {
                        if (getPropertyConfigModel().getUnitIdTourMappingMap().containsKey(unitKey)) {
                            unitTourId = getPropertyConfigModel().getUnitIdTourMappingMap().get(unitKey);
                        }
                        else if (unitImagesModel.getUnitNumberKey()!= null && getPropertyConfigModel().getUnitIdTourMappingMap().containsKey(unitImagesModel.getUnitNumber())) {
                            unitTourId = getPropertyConfigModel().getUnitIdTourMappingMap().get(unitImagesModel.getUnitNumber());
                        }
                    }
                    if (peekURL == null && unitTourId == null && getPropertyConfigModel() != null
                            && getPropertyConfigModel().getUnitTypeIdTourMappingMap() != null
                            && !getPropertyConfigModel().getUnitTypeIdTourMappingMap().isEmpty()) { //check if we have a tour for apartment type/floor type
                        if (getPropertyConfigModel().getUnitTypeIdTourMappingMap().containsKey(unitImagesModel.getUnitTypeName())) {
                            unitTourId = getPropertyConfigModel().getUnitTypeIdTourMappingMap().get(unitImagesModel.getUnitTypeName());
                        }                       
                    }
                }
            }
        }
    }

    

    protected ConfigPath resolvePortfolioConfigurations(String suffix) {
        LOG.info("resolvePortfolioConfigurations suffix:{}", suffix);
        //city
        ConfigPath cp = new ConfigPath();
        cp.setSiteConfigPath("/content/siteconfig/sites/portfolio/master");
        //if Property or Not
        String[] pagePathParts = currentPage.getPath().split("/"); ///content/portfolio/en/properties/demo/commoncontent
        //the length must be greater than or equals 6 0== empty as the path begins with slash 1== content 2== portfolio 3== en 4 == properties/states/neighborhoods,cities or the page name it self like index 5== the actual property or state or neighborhood if the length is more than 5
        if (pagePathParts.length < 6) {
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/" + currentPage.getName());
            cp.setSiteConfigPathSecondary("/content/siteconfig/sites/portfolio/" + currentPage.getName());
            return cp;
        }
        String contextType = pagePathParts[4]; //properties, neighborhoods 
        String contextName = pagePathParts[5];

        if (contextType.equalsIgnoreCase("properties")) { //property/the-eugene.html
            cp.setPropertyConfigPath("/content/siteconfig/properties/" + contextName);
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/properties/" + contextName);
            cp.setSiteConfigPathSecondary("/content/siteconfig/sites/portfolio/properties/" + contextName);
        } else if (contextType.equalsIgnoreCase("cities")) { //city/oakland.html
            cp.setCityConfigPath("/content/siteconfig/cities/" + contextName);
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/cities/" + contextName);
            cp.setSiteConfigPathSecondary("/content/siteconfig/sites/portfolio/cities/" + contextName);
        } else if (contextType.equalsIgnoreCase("states")) { //states/ca.html
            cp.setStateConfigPath("/content/siteconfig/states/" + contextName);
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/states/" + contextName);
            cp.setSiteConfigPathSecondary("/content/siteconfig/sites/portfolio/states/" + contextName);
        } else if (contextType.equalsIgnoreCase("neighborhoods")) { // neighborhood/oakland.html
            cp.setNeighborhoodConfigPath("/content/siteconfig/neighborhoods/" + contextName);
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/neighborhoods/" + contextName);
            cp.setSiteConfigPathSecondary("/content/siteconfig/sites/portfolio/neighborhoods/" + contextName);
        } else if (contextType.equalsIgnoreCase("collections")) { //collection/the-collection.html -- SUFFIX
            cp.setCollectionConfigPath("/content/siteconfig/collections/" + contextName);
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/collections/" + contextName);
            cp.setSiteConfigPathSecondary("/content/siteconfig/sites/portfolio/collections/" + contextName);
        } else if (contextType.equalsIgnoreCase("metroareas")) { //metro/oakland.html -- SUFFIX
            cp.setMetroConfigPath("/content/siteconfig/metroareas/" + contextName);
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/metroareas/" + contextName);
            cp.setSiteConfigPathSecondary("/content/siteconfig/sites/portfolio/metroareas/" + contextName);
        } else if (contextType.equalsIgnoreCase("states")) {
            cp.setStateConfigPath("/content/siteconfig/states" + contextName);
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/states/" + contextName);
            cp.setSiteConfigPathSecondary("/content/siteconfig/sites/portfolio/states/" + contextName);
        } else {
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/" + contextName);
            cp.setSiteConfigPathSecondary("/content/siteconfig/sites/portfolio/" + contextName);
        }
        return cp;
    }

    /**
     *
     * @param siteName
     * @return
     */
    protected ConfigPath resolveStandaloneConfigurations(String siteName) {
        LOG.info("resolveStandaloneConfigurations siteName:{}", siteName);
        ConfigPath cp = new ConfigPath();
        cp.setSiteConfigPath("/content/siteconfig/sites/standalone/" + siteName);
        cp.setSiteConfigPathSecondary("/content/siteconfig/sites/standalone/master"); //pick it from master if the <<the-site-name>> is not available
        cp.setPropertyConfigPath("/content/siteconfig/properties/" + siteName);
        return cp;
    }

    /**
     *
     * @param nodeName
     * @param valueMap
     * @param prefix
     * @param additionalMap
     */
    protected void populateValueMap(String nodeName, ValueMap valueMap, String prefix, Map<String, Object> additionalMap) {
        if (valueMapFinal == null) {
            valueMapFinal = new HashMap<>();
        }
        if (valueMap == null || valueMap.isEmpty()) {
            if (additionalMap != null && !additionalMap.isEmpty()) {
                valueMapFinal.putAll(additionalMap);
            }
            return;
        }
        valueMapFinal.put(((prefix != null) ? (prefix + "nodeName") : "nodeName"), nodeName);
        if (valueMap.containsKey(JcrConstants.JCR_TITLE)) {
            valueMapFinal
                    .put(((prefix != null) ? (prefix + "name") : "name"), valueMap.get(JcrConstants.JCR_TITLE, String.class
                    ));
        }
        for (String aKey
                : valueMap.keySet()) {
            if (aKey.startsWith("nt:") || aKey.startsWith("cq:") || aKey.startsWith("sling:")) {
                continue;
            }
            String value = valueMap.get(aKey, String.class); //ignore any other type
            if (value == null) {
                continue;
            }
            if (prefix == null) {
                valueMapFinal.put(aKey, value);
            }
            if (prefix != null) {
                valueMapFinal.put(prefix + aKey, value);
            }
        }
        if (additionalMap != null && !additionalMap.isEmpty()) {
            valueMapFinal.putAll(additionalMap);
        }
    }

    /**
     *
     * @return
     */
    public String getJsonLdString() {
        JsonArray arr = null;
        String baseURIString = null;
        String selfAbsolutePath = "";
        try {
            URI baseURI = new URI(request.getScheme(), null, request.getServerName(), request.getServerPort(), null, null, null);
            baseURIString = baseURI.toString();

            URI currentAbsolutePathURI = new URI(request.getScheme(), null, request.getServerName(), request.getServerPort(), request.getRequestURI(), null, null);
            selfAbsolutePath = currentAbsolutePathURI.toString();
        } catch (URISyntaxException e) {
            LOG.error(null, e);
        }

        if (this.propertyConfigModel != null) {
            JsonObject p = propertyConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }

        if (this.siteConfigModel != null) {
            JsonObject p = siteConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }
        if (this.neighborhoodConfigModel != null) {
            JsonObject p = neighborhoodConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }
        if (this.metroAreaConfigModel != null) {
            JsonObject p = metroAreaConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }
        if (this.cityConfigModel != null) {
            JsonObject p = cityConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }
        return arr == null ? null : arr.toString().replaceAll("BASE_SERVER_URL", baseURIString).replaceAll("SELF", selfAbsolutePath);
    }

    public String getContentFragmentPath() {
        return contentFragmentPath;
    }

    public String getContentFragmentPathForCommonContent() {
        return contentFragmentPathForCommonContent;
    }

}
