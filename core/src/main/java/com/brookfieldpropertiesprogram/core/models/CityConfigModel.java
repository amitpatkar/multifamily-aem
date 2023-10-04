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

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
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
public class CityConfigModel {

    private static final Logger LOG = LoggerFactory.getLogger(CityConfigModel.class);
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

    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String theme;

    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    @Named("activeVariation")
    private String activeVariation;

    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    @Named("destinationPage")
    private String destinationPage;

    @ResourcePath(path = "/content/siteconfig/states")
    Resource statesFolder;

    String stateCode;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    String associatedState;

    @ResourcePath(path = "/content/siteconfig/neighborhoods")
    Resource neighborhoodsFolder;

    @ResourcePath(path = "/content/siteconfig/properties")
    Resource propertiesFolder;

    List<Resource> neighborhoods;
    List<Resource> properties;
    List<PropertyConfigModel> propertyConfigModels;

    private final ValueMap valueMap;

    public CityConfigModel(Resource resource) {
        this.resource = resource;
        valueMap = resource.getChild(JcrConstants.JCR_CONTENT).adaptTo(ValueMap.class);
    }

    @PostConstruct
    protected void init() {
        //PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        /*
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(currentResource))
                .map(Page::getPath).orElse("");       
         */
        activeVariation = valueMap.get("activeVariation", String.class);
        theme = valueMap.get("theme", String.class);

        if (propertiesFolder != null) {
            for (Resource r : propertiesFolder.getChildren()) {
                if (!StringUtils.equalsIgnoreCase(r.getResourceType(), NameConstants.NT_PAGE)) {
                    continue;
                }
                Page p = r.adaptTo(Page.class);
                String resourcePath = p.getContentResource().getValueMap().get("associatedCity", String.class);
                if (StringUtils.isEmpty(resourcePath)) {
                    continue;
                }
                if (resourcePath.equals(resource.getPath())) {
                    if (properties == null) {
                        properties = new ArrayList<>();
                    }
                    if (propertyConfigModels == null) {
                        propertyConfigModels = new ArrayList<>();
                    }
                    properties.add(r);
                    propertyConfigModels.add(r.adaptTo(PropertyConfigModel.class));
                }
            }
        }

        if (neighborhoodsFolder != null && neighborhoodsFolder.hasChildren()) {
            for (Resource r : neighborhoodsFolder.getChildren()) {
                if (!StringUtils.equalsIgnoreCase(r.getResourceType(), NameConstants.NT_PAGE)) {
                    continue;
                }
                Page p = r.adaptTo(Page.class);
                if (null != p) {
                    ValueMap vp = p.getContentResource().getValueMap();
                    if (null != vp && vp.containsKey("associatedCity")) {
                        String resourcePath = p.getContentResource().getValueMap().get("associatedCity", String.class);
                        if (StringUtils.isEmpty(resourcePath)) {
                            continue;
                        }
                        if (resourcePath.equals(resource.getPath())) {
                            if (neighborhoods == null) {
                                neighborhoods = new ArrayList<>();
                            }
                            neighborhoods.add(r);
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotEmpty(associatedState)) {
            String parts[] = associatedState.split("[/]");
            Resource resourceState = statesFolder.getChild(parts[parts.length - 1]);
            if (resourceState != null) {
                Page pageState = resourceState.adaptTo(Page.class);
                if (pageState != null) {
                    this.stateCode = resourceState.getName();
                }
            }
        }
    }

    public List<PropertyConfigModel> getPropertyConfigModels() {
        return (propertyConfigModels != null ? new ArrayList<>(propertyConfigModels) : null);
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

    public String getTheme() {
        return theme;
    }

    public String getActiveVariation() {
        return activeVariation;
    }

    public ValueMap getValueMap() {
        return valueMap;
    }

    public JsonObject getDataAsObject() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", resource.getName());
        jsonObj.addProperty("name", getTitle());
        jsonObj.addProperty("state", StringUtils.isNotEmpty(stateCode) ? stateCode : ""); 
        jsonObj.addProperty("href", "/cities/" + (StringUtils.isNotEmpty(destinationPage) ? destinationPage : resource.getName()) + ".html");
        if (properties != null) {
            JsonArray a = new JsonArray();
            for (Resource r : properties) {
                PropertyConfigModel p = r.adaptTo(PropertyConfigModel.class);
                if (p != null) {
                    a.add(p.getDataAsObject());
                }
            }
            jsonObj.add("properties", a);
        }

        if (neighborhoods != null) {
            JsonArray a = new JsonArray();
            for (Resource r : neighborhoods) {
                NeighborhoodConfigModel n = r.adaptTo(NeighborhoodConfigModel.class);
                if (n != null) {
                    a.add(n.getDataAsObject());
                }
            }
            jsonObj.add("neighborhoods", a);
        }
        //Analytics        
        return jsonObj;
    }

    public String getData() {

        //Analytics        
        return getDataAsObject().toString();
    }

    /**
     *
     * @return https://schema.org/City
     */
    public JsonObject getJsonLdObject() {
        return new JsonObject();
    }

    public String getName() {
        return resource.getName();
    }

    public String getDestinationPage() {
        return destinationPage;
    }

}
