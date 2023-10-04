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

import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.brookfieldpropertiesprogram.core.constants.PathConstants;
import com.brookfieldpropertiesprogram.core.services.EnvironmentSetting;
import com.day.cq.dam.api.DamConstants;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {Resource.class})
public class NavigationLink {

    private final static Logger LOG = LoggerFactory.getLogger(NavigationLink.class);

    @Inject
    @org.apache.sling.models.annotations.Optional
    private String icon;
    @Inject
    @org.apache.sling.models.annotations.Optional
    private String label;
    @Inject
    @org.apache.sling.models.annotations.Optional
    private String id;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private String cssClass;
    @Inject
    @org.apache.sling.models.annotations.Optional
    private String url;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private String openLinkInNewTab;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private String openVirtualTour;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private String btnType;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private String btnStyle;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private List<NavigationLink> subMenuLinks;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private List<NavigationLink> attributes;

    @ResourcePath(path = "/content/siteconfig/sites/portfolio/master")
    Resource siteConfigPortfolio;

    @ResourcePath(path = "/content/siteconfig/sites/standalone")
    Resource siteConfigStandaloneRoot;

    SiteConfigModel siteConfigModel = null;

    @OSGiService
    EnvironmentSetting environmentSetting;

    boolean shouldPrefixImageService = false;
    String imageServerUrl = null;

    final Resource resource;

    public NavigationLink(Resource resource) {
        this.resource = resource;
    }

    @PostConstruct
    public void init() {
        if (environmentSetting != null
                && environmentSetting.isPublishServer()) {            
            String[] partsOfPath = resource.getPath().split("[/]");
            if (partsOfPath != null && partsOfPath.length >= 4 && resource.getPath().startsWith(PathConstants.STANDALONE_BASE)) { // /content/standalone/the-burnham/index/jcr:content
                String stSite = partsOfPath[3];
                Resource siteConfigResource = siteConfigStandaloneRoot.getChild(stSite);
                if (siteConfigResource != null) {
                    siteConfigModel = siteConfigResource.adaptTo(SiteConfigModel.class);
                }
            }            
            else if (resource.getPath().startsWith(PathConstants.PORTFOLIO_BASE)) {
                siteConfigModel = siteConfigPortfolio.adaptTo(SiteConfigModel.class);
            }
            if (siteConfigModel != null && StringUtils.isNotEmpty(siteConfigModel.getImageServiceUrl()) && StringUtils.isNotEmpty(this.url) && this.url.startsWith(DamConstants.MOUNTPOINT_ASSETS)) {
                shouldPrefixImageService = true;
            }
        }
    }

    //private String target;
    private String active;

    public String getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public String getOpenLinkInNewTab() {
        return openLinkInNewTab;
    }

    public String getOpenVirtualTour() {
        return openVirtualTour;
    }

    public String getUrl() {
        if (shouldPrefixImageService) {
            return siteConfigModel.getImageServiceUrl() + url;
        } else {
            return url;
        }
    }

    public String getAbsUrl() {
        String absUrl = url;
        if (!StringUtils.isEmpty(absUrl)) {
            if (absUrl.startsWith("/content/") && !absUrl.endsWith(".html")) {
                absUrl += ".html";
            }
        }
        return absUrl;
    }

    public String getTarget() {
        if (!StringUtils.isEmpty(openLinkInNewTab) && Boolean.valueOf(openLinkInNewTab)) {
            return "_blank";
        } else {
            return "_self";
        }
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public List<NavigationLink> getSubMenuLinks() {
        return new ArrayList(subMenuLinks);
    }

    public String getId() {
        return id;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getBtnType() {
        return btnType;
    }

    public String getBtnStyle() {
        return btnStyle;
    }

    public List<NavigationLink> getAttributes() {
        return new ArrayList(attributes);
    }

    public String getDataLayer() {
        Map<String, String> properties = new HashMap<>();

        properties.put("@type", "link");
        properties.put("name", resource.getName());
        if (StringUtils.isNotEmpty(this.getLabel())) {
            properties.put("label", this.getLabel());
        }
        if (StringUtils.isNotEmpty(this.getIcon())) {
            properties.put("icon", this.getIcon());
        }
        if (StringUtils.isNotEmpty(this.getAbsUrl())) {
            properties.put("absUrl", this.getAbsUrl());
        }
        if (StringUtils.isNotEmpty(this.getUrl())) {
            properties.put("url", this.getUrl());
        }
        if (StringUtils.isNotEmpty(this.getCssClass())) {
            properties.put("cssClass", this.getCssClass());
        }
        if (StringUtils.isNotEmpty(this.getId())) {
            properties.put("id", this.getId());
        }
        if (StringUtils.isNotEmpty(this.getBtnType())) {
            properties.put("btnType", this.getBtnType());
        }

        if (StringUtils.isNotEmpty(this.getBtnStyle())) {
            properties.put("btnStyle", this.getBtnStyle());
        }

        //Use AEM Core Component utils to get a unique identifier for the Byline component (in case multiple are on the page)
        String bylineComponentID = ComponentUtils.generateId(resource.getName(), resource.getPath());
        // Return the bylineProperties as a JSON String with a key of the bylineResource's ID

        Gson gson = new Gson();
        return String.format("{\"%s\":%s}",
                bylineComponentID,
                // Use the ObjectMapper to serialize the bylineProperties to a JSON string
                gson.toJson(properties));
    }

}
