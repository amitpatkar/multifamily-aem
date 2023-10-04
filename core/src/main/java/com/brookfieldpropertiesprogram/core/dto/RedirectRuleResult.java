/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.dto;

import org.apache.sling.api.resource.Resource;

/**
 *
 * @author amitpatkar
 */
public class RedirectRuleResult {
    
    final String inputUrl;    
    final String redirectType;
    final String resourcePathToRedirectTo;

    public RedirectRuleResult(String inputUrl, String redirectType, String resourcePathToRedirectTo) {
        this.inputUrl = inputUrl;
        this.redirectType = redirectType;
        this.resourcePathToRedirectTo = resourcePathToRedirectTo;
    }

    public String getInputUrl() {
        return inputUrl;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public String getResourcePathToRedirectTo() {
        return resourcePathToRedirectTo;
    }    
    
}
