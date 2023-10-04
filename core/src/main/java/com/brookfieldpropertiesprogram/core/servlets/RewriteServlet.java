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

import com.brookfieldpropertiesprogram.core.models.SiteConfigRequestModel;
import com.brookfieldpropertiesprogram.core.utils.CommonConstants;
import com.drew.lang.annotations.NotNull;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
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
        resourceTypes = {CommonConstants.COMPONENTS_PAGE, CommonConstants.SLING_SERVLET_DEFAULT},
        methods = HttpConstants.METHOD_GET,
        selectors = {"rewrite"},
        extensions = "html")
@ServiceDescription("Brookfield Properties - Sitemap Servlet")
public class RewriteServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    transient Logger LOG = LoggerFactory.getLogger(RewriteServlet.class);

    @Override
    protected void doGet(@NotNull final SlingHttpServletRequest req,
            @NotNull final SlingHttpServletResponse resp) {
        SiteConfigRequestModel model = req.adaptTo(SiteConfigRequestModel.class);
        if (model == null) {
            LOG.warn(" model is null");
            resp.setStatus(HttpStatus.SC_NOT_FOUND);
            return;
        }
        if (model.getRedirectRuleResult() == null) {
            LOG.warn("model.getRedirectRuleResult() is null");
            resp.setStatus(HttpStatus.SC_NOT_FOUND);
            return;
        }
        if (model.getRedirectRuleResult().getRedirectType() == null && model.getRedirectRuleResult() != null && model.getRedirectRuleResult().getResourcePathToRedirectTo() != null) {
            try {                              
                RequestDispatcher reqDispatcher = req.getRequestDispatcher(model.getRedirectRuleResult().getResourcePathToRedirectTo() + ".html");
                LOG.warn("reqDispatcher:{}", reqDispatcher);
                reqDispatcher.forward(req, resp);
                //resp.setStatus(HttpStatus.SC_OK);
                return;
            } catch (ServletException | IOException ex) {
                LOG.error(null,ex);
                resp.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }        
        //resp.getWriter().print(units);
        resp.setStatus(HttpStatus.SC_OK);
    }

}
