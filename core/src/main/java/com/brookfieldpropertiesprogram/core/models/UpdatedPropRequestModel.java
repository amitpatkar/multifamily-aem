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

import com.brookfieldpropertiesprogram.core.services.EnvironmentSetting;
import com.brookfieldpropertiesprogram.core.utils.ValueMapUtils;
import com.day.cq.dam.api.DamConstants;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {SlingHttpServletRequest.class})
public class UpdatedPropRequestModel {

    private final static Logger LOG = LoggerFactory.getLogger(UpdatedPropRequestModel.class);
    
    @OSGiService     
    EnvironmentSetting environmentSetting; 

    final SlingHttpServletRequest request;

    final Map<String, Object> finalMap = new HashMap<>();

    public UpdatedPropRequestModel(SlingHttpServletRequest request) {
        this.request = request;
    }

    @PostConstruct
    public void init() {
        substituteString();        
    }

    /**
     * We take the currentResource Properties and then add the SiteConfig
     * Properties too SiteConfiguration can also use substitutions
     */
    protected void substituteString() {
        PageInfoRequestModel pageInfoRequestModel = request.adaptTo(PageInfoRequestModel.class);
        if (pageInfoRequestModel == null || pageInfoRequestModel.getPageInfoModel() == null || pageInfoRequestModel.getCombinedProps() == null || pageInfoRequestModel.getSiteConfigModel() == null) {
            finalMap.putAll(request.getResource().getValueMap());
        }
        else {
            boolean shouldPrefixImageService = false;
            if (environmentSetting != null
                    && pageInfoRequestModel.getSiteConfigModel() != null
                    && StringUtils.isNotEmpty(pageInfoRequestModel.getSiteConfigModel().getImageServiceUrl())
                    && environmentSetting.isPublishServer()                    
                ) {
                shouldPrefixImageService = true;
            }
            HashMap<String,Object> combinedHashMapWithResource = new HashMap(request.getResource().getValueMap());
            combinedHashMapWithResource.putAll(pageInfoRequestModel.getSiteConfigModel().getCurrentResource().getChild(JcrConstants.JCR_CONTENT).getValueMap());
                //pageInfoRequestModel.getSiteConfigModel().getCurrentResource().getChild(JcrConstants.JCR_CONTENT).getValueMap()

            ValueMap combinedValueMapWithResource = new ValueMapDecorator(combinedHashMapWithResource);
            for (String aKey : combinedValueMapWithResource.keySet()) {
                if (aKey.startsWith("jcr:") || aKey.startsWith("cq:") || aKey.startsWith("sling:") || aKey.startsWith("nt:")) {
                    continue;
                }
                String valueToParse = combinedValueMapWithResource.get(aKey, String.class);
                if (valueToParse != null) {
                    String finalString = ValueMapUtils.substitute(valueToParse, pageInfoRequestModel.getCombinedProps());
                    if (shouldPrefixImageService && finalString.startsWith(DamConstants.MOUNTPOINT_ASSETS)) {
                        finalString = pageInfoRequestModel.getSiteConfigModel().getImageServiceUrl() +  finalString; //this is to refix the content dam urls with that of image server i.e. cloudinary
                    }
                    //LOG.debug("Substitution aKey:{} valueToParse:{} finalString:{} ", aKey, valueToParse, finalString);
                    finalMap.put(aKey, finalString);
                }
            }            
        }        
    }

    public ValueMap getProperties() {
        return new ValueMapDecorator(finalMap);
    }
}
