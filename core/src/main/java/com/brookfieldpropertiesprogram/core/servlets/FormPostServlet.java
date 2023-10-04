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

import com.brookfieldpropertiesprogram.core.models.GlobalConfigRequestModel;
import com.brookfieldpropertiesprogram.core.models.PageInfoRequestModel;
import com.brookfieldpropertiesprogram.core.models.PropertyConfigModel;
import com.brookfieldpropertiesprogram.core.services.ContactUsService;
import com.brookfieldpropertiesprogram.core.utils.CommonConstants;
import com.drew.lang.annotations.NotNull;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
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
        methods = HttpConstants.METHOD_POST,
        selectors = {"contactus"},
        extensions = "html")
@ServiceDescription("Brookfield Properties - Form Post Servlet")
public class FormPostServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    final transient Logger LOG = LoggerFactory.getLogger(FormPostServlet.class);
    @Reference
    transient ContactUsService contactUsService;

    @Override
    protected void doPost(@NotNull final SlingHttpServletRequest req,
            @NotNull final SlingHttpServletResponse resp) throws ServletException, IOException {
        String postRequestBody = req.getReader().lines().collect(Collectors.joining());
        PageInfoRequestModel pageInfoRequestModel;
        pageInfoRequestModel = req.adaptTo(PageInfoRequestModel.class);

        if (!StringUtils.isEmpty(postRequestBody)) {
            try {
                PropertyConfigModel model = null;
                GlobalConfigRequestModel globalConfigRequestModel = req.adaptTo(GlobalConfigRequestModel.class);
                if (pageInfoRequestModel != null) {
                    model = pageInfoRequestModel.getPropertyConfigModel();
                    contactUsService.parseJSONAndSendEmail(globalConfigRequestModel,model, postRequestBody);                    
                }
                
            } catch (LoginException ex) {
                LOG.error("FormPostServlet.doPost -> contactUsService.parseJSONAndSendEmail", ex);
            }
        } else {
            throw new IOException("Empty Post Data");
        }
    }
}
