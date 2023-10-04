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
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.wcm.api.components.ComponentContext;
import com.google.gson.Gson;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

@Model(adaptables = {SlingHttpServletRequest.class})
public class DataLayerRequestModel {

    final static Logger LOG = LoggerFactory.getLogger(DataLayerRequestModel.class);

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @ScriptVariable
    private ComponentContext componentContext;

    private final SlingHttpServletRequest request;
    private final ValueMap valueMap;
    
    String data = "{}";

    public DataLayerRequestModel(SlingHttpServletRequest request) {
        this.request = request;
        valueMap = request.getResource().adaptTo(ValueMap.class);
    }

    @PostConstruct
    protected void init() {
        //we will implement data layer for each component using a factory configuration for now lets use if then else 
        PageInfoRequestModel pr = request.adaptTo(PageInfoRequestModel.class);
        if (pr == null || pr.getSiteConfigModel() == null) {
            return;
        }
        Map<String, Object> properties = new HashMap<>();

        properties.put("@type", request.getResource().getResourceType());
        properties.put("name", request.getResource().getName());
        switch (request.getResource().getResourceType()) {
            case "brookfieldpropertiesprogram/components/navigation_standalone": {
                getDataLayerForNavigationStandalone(pr, properties);
                break;
            }
            case "brookfieldpropertiesprogram/components/footer_standalone": {
                getDataLayerForFooterStandalone(pr, properties);
                break;
            }            
            case "brookfieldpropertiesprogram/components/apt_promo": {
                getDataLayerForAptPromo(pr, properties);
                break;
            }
            case "brookfieldpropertiesprogram/components/accordion": {
                getDataLayerForAccordion(pr, properties);
                break;
            }
            default: {
                LOG.warn("not implemented");
            }
            
        }
        //Use AEM Core Component utils to get a unique identifier for the Byline component (in case multiple are on the page)
        String bylineComponentID = ComponentUtils.getId(request.getResource(), pr.getCurrentPage(), componentContext);
        // Return the bylineProperties as a JSON String with a key of the bylineResource's ID
        Gson gson = new Gson();
            data = String.format("{\"%s\":%s}",
                    bylineComponentID,
                    // Use the ObjectMapper to serialize the bylineProperties to a JSON string
                    gson.toJson(properties));       
    }

    public String getData() {
        return data;
    }        

    protected void getDataLayerForNavigationStandalone(PageInfoRequestModel pr, Map<String, Object> properties) {

        properties.put("headerMenuLeftTotal", pr.getSiteConfigModel().getHeaderMenuLinksLeft() == null ? 0 : pr.getSiteConfigModel().getHeaderMenuLinksLeft().size());
        properties.put("headerMenuRightTotal", pr.getSiteConfigModel().getHeaderMenuLinksRight() == null ? 0 : pr.getSiteConfigModel().getHeaderMenuLinksRight().size());
    }
    
    protected void getDataLayerForFooterStandalone(PageInfoRequestModel pr, Map<String, Object> properties) {
        properties.put("footerMenuLeftTotal", pr.getSiteConfigModel().getFooterMenuLinksLeft() == null ? 0 : pr.getSiteConfigModel().getFooterMenuLinksLeft().size());
        properties.put("footerMenuRightTotal", pr.getSiteConfigModel().getFooterMenuLinksRight() == null ? 0 : pr.getSiteConfigModel().getFooterMenuLinksRight().size());
    }
    
    protected void getDataLayerForAptPromo(PageInfoRequestModel pr, Map<String, Object> properties) {
        if (pr.getPropertyConfigModel() == null) return;        
        properties.put("specials", pr.getPropertyConfigModel().getSpecials());        
    }
    
    protected void getDataLayerForAccordion(PageInfoRequestModel pr, Map<String, Object> properties) {
        if (pr.getPropertyConfigModel() == null) return;         
        com.brookfieldpropertiesprogram.core.models.NavigationRequestModel model = request.adaptTo(com.brookfieldpropertiesprogram.core.models.NavigationRequestModel.class);
        properties.put("title", valueMap.get("title",""));     
        if (model != null && model.getComponentModel() != null && model.getComponentModel().getMenuLinks() != null) {
            properties.put("total", valueMap.get("total",model.getComponentModel().getMenuLinks().size()));  
            int i = 1;
            for (NavigationLink navigationLink:model.getComponentModel().getMenuLinks()) {
                properties.put("title_" +i++, navigationLink.getLabel());  
            }
        }
    }
 
}
