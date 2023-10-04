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
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class SearchInputFieldModel {

    private static final Logger LOG = LoggerFactory.getLogger(SearchInputFieldModel.class);
    final Resource resource;

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @ResourcePath(path = "/content/siteconfig/states", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentStates;

    @ResourcePath(path = "/content/siteconfig/cities", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentCities;

    @ResourcePath(path = "/content/siteconfig/neighborhoods", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentNeighborhoods;

    @ResourcePath(path = "/content/siteconfig/metroareas", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentMetroAreas;

    @ResourcePath(path = "/content/siteconfig/properties", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentProperties;

    @ResourcePath(path = "/content/siteconfig/collections", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentCollections;

    @ResourcePath(path = "/content/portfolio/en/states", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentContentStates;

    @ResourcePath(path = "/content/portfolio/en/cities", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentContentCities;

    @ResourcePath(path = "/content/portfolio/en/neighborhoods", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentContentNeighborhoods;

    @ResourcePath(path = "/content/portfolio/en/metroareas", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentContentMetroAreas;

    @ResourcePath(path = "/content/portfolio/en/properties", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentContentProperties;

    @ResourcePath(path = "/content/portfolio/en/collections", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource parentContentCollections;

    @Optional
    @Inject
    List<NavigationLink> additionalSearchTerms;

    String data;

    public SearchInputFieldModel(Resource resource) {
        this.resource = resource;
    }

    @PostConstruct
    protected void init() {
        JsonObject jo = new JsonObject();
        fillJsonArray("states", jo, parentStates, parentContentStates);
        fillJsonArray("cities", jo, parentCities, parentContentCities);
        fillJsonArray("neighborhoods", jo, parentNeighborhoods, parentContentNeighborhoods);
        fillJsonArray("properties", jo, parentProperties, parentContentProperties);
        fillJsonArray("collections", jo, parentCollections, parentContentCollections);
        //fillJsonArray("metroareas", jo, parentMetroAreas, parentContentMetroAreas); i don't think we need metro to be searchable

        //additional search terms
        JsonArray joStates = new JsonArray();
        jo.add("additionalSearchTerms", joStates);
        if (additionalSearchTerms != null && additionalSearchTerms.size() > 0) {
            for (NavigationLink link : additionalSearchTerms) {
                if (StringUtils.isEmpty(link.getLabel())) {
                    break;
                }
                String[] parts = link.getLabel().split("[ " + System.lineSeparator() + "]");
                for (String aPart : parts) {
                    JsonObject joN = new JsonObject();
                    joN.addProperty("id", aPart.trim());
                    joN.addProperty("title", aPart.trim());
                    joN.addProperty("href", link.getAbsUrl().replaceAll(PathConstants.PORTFOLIO_BASE, ""));
                    joStates.add(joN);
                }
            }
        }
        data = jo.toString();
    }

    protected void fillJsonArray(String theKey, JsonObject jo, Resource parentResource, Resource parentContentResource) {
        if (parentResource != null && parentResource.hasChildren()) {
            JsonArray joStates = new JsonArray();
            jo.add(theKey, joStates);
            for (Resource aResource : parentResource.getChildren()) {
                if (!aResource.getResourceType().equals(NameConstants.NT_PAGE)) {
                    continue;
                }
                Page aPage = aResource.adaptTo(Page.class);
                if (aPage == null) {
                    continue;
                }
                String destination = null;
                if (aPage.getContentResource().getValueMap().get("destinationPage", String.class) != null) {
                    destination = aPage.getContentResource().getValueMap().get("destinationPage", String.class).replaceAll(PathConstants.PORTFOLIO_BASE_WITH_SLASH, "") + ".html";
                } else if (parentContentResource != null && parentContentResource.getChild(aResource.getName()) != null) {
                    destination =  theKey + "/" + aResource.getName() + ".html";
                } else {
                    continue;
                }

                JsonObject joS = new JsonObject();
                joS.addProperty("id", aResource.getName());
                if (theKey.equals("cities")) {
                    String statePath = aPage.getProperties().get("associatedState", String.class);
                    String stateCode = "";
                    if (StringUtils.isNotEmpty(statePath)) {
                        String[] statePathParts = statePath.split("[/]");
                        stateCode = statePathParts[statePathParts.length - 1].toUpperCase();
                        joS.addProperty("stateCode", stateCode);
                    }
                    joS.addProperty("title", aPage.getTitle());
                } else {
                    joS.addProperty("title", aPage.getTitle());
                }

                joS.addProperty("href", destination);
                joStates.add(joS);
            }
        }
    }

    public String getData() {
        return data;
    }

}
