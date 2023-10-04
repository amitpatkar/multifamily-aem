/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amitpatkar
 */
@Component (property=Constants.SERVICE_RANKING+":Integer=800")
@SlingServletFilter(
            scope = {SlingServletFilterScope.REQUEST},
                       extensions = {"html"},
                       pattern="/content/(.*)",  
                       selectors = {"contactus"},
                       methods = {HttpConstants.METHOD_POST})
public class FormPostRequestFilter implements Filter {
    static final Logger LOG = LoggerFactory.getLogger(FormPostRequestFilter.class);
       @Override
       public void init(FilterConfig filterConfig) throws ServletException {
            //LOG.info("Init");
       }

       @Override
       public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {           
           MultiReadRequestWrapper multiReadRequestWrapper = new MultiReadRequestWrapper((SlingHttpServletRequest)request);
           //will only be run on (GET|HEAD) /content/.*.foo|bar.txt|json/suffix/foo requests
           //code here can be reduced to what should actually be done in that case
           //for other requests, this filter will not be in the call stack
           chain.doFilter(multiReadRequestWrapper, response);
       }

       @Override
       public void destroy() {

       }
}
