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

import com.brookfieldpropertiesprogram.core.services.PropertyListExternalService;
import com.brookfieldpropertiesprogram.core.utils.CommonConstants;
import com.drew.lang.annotations.NotNull;
import com.google.gson.JsonArray;
import java.io.IOException;
import javax.servlet.Servlet;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
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
        resourceTypes = {CommonConstants.COMPONENTS_PAGE, CommonConstants.SLING_SERVLET_DEFAULT},
        methods = HttpConstants.METHOD_GET,
        selectors = {"availability"},
        extensions = "json")
@ServiceDescription("Brookfield Properties - Availability Servlet")
public class AvailabilityServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    transient Logger LOG = LoggerFactory.getLogger(AvailabilityServlet.class);

    @Reference
    transient PropertyListExternalService propertyListExternalService;

    @Override
    protected void doGet(@NotNull final SlingHttpServletRequest req,
                         @NotNull final SlingHttpServletResponse resp) throws IOException {

        String[] oneSiteIDs = req.getParameterMap().get("oneSiteID");
        if (oneSiteIDs == null || oneSiteIDs.length == 0) {
            resp.getWriter().print("oneSiteID query parameter is required");
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            return;
        }

        JsonArray units = propertyListExternalService.searchAvailabilityByOneSiteID(oneSiteIDs);

        resp.setContentType("application/json");
        resp.getWriter().print(units);
        resp.setStatus(HttpStatus.SC_OK);
    }
}
