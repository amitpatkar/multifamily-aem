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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class)
public class PropertyConfigListModel {

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @ResourcePath(path = "/content/portfolio/en/states")
    Resource statesContentFolder;

    @ResourcePath(path = "/content/siteconfig/states")
    Resource statesFolder;

    @ResourcePath(path = "/content/siteconfig/properties")
    Resource propertiesFolder;

    private List<PropertyConfigModel> propertyConfigModels;
    
    private static String CONTENT_PROPERTIES_PATH = "/content/portfolio/en/properties/";

    @PostConstruct
    protected void init() {
        if (propertiesFolder != null && propertiesFolder.hasChildren()) {
            propertyConfigModels = new ArrayList<>();
            for (Resource childResource : propertiesFolder.getChildren()) {
                if (childResource.getName().equals(JcrConstants.JCR_CONTENT)) continue;
                PropertyConfigModel propertyConfigModel = childResource.adaptTo(PropertyConfigModel.class);
                if(propertyConfigModel!=null && StringUtils.isNotEmpty(propertyConfigModel.getOnesiteID()) && isPropertyPageExist(childResource)){
                    propertyConfigModels.add(propertyConfigModel);
                }
                //propertyConfigModels.add(propertyConfigModel);
            }
        }
    }

    private boolean isPropertyPageExist(Resource childResource) {
		ResourceResolver resolver = childResource.getResourceResolver();
		return null != resolver.getResource(CONTENT_PROPERTIES_PATH + childResource.getName());
	}

	public List<PropertyConfigModel> getPropertyConfigModels() {
        return Optional.ofNullable(propertyConfigModels).map(List::stream).orElseGet(Stream::empty).collect(Collectors.toList());
    }

    public List<PropertyConfigModel> getSortedPropertyConfigModels() {
        return Optional.ofNullable(propertyConfigModels).map(List::stream).orElseGet(Stream::empty).sorted(Comparator.comparing(PropertyConfigModel::getName)).collect(Collectors.toList());
    }

    public String getSortedPropertyConfigModelsAsJSONString() {
        List<PropertyConfigModel> configModels = Optional.ofNullable(propertyConfigModels).map(List::stream).orElseGet(Stream::empty).sorted(Comparator.comparing(PropertyConfigModel::getTitle)).collect(Collectors.toList());
        JsonObject jo = new JsonObject();

        JsonArray ja = new JsonArray();
        for (PropertyConfigModel configModel : configModels) {
            ja.add(new Gson().fromJson(configModel.getData(), JsonObject.class));
        }
        jo.add("properties", ja);

        if (statesFolder != null && statesFolder.hasChildren()) {
            JsonArray jas = new JsonArray();
            for (Resource r : statesFolder.getChildren()) {
                if (r.getName().equals(JcrConstants.JCR_CONTENT)) continue;
                if (statesContentFolder.getChild(r.getName()) != null) {
                    StateConfigModel sm = r.adaptTo(StateConfigModel.class);
                    jas.add(sm.getDataAsObject());
                }
            }
            jo.add("states", jas);
        }
        return jo.toString();
    }

}
