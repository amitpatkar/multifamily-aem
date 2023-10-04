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
import com.brookfieldpropertiesprogram.core.dto.RedirectRuleResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class SiteConfigRequestModel {

    static final Logger LOG = LoggerFactory.getLogger(SiteConfigRequestModel.class);

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE)
    @Default(values = "No resourceType")
    protected String resourceType;

    final SlingHttpServletRequest request;

    SiteConfigModel siteConfigModel;

    private Resource siteConfigResource;
    String contentPath;
    RedirectRuleResult redirectRuleResult;

    public SiteConfigRequestModel(SlingHttpServletRequest request) {
        this.request = request;
    }

    @PostConstruct
    protected void init() {
        //this request must have the suffix which is the source requested url
        String suffixToLookFor = request.getRequestPathInfo().getSuffix();
        if (suffixToLookFor == null) {
            LOG.warn("suffixToLookFor is Null");
            return;
        }
        siteConfigModel = request.getResource().adaptTo(SiteConfigModel.class);
        if (siteConfigModel == null) {
            LOG.warn("siteConfigModel is empty");
            return;
        }
        if (StringUtils.isEmpty(siteConfigModel.getGlobalRewriteRules())) {
            LOG.warn("globalRedirectRules is empty");
            return;
        }
        siteConfigResource = request.getResource();
        //if (siteConfigResource == null) {
        //    LOG.warn("siteConfigResource is empty");
        //    return;
        //}
        //example 1 - /content/siteconfig/sites/standalone/demo
        //example 2 -- /content/siteconfig/sites/portfolio/master for portfolio

        //content path /content/standalone/demo
        // /content/portfolio/en
        contentPath = determineContentPath();
        //now ensure this contentPath is a valid Resource
        if (StringUtils.isEmpty(contentPath)) {
            //this should not happen
            return;
        }
        LOG.info("suffixToLookFor:{}", suffixToLookFor);
        findRedirectRuleResult(siteConfigModel.getGlobalRewriteRules(), suffixToLookFor);

    }

    public boolean isStandalone() {
        return !siteConfigResource.getPath().startsWith("/content/siteconfig/sites/portfolio/"); //if doesn't start with portfolio then its a standalone        
    }

    public SlingHttpServletRequest getRequest() {
        return request;
    }

    public Resource getSiteConfigResource() {
        return siteConfigResource;
    }

    public String getContentPath() {
        return contentPath;
    }

    public RedirectRuleResult getRedirectRuleResult() {
        return redirectRuleResult;
    }

    protected String determineContentPath() {
        if (isStandalone()) {
            return siteConfigResource.getPath().replaceAll("/siteconfig/sites/standalone/", "/standalone/");
        } else {
            return siteConfigResource.getPath().replaceAll("/siteconfig/sites/portfolio/master", "/portfolio/en");
        }
    }

    protected void findRedirectRuleResult(String rulesToInspect, String sourceUrl) {
        String[] partsOfRules = rulesToInspect.split(System.lineSeparator());
        for (String aRule : partsOfRules) {
            if (StringUtils.isEmpty(aRule)) {
                continue;
            }
            String[] partIndividualRules = aRule.split("[~=]");
            if (partIndividualRules.length != 2 && partIndividualRules.length != 3) {
                continue;
            }
            String ruleRegEx = partIndividualRules[0].trim();
            String target = partIndividualRules[1].trim();
            String type = null;

            if (partIndividualRules.length == 3) {
                type = partIndividualRules[2].trim();
            }
            LOG.debug("Matching sourceUrl:{} against :{}", sourceUrl, ruleRegEx);
            if (sourceUrl.matches(ruleRegEx)) {
                String currentDestiString = determineContentPath() + "/" + target;
                //$1 is the first place holder and so on so for
                Pattern p = Pattern.compile(ruleRegEx);
                Matcher m = p.matcher(sourceUrl);
                if (m.matches() && m.groupCount() > 0) {
                    for (int i = 1; i <= m.groupCount(); i++) { //remember m.group(0) is always the entire sourceUrl if matched
                        currentDestiString = currentDestiString.replaceAll("\\$" + (i), m.group(i));
                    }
                }
                LOG.debug("Matching sourceUrl:{} against :{} Matched", sourceUrl, ruleRegEx);
                if (target.startsWith(PathConstants.COMMON_CONTENT_PAGE_NAME)) {
                    //found it
                    redirectRuleResult = new RedirectRuleResult(sourceUrl, type, currentDestiString);
                    return;
                } else {
                    Resource finalDestinationResource = request.getResourceResolver().getResource(currentDestiString);
                    if (finalDestinationResource != null) {
                        //found it
                        redirectRuleResult = new RedirectRuleResult(sourceUrl, type, currentDestiString);
                        return;
                    }
                }
            }
        }
    }
}
