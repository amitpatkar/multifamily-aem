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
public class UnitTypeImagesRequest {

    final String propertyId;
    final String propertyName;
    final String group;

    public UnitTypeImagesRequest(String propertyId, String propertyName, String group) {
        this.propertyId = propertyId; //internal node name
        this.propertyName = propertyName; //external property title        
        this.group = group;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }
  
    public String getGroup() {
        return group;
    }
    
    public String getSearchBasePath() {
        //content/dam/common/properties/the-eugene/"
        return DamConstants.MOUNTPOINT_ASSETS + "/common/properties/" + getPropertyId();
    }

    public String[] getGalleryImagePaths() {        
        return new String[] { "hero","gallery-left","gallery-right" };        
    }

}
