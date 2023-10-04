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

import com.brookfieldpropertiesprogram.core.services.PeekToursProvider;
import com.brookfieldpropertiesprogram.core.utils.CommonConstants;
import com.drew.lang.annotations.NotNull;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
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
        selectors = {"peek"},
        extensions = "json")
@ServiceDescription("Brookfield Properties - Peek Tours Servlet")
public class PeekToursServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    transient PeekToursProvider peekTours;

    //Need to somehow inject this values from property level config
    String peekKey = "2kzcsm5wknpnem1635958894388";
    String peekAgentId = "61802a9b4524e570c9edfbc5";

    @Override
    protected void doGet(@NotNull final SlingHttpServletRequest req,
            @NotNull final SlingHttpServletResponse resp) throws ServletException, IOException {
        String unitNumber = req.getParameter("unit_number");
        String floorplanName = req.getParameter("floorplan");
        if (StringUtils.isNotEmpty(unitNumber) || StringUtils.isNotEmpty(floorplanName)) {
            JsonObject tour;
            if (StringUtils.isNotEmpty(unitNumber)) {
                tour = peekTours.getTourByNumber(new PeekToursProvider.Credentials(peekKey, peekAgentId), unitNumber);
            } else {
                tour = peekTours.getTourByFloorplan(new PeekToursProvider.Credentials(peekKey, peekAgentId), floorplanName);
            }

            if (tour == null) {
                resp.setStatus(HttpStatus.SC_NOT_FOUND);
            } else {
                resp.getWriter().write(tour.toString());
                resp.setStatus(HttpStatus.SC_OK);
            }
        } else {
            JsonArray tours = peekTours.getPeekTours(new PeekToursProvider.Credentials(peekKey, peekAgentId));
            resp.getWriter().write(tours.toString());
            resp.setStatus(HttpStatus.SC_OK);
        }
    }
}
