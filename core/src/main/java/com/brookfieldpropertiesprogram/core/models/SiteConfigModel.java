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

import com.brookfieldpropertiesprogram.core.services.EnvironmentSetting;
import com.brookfieldpropertiesprogram.core.utils.JsonLdUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class SiteConfigModel {

    static final Logger LOG = LoggerFactory.getLogger(SiteConfigModel.class);

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE)
    @Default(values = "No resourceType")
    protected String resourceType;

    final Resource currentResource;

    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String imageServiceUrl;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String theme;

    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String activeVariation;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String footer_cta_heading;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String footer_cta_label;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String footer_cta_url;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String globalRewriteRules;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private String footer_cta_openLinkInNewTab;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String reportSuiteBaseName;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    public String rootContentPath;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private List<NavigationLink> headerMenuLinksLeft;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private List<NavigationLink> headerMenuLinksRight;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private List<NavigationLink> footerMenuLinksLeft;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private List<NavigationLink> footerMenuLinksRight;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    public String hideLogo;
    
    @OSGiService
    EnvironmentSetting environmentSetting;

    private final ValueMap valueMap;

    public SiteConfigModel(Resource resource) {
        this.currentResource = resource;
        valueMap = ( (resource != null && resource.getChild(JcrConstants.JCR_CONTENT) != null )? resource.getChild(JcrConstants.JCR_CONTENT).adaptTo(ValueMap.class) : null);
    }

    @PostConstruct
    protected void init() {
        //PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        /*
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(currentResource))
                .map(Page::getPath).orElse("");       
         */
        activeVariation = (valueMap != null ? valueMap.get("activeVariation", String.class) : null);
        imageServiceUrl = (valueMap != null && (environmentSetting != null && environmentSetting.isPublishServer()) ? valueMap.get("imageServiceUrl", String.class) : null); //set this variable if and only if the server is a publish server
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getImageServiceUrl() {
        return imageServiceUrl;
    }        

    public String getTheme() {
        return theme;
    }

    public String getActiveVariation() {
        return activeVariation;
    }

    public String getReportSuiteBaseName() {
        return reportSuiteBaseName;
    }

    public String getName() {
        return currentResource.getName();
    }

    public String getData() {
        JsonObject jsonObj = new JsonObject();
        //Analytics        
        jsonObj.addProperty("reportSuiteBaseName", StringUtils.isEmpty(reportSuiteBaseName) ? "" : reportSuiteBaseName);
        return jsonObj.toString();
    }

    /**
     * https://schema.org/Place
     *
     * @return
     */
    public JsonObject getJsonLdObject() {
        Map<String, Object> ld = new HashMap<>();

        ld.put("@context", "https://schema.org/");
        ld.put("@type", "WebSite");

        return JsonLdUtils.getJsonLd(ld);
    }

    public String getGlobalRewriteRules() {
        return globalRewriteRules;
    }

    public Resource getCurrentResource() {
        return currentResource;
    }

    public String getFooter_cta_heading() {
        return footer_cta_heading;
    }

    public String getFooter_cta_label() {
        return footer_cta_label;
    }

    public String getFooter_cta_url() {
        return footer_cta_url;
    }

    public String getFooter_cta_absUrl() {
        if (!StringUtils.isEmpty(footer_cta_url)) {
            if (footer_cta_url.startsWith("/content/") && !footer_cta_url.endsWith(".html")) {
                footer_cta_url += ".html";
            }
        }
        return footer_cta_url;
    }

    public String getFooter_cta_target() {
        if (!StringUtils.isEmpty(footer_cta_openLinkInNewTab) && Boolean.valueOf(footer_cta_openLinkInNewTab)) {
            return "_blank";
        } else {
            return "_self";
        }
    }

    public ValueMap getValueMap() {
        return valueMap;
    }

    public List<NavigationLink> getHeaderMenuLinksLeft() {
        return (headerMenuLinksLeft != null ? new ArrayList<>(headerMenuLinksLeft) : null);
    }

    public List<NavigationLink> getHeaderMenuLinksRight() {
        return (headerMenuLinksRight != null ? new ArrayList<>(headerMenuLinksRight) : null);
    }

    public List<NavigationLink> getFooterMenuLinksLeft() {
        return (footerMenuLinksLeft != null ? new ArrayList<>(footerMenuLinksLeft) : null);
    }

    public List<NavigationLink> getFooterMenuLinksRight() {
        return (footerMenuLinksRight != null ? new ArrayList<>(footerMenuLinksRight) : null);
    }

}
