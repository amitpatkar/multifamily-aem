/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amitpatkar
 */
public class ImageUtils {
    static final Logger LOG = LoggerFactory.getLogger(ImageUtils.class);
    public static String escape(String inStr) {
        if (StringUtils.isEmpty(inStr)) return inStr;

        return inStr.trim().replaceAll("\\s", "-").toLowerCase();
    }
    
    public static String urlEncode(String inStr) {
        if (StringUtils.isEmpty(inStr)) return inStr;
        try {
            return URLEncoder.encode(inStr, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOG.error(null,ex);
            return inStr;
        }
    }
    
    public static String escapeRegEx(String inStr) {
        return Pattern.quote(escape(inStr));
    }
    
    public static Boolean isPortFolioSite(String pagePath) {    	
    	if(pagePath.contains("portfolio")) {
    		return true;
    	}
    	return false;
    }
}
