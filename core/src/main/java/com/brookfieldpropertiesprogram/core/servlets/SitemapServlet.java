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

import com.brookfieldpropertiesprogram.core.models.PageInfoRequestModel;
import com.brookfieldpropertiesprogram.core.utils.CommonConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.drew.lang.annotations.NotNull;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.Servlet;
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
        extensions = "xml")
@ServiceDescription("Brookfield Properties - Sitemap Servlet")
public class SitemapServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    transient Logger LOG = LoggerFactory.getLogger(SitemapServlet.class);

    @Override
    protected void doGet(@NotNull final SlingHttpServletRequest req,
            @NotNull final SlingHttpServletResponse resp) throws IOException {
        resp.setContentType("application/xml");
        resp.getWriter().write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        resp.getWriter().write("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:xhtml=\"http:www.w3.org/1999/xhtml\">");
        Map<String, String> listOfPages = new HashMap<>();
        //find the parent page and simply loop 
        PageManager pageManager = req.getResourceResolver().adaptTo(PageManager.class);
        if (pageManager != null) {
            Page theSitemapPage = pageManager.getContainingPage(req.getResource());
            PageInfoRequestModel pageInfo = req.adaptTo(PageInfoRequestModel.class);
            if (theSitemapPage != null && pageInfo != null) {                
                String parentPath = theSitemapPage.getParent().getPath();                
                Iterator<Page> itPages = theSitemapPage.getParent().listChildren(new SiteMapPageFilter(), true);
                while (itPages.hasNext()) {
                    Page aPage = itPages.next();
                    String lastMod = aPage.getLastModified().get(Calendar.YEAR) + "/" + aPage.getLastModified().get(Calendar.MONTH) + "/" + aPage.getLastModified().get(Calendar.DAY_OF_MONTH);
                    listOfPages.put(aPage.getPath().replace(parentPath, ""), lastMod);
                }
                for (String aPagePath : listOfPages.keySet()) {
                    resp.getWriter().write("<url>");
                    resp.getWriter().write("<loc>" + pageInfo.getBaseURI() + aPagePath + ".html</loc>");
                    resp.getWriter().write("<lastmod>" + listOfPages.get(aPagePath) + "</lastmod>");
                    resp.getWriter().write("<changefreq>weekly</changefreq>");
                    resp.getWriter().write("<priority>0.9</priority>");
                    resp.getWriter().write("</url>");
                }
            }
            else {
                if (theSitemapPage == null) LOG.warn ("Unable to find a page at {}",req.getResource());
                if (pageInfo == null) LOG.warn ("Unable to convert Resource to PageInfo");
            }            
        }
        else {
            LOG.warn("page Mangager is null");
        }
        resp.getWriter().write("</urlset>");
        //resp.getWriter().print(units);
        resp.setStatus(HttpStatus.SC_OK);
    }

    class SiteMapPageFilter implements com.day.cq.commons.Filter<Page> {

        @Override
        public boolean includes(Page page) {
            return !(page.getName().equalsIgnoreCase("404") || page.getName().equalsIgnoreCase("500") || page.getName().equalsIgnoreCase("sitemap"));
        }

    }
}
