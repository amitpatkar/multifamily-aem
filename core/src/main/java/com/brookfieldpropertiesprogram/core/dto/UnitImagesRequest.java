/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.dto;

import com.brookfieldpropertiesprogram.core.utils.ImageUtils;
import com.day.cq.dam.api.DamConstants;

/**
 *
 * @author amitpatkar
 */
public class UnitImagesRequest {

    final String propertyId;
    final String propertyName;
    final String unit;

    public UnitImagesRequest(String propertyId, String propertyName, String unit) {
        this.propertyId = propertyId; //internal node name
        this.propertyName = propertyName; //external property title        
        this.unit = unit;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }
  
    public String getUnit() {
        return unit;
    }
    
    public String getSearchBasePath() {
        //content/dam/common/properties/the-eugene/"
        return DamConstants.MOUNTPOINT_ASSETS + "/common/properties/" + getPropertyId();
    }

    public String[] getGalleryImagePaths() {        
        return new String[] { "hero","gallery-left","gallery-right"};        
    }
    
    public String[] getHeroImagePaths() {        
        return new String[] { "hero\\-" + ImageUtils.escapeRegEx(this.getUnit()) +  "\\-(.*?)\\.jpg" };        
    }

}
