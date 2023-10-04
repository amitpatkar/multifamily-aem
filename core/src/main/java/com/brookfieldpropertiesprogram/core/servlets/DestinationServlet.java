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
package com.brookfieldpropertiesprogram.core.servlets;

import com.brookfieldpropertiesprogram.core.utils.CommonConstants;
import com.day.cq.wcm.api.Page;
import java.io.IOException;
import javax.servlet.Servlet;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletName;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = {Servlet.class})
@SlingServletResourceTypes(
        resourceTypes = {                        
        		CommonConstants.SLING_SERVLET_DEFAULT},
        methods = HttpConstants.METHOD_GET,
        selectors = {"destination"},
        extensions = "json")
@SlingServletName(servletName = "Brookfield Properties Program - Destination Servlet")
@ServiceDescription("Brookfield Properties - Places Servlet")
public class DestinationServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    transient Logger LOG = LoggerFactory.getLogger(DestinationServlet.class);

    //@Reference
    //transient XSSAPI xssApi;
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Page aPage = req.getResource().adaptTo(Page.class);
        if (aPage == null) {
            return;
        }        
        String resourceType = aPage.getContentResource().getResourceType();
        if (resourceType.equals(CommonConstants.XFPAGE_STATE_NHOOD_CONFIG)) {
            //find properties associated with this neighborhood
        }
        else if (resourceType.equals(CommonConstants.XFPAGE_PROP_CONFIG)) {
            
        }
        else if (resourceType.equals(CommonConstants.XFPAGE_CITY_CONFIG)) {
            
        }
        else if (resourceType.equals(CommonConstants.XFPAGE_STATE_CONFIG)) {
            
        }                   
        String resourcePath = req.getRequestPathInfo().getResourcePath();
        resp.getWriter().write(req.getResponseContentType());
        resp.getWriter().write(resourceType);
        resp.getWriter().write(resourcePath);
        resp.setStatus(HttpStatus.SC_OK);
    }
}
