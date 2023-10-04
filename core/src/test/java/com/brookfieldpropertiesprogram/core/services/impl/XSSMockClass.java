/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Component;

/**
 *
 * @author patkara
 */
@Component (service = XSSAPI.class)
public class XSSMockClass implements XSSAPI{

    @Override
    public Integer getValidInteger(String arg0, int arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long getValidLong(String arg0, long arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double getValidDouble(String arg0, double arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getValidDimension(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getValidHref(String arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getValidJSToken(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getValidStyleToken(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getValidCSSColor(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getValidMultiLineComment(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getValidJSON(String arg0, String arg1) {
         return arg0;
    }

    @Override
    public String getValidXML(String arg0, String arg1) {
         return arg0;
    }

    @Override
    public String encodeForHTML(String arg0) {
         return arg0;
    }

    @Override
    public String encodeForHTMLAttr(String arg0) {
         return arg0;
    }

    @Override
    public String encodeForXML(String arg0) {
         return arg0;
    }

    @Override
    public String encodeForXMLAttr(String arg0) {
         return arg0;
    }

    @Override
    public String encodeForJSString(String arg0) {
         return arg0;
    }

    @Override
    public String encodeForCSSString(String arg0) {
         return arg0;
    }

    @Override
    public String filterHTML(String arg0) {
        return arg0;
    }
   

    

   
}
