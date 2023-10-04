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

import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
public class GlobalConfigRequestModel {

    final static Logger LOG = LoggerFactory.getLogger(GlobalConfigRequestModel.class);

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;
    
    private String catchAllContactUsEmail;
    
    private String googleMapsAPIKey;

    private final Resource resource;
    
    private final SlingHttpServletRequest request;

    @ResourcePath(path = "/conf/common/settings/wcm/policies/brookfieldpropertiesprogram/components/body_end_javascripts/policy_body_end_javascripts")
    Resource otherConfigItemsParentResource;

    @ResourcePath(path = "/conf/common/settings/wcm/policies/brookfieldpropertiesprogram/components/body_end_javascripts/policy_body_end_javascripts/otherConfigItems")
    Resource otherConfigItemsResource;

    public GlobalConfigRequestModel(Resource resource) {
        this.resource = resource;
        this.request = null;
    }
    
    public GlobalConfigRequestModel(SlingHttpServletRequest request) {
        this.request = request;
        this.resource = null;
    }

    @PostConstruct
    protected void init() {
        if (otherConfigItemsParentResource != null) {
            catchAllContactUsEmail = otherConfigItemsParentResource.getValueMap().get("catchAllContactUsEmail",String.class);
            googleMapsAPIKey = otherConfigItemsParentResource.getValueMap().get("googleMapsAPIKey",String.class);                    
        }
    }

    public String getCatchAllContactUsEmail() {
        return catchAllContactUsEmail;
    }

    public String getGoogleMapsAPIKey() {
        return googleMapsAPIKey;
    }   
}
