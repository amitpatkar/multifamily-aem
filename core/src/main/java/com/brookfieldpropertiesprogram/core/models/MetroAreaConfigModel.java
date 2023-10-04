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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class MetroAreaConfigModel {

    private static final Logger LOG = LoggerFactory.getLogger(MetroAreaConfigModel.class);
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
    
    AssociationConfigModel associatedPropertiesConfigModel;

    private final ValueMap valueMap;

    public MetroAreaConfigModel(Resource resource) {
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
        if (this.resource.getChild(JcrConstants.JCR_CONTENT) != null) associatedPropertiesConfigModel = this.resource.getChild(JcrConstants.JCR_CONTENT).adaptTo(AssociationConfigModel.class);
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

    public String getData() {
        //Analytics                
        return getDataAsObject().toString();
    }

    public AssociationConfigModel getAssociatedPropertiesConfigModel() {
        return associatedPropertiesConfigModel;
    }        
    
    public JsonObject getDataAsObject() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", resource.getName());
        jsonObj.addProperty("name", getTitle());
        if (associatedPropertiesConfigModel != null && associatedPropertiesConfigModel.getPropertyConfigModels() != null && !associatedPropertiesConfigModel.getPropertyConfigModels().isEmpty()) {
            JsonArray jsonAray = new JsonArray();
            for (PropertyConfigModel configModel:associatedPropertiesConfigModel.getPropertyConfigModels()) {
                jsonAray.add(new JsonParser().parse(configModel.getData()).getAsJsonObject());
            }
            jsonObj.add("properties",jsonAray);
        }
        //Analytics                
        return jsonObj;
    }

    
    /**
     * https://schema.org/Place
     * @return 
     */
    public JsonObject getJsonLdObject() {
        return null;
    }

    public String getName() {
        return resource.getName();
    }

    public String getDestinationPage() {
        return destinationPage;
    }        

}
