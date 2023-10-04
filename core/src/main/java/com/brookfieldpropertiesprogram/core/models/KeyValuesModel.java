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
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class KeyValuesModel {

    private static final Logger LOG = LoggerFactory.getLogger(KeyValuesModel.class);
    final Resource resource;

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String keyValuesAsTextArea;

    private HashMap<String,String> keyValues;


    public KeyValuesModel(Resource resource) {
        this.resource = resource;        
    }
    
    @PostConstruct
    protected void init() {
        if (StringUtils.isNotEmpty(keyValuesAsTextArea)) {
            String[] parts = keyValuesAsTextArea.split(System.lineSeparator());
            for (String aPart:parts) {
                String[] fp = aPart.split("=");
                if (fp.length !=2 && fp.length != 1) continue;
                if (keyValues == null) keyValues = new HashMap<>();
                if (fp.length == 1) {
                    keyValues.put(fp[0].trim(),fp[0].trim()); // use 
                }
                else {
                    keyValues.put(fp[0].trim(),fp[1].trim());
                }
            }
        }
        Resource r = resource.getChild(JcrConstants.JCR_CONTENT).getChild("keyValues");
        if (r != null && r.hasChildren()) {
            for (Resource c: r.getChildren()) {                
                if (keyValues == null) keyValues = new HashMap<>();
                keyValues.put(c.getValueMap().get("key",String.class),c.getValueMap().get("value",String.class));
            }
        }
    }

    public Map<String, String> getKeyValues() {
        if (keyValues == null) return null;
        Set<Map.Entry<String, String>> entries = keyValues.entrySet();
        return (HashMap<String, String>) entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    

   
}
