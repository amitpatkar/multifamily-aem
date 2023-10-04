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

import com.day.cq.wcm.api.Page;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class AssociationConfigModel {

    private static final Logger LOG = LoggerFactory.getLogger(AssociationConfigModel.class);
    final Resource resource;

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @ResourcePath(name = "associatedState", injectionStrategy = InjectionStrategy.OPTIONAL)
    private Resource associatedState;

    @ResourcePath(name = "associatedCity", injectionStrategy = InjectionStrategy.OPTIONAL)
    private Resource associatedCity;

    @ResourcePath(name = "associatedCollections", injectionStrategy = InjectionStrategy.OPTIONAL)
    private Resource[] associatedCollections;

    @ResourcePath(name = "associatedCities", injectionStrategy = InjectionStrategy.OPTIONAL)
    private Resource[] associatedCities;

    String stateCode;
    String stateName;
    String city;

    @ResourcePath(name = "associatedProperties", injectionStrategy = InjectionStrategy.OPTIONAL)
    Resource[] associatedProperties;

    List<PropertyConfigModel> propertyConfigModels;
    List<CollectionConfigModel> collectionConfigModels;

    public AssociationConfigModel(Resource resource) {
        this.resource = resource;
    }

    @PostConstruct
    protected void init() {
        if (associatedProperties != null && associatedProperties.length > 0) {
            for (Resource aResource : associatedProperties) {
                PropertyConfigModel configModel = aResource.adaptTo(PropertyConfigModel.class);
                if (configModel != null) {
                    if (propertyConfigModels == null) {
                        propertyConfigModels = new ArrayList<>();
                    }
                    propertyConfigModels.add(configModel);
                } else {
                    LOG.warn("Unable to case it to PropertyConfigModel:{}", aResource.getPath());
                }
            }
        }
        if (associatedState != null) {
            Page aPage = associatedState.adaptTo(Page.class);
            if (aPage != null) {
                stateCode = aPage.getName();
                stateName = aPage.getTitle();
            }
        }
        if (associatedCity != null) {
            Page aPage = associatedCity.adaptTo(Page.class);
            if (aPage != null) {
                city = aPage.getTitle();
            }
        }

        if (associatedCities != null) {
            for (Resource aResource : associatedCities) {
                CollectionConfigModel configModel = aResource.adaptTo(CollectionConfigModel.class);
                if (configModel != null) {
                    if (collectionConfigModels == null) {
                        collectionConfigModels = new ArrayList<>();
                    }
                    collectionConfigModels.add(configModel);
                } else {
                    LOG.warn("Unable to case it to CollectionConfigModel:{}", aResource.getPath());
                }
            }
        }

        if (associatedCollections != null) {
            for (Resource aResource : associatedCollections) {
                CollectionConfigModel configModel = aResource.adaptTo(CollectionConfigModel.class);
                if (configModel != null) {
                    if (collectionConfigModels == null) {
                        collectionConfigModels = new ArrayList<>();
                    }
                    collectionConfigModels.add(configModel);
                } else {
                    LOG.warn("Unable to case it to CollectionConfigModel:{}", aResource.getPath());
                }
            }
        }
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public String getCity() {
        return city;
    }

    public List<CollectionConfigModel> getCollectionConfigModels() {
        return collectionConfigModels != null ? new ArrayList<>(collectionConfigModels) : null;
    }

    public List<PropertyConfigModel> getPropertyConfigModels() {
        return propertyConfigModels != null ? new ArrayList<>(propertyConfigModels) : null;
    }
}
