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
import com.day.cq.wcm.api.Page;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class UrlInfoRequestModel {

    static final Logger LOG = LoggerFactory.getLogger(UrlInfoRequestModel.class);

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE)
    @Default(values = "No resourceType")
    protected String resourceType;

    final SlingHttpServletRequest request;

    String aptAvailability;
    String aptType;
    String apt;
    String propertyStandaloneBase = "/content/portfolio/en/properties";
    
    
    String standaloneBase = "/content/standalone";
    String portfolioBase = "/content/portfolio/en/";
    String standaloneBaseMapped;
    String portfolioBaseMapped;

    String aptAvailabilityMapped;
    String aptTypeMapped;
    String aptMapped;
    String propertyStandaloneBaseMapped;

    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Page currentPage;

    public UrlInfoRequestModel(SlingHttpServletRequest request) {
        this.request = request;
    }

    @PostConstruct
    protected void init() {
        //this.currentPage = resource.adaptTo(Page.class);
        if (currentPage == null) {
            //lets try to find the page here
            currentPage = request.getResource().adaptTo(Page.class);
            if (currentPage == null) {
                LOG.warn("Unable to find the current page from resource path:{}", request.getResource().getPath());
                return;
            }
        }

        //if standalone then apartment-availability , apartment-type and apartment will be sibling of the current page
        //or else for portfolio they will be children of a property, collection or neighbborhood
        Page theBase = currentPage;
        if (isStandalone() || currentPage.getName().equals(PathConstants.COMMON_CONTENT_PAGE_NAME)) {
            theBase = currentPage.getParent();
        }
        //now we have the base lets check for apartment-availability
        if (theBase.hasChild(PathConstants.PAGE_APT_AVAILABILITY)) {
            aptAvailability = theBase.getPath() + "/" + PathConstants.PAGE_APT_AVAILABILITY + ".html";
        } else {
            aptAvailability = theBase.getPath() + "/" + PathConstants.COMMON_CONTENT_PAGE_NAME + "." + PathConstants.PAGE_APT_AVAILABILITY + ".html";
        }

        if (theBase.hasChild(PathConstants.PAGE_APT_TYPE)) {
            aptType = theBase.getPath() + "/" + PathConstants.PAGE_APT_TYPE + ".html";
        } else {
            aptType = theBase.getPath() + "/" + PathConstants.COMMON_CONTENT_PAGE_NAME + "." + PathConstants.PAGE_APT_TYPE + ".html";
        }

        if (theBase.hasChild(PathConstants.PAGE_APT)) {
            apt = theBase.getPath() + "/" + PathConstants.PAGE_APT + ".html";
        } else {
            apt = theBase.getPath() + "/" + PathConstants.COMMON_CONTENT_PAGE_NAME + "." + PathConstants.PAGE_APT + ".html";
        }
        
        

        //now find the map
        aptAvailabilityMapped = request.getResourceResolver().map(aptAvailability);
        aptTypeMapped = request.getResourceResolver().map(aptType);
        aptMapped = request.getResourceResolver().map(apt);        
        propertyStandaloneBaseMapped = request.getResourceResolver().map(propertyStandaloneBase);
        
        standaloneBaseMapped = request.getResourceResolver().map(standaloneBase);
        portfolioBaseMapped = request.getResourceResolver().map(portfolioBase);
    }

    public boolean isStandalone() {
        return currentPage.getPath().startsWith(PathConstants.STANDALONE_BASE); //if doesn't start with portfolio then its a standalone        
    }

    public SlingHttpServletRequest getRequest() {
        return request;
    }

    public String getAptAvailability() {
        return aptAvailability;
    }

    public String getAptType() {
        return aptType;
    }

    public String getApt() {
        return apt;
    }

    public String getPropertyStandaloneBase() {
        return propertyStandaloneBase;
    }
    
    public String getAptAvailabilityMapped() {
        return aptAvailabilityMapped;
    }

    public String getAptTypeMapped() {
        return aptTypeMapped;
    }

    public String getAptMapped() {
        return aptMapped;
    }

    public String getPropertyStandaloneBaseMapped() {
        return propertyStandaloneBaseMapped;
    }

    public String getStandaloneBaseMapped() {
        return standaloneBaseMapped;
    }

    public String getPortfolioBaseMapped() {
        return portfolioBaseMapped;
    }        

}
