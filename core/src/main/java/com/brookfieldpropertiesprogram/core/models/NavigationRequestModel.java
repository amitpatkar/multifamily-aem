/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.brookfieldpropertiesprogram.core.models;

import com.day.cq.wcm.api.Page;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {SlingHttpServletRequest.class})
public class NavigationRequestModel {
    final static Logger LOG = LoggerFactory.getLogger(NavigationRequestModel.class);
    @Self
    private SlingHttpServletRequest request;
    
    @ScriptVariable
    private Page currentPage;

    NavigationComponentModel componentModel;
    
    

    @PostConstruct
    protected void initModel() {        
        componentModel = request.getResource().adaptTo(NavigationComponentModel.class);     
        if (componentModel != null && componentModel.getMenuLinks() != null && componentModel.getMenuLinks().size() > 0) {
            componentModel.getMenuLinks().stream().filter((link) -> ( link.getUrl() != null && currentPage.getPath().contains(link.getUrl()))).forEachOrdered((link) -> {
                link.setActive("active");
            });
        }
    }

    public String getCurrentPagePath() {
        return currentPage.getPath();
    }
    
    

    public NavigationComponentModel getComponentModel() {
        return componentModel;
    }

    

}

