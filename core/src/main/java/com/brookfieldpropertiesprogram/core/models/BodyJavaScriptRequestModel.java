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

import com.brookfieldpropertiesprogram.core.utils.ValueMapUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {SlingHttpServletRequest.class})
public class BodyJavaScriptRequestModel {

    final static Logger LOG = LoggerFactory.getLogger(BodyJavaScriptRequestModel.class);

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    private final SlingHttpServletRequest request;

    @Inject
    @Optional
    Object[] scriptKeys;

    @Inject
    @Optional
    Object[] scriptKeyValues;

    @Inject
    @Source("request-attributes")
    @Optional
    List<Object> scriptKeyList;

    @Inject
    @Source("request-attributes")
    @Optional
    Map<Object, Object> scriptValueMap;

    List<String> scripts;

    @ResourcePath(path = "/conf/common/settings/wcm/policies/brookfieldpropertiesprogram/components/body_end_javascripts/policy_body_end_javascripts/bodyJavascript")
    Resource bodyJavascriptResource;

    public BodyJavaScriptRequestModel(SlingHttpServletRequest resource) {
        this.request = resource;
    }

    @PostConstruct
    protected void init() {
        //LOG.info("Script Keys");
        if (scriptKeys != null && scriptKeys.length > 0) {
            int i = 0;
            for (Object aKey : scriptKeys) {
                //LOG.debug("Adding Key:{} to Request", aKey);
                if (scriptKeyList == null) {
                    scriptKeyList = new ArrayList<>();
                }
                scriptKeyList.add(aKey);
                if (scriptKeyValues != null && scriptKeyValues.length > i) {
                    String v = scriptKeyValues[i].toString();
                    if (StringUtils.isNotEmpty(v)) {
                        if (scriptValueMap == null) {
                            scriptValueMap = new HashMap<>();
                        }
                        scriptValueMap.put(aKey, v);
                    }
                }
                i++;

            }
            request.setAttribute("scriptKeyList", scriptKeyList);
            request.setAttribute("scriptValueMap", scriptValueMap);
        }
    }

    public List<Object> getScriptKeyList() {
        return scriptKeyList;
    }

    public List<String> getScripts() {
        if (bodyJavascriptResource == null || !bodyJavascriptResource.hasChildren()) {
            return null;
        }
        PageInfoRequestModel pageInfoRequestModel = request.adaptTo(PageInfoRequestModel.class);
        for (Resource childResource : bodyJavascriptResource.getChildren()) {
            ValueMap childResourceMap = childResource.adaptTo(ValueMap.class);
            if (childResourceMap == null) {
                continue;
            }
            String key = childResourceMap.get("key", String.class);
            String value = childResourceMap.get("value", String.class);
            String runOnAllPages = childResourceMap.get("runOnAllPages", "false");
            String runIfNotNull = childResourceMap.get("runIfNotNull", String.class);
            boolean shouldSkip = false;
            if (runIfNotNull != null && StringUtils.isNotEmpty(runIfNotNull)) {
                shouldSkip = (pageInfoRequestModel.getCombinedProps() == null
                        || !pageInfoRequestModel.getCombinedProps().containsKey(runIfNotNull)
                        || StringUtils.isEmpty(pageInfoRequestModel.getCombinedProps().get(runIfNotNull, String.class)));
            }
            if (!shouldSkip && StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                if (runOnAllPages.equalsIgnoreCase("true") || (scriptKeyList != null && scriptKeyList.contains(key))) {
                    if (scripts == null) {
                        scripts = new ArrayList<>();
                    }
                    String retValue = value;
                    if (pageInfoRequestModel != null && pageInfoRequestModel.getCombinedProps() != null) {
                        retValue = ValueMapUtils.substitute(value, pageInfoRequestModel.getCombinedProps());
                    }
                    scripts.add(retValue);
                }
            }
        }
        //hack for Search Input Field
        if (scriptKeyList != null && scriptKeyList.contains("SearchInputFieldModel")) {
            String data = null;
            if (scriptValueMap != null && scriptValueMap.size() > 0 && scriptValueMap.containsKey("SearchInputFieldModel")) {
                data = scriptValueMap.get("SearchInputFieldModel").toString();
            } else {
                SearchInputFieldModel searchInputFieldModel = bodyJavascriptResource.adaptTo(SearchInputFieldModel.class);
                if (searchInputFieldModel != null) {
                    data = searchInputFieldModel.getData();
                }
            }

            scripts.add("<script>digitalData.searchInputFieldModel=" + data + "</script>");
        }
        //hack for Search Input Field
        if (scriptKeyList != null && scriptKeyList.contains("PropertyConfigListModel")) {
            PropertyConfigListModel propertyConfigListModel = bodyJavascriptResource.adaptTo(PropertyConfigListModel.class);
            scripts.add("<script>digitalData.propertyList=" + propertyConfigListModel.getSortedPropertyConfigModelsAsJSONString() + "</script>");
        }
        return scripts;
    }

}
