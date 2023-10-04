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
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Model(adaptables = SlingHttpServletRequest.class)
public class CommonContentRequestModel {
    final static Logger LOG = LoggerFactory.getLogger(CommonContentRequestModel.class);
    final SlingHttpServletRequest request;

    @ValueMapValue(name=PROPERTY_RESOURCE_TYPE, injectionStrategy=InjectionStrategy.OPTIONAL)
    @Default(values="No resourceType")
    protected String resourceType;

    
    String selectorString;    
    String resourceToFindPath;
    
    
    public CommonContentRequestModel(final SlingHttpServletRequest request) {
        this.request = request;
    }
    
    @PostConstruct
    protected void init() {
        selectorString = request.getRequestPathInfo().getSelectorString();        
        resourceToFindPath = PathConstants.COMMON_CONTENT_XF_PATH + selectorString + "/master";        
        LOG.debug("Checking for Resource :{} ",resourceToFindPath);
        Resource resourceToFind = request.getResourceResolver().getResource(resourceToFindPath);
        if (resourceToFind == null) {
            resourceToFindPath  = null; //reset the path no point sending it back      
            LOG.debug("Unable to find Resource :{}",resourceToFindPath);
            //second chance
            PageInfoRequestModel pageInfo = request.adaptTo(PageInfoRequestModel.class);
            if (null != pageInfo && null != pageInfo.getCurrentPage()) {
                resourceToFindPath = PathConstants.COMMON_CONTENT_XF_PATH + pageInfo.getCurrentPage().getName() + "/master";
                resourceToFind = request.getResourceResolver().getResource(resourceToFindPath);
            }
            if (resourceToFind == null) {
                resourceToFindPath  = null;
            }
        }
    }

    public String getResourceToFindPath() {
        return resourceToFindPath;
    }        
}
