/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.dto;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 * @author amitpatkar
 */
@ExtendWith(AemContextExtension.class)
public class UnitTypeImagesRequestTest {

    UnitTypeImagesRequest instance;

    @BeforeEach
    public void setup() throws Exception {
        instance = new UnitTypeImagesRequest("the-eugene", "The Eugene", "studio r");
        //context.request().setResource(context.currentResource());
        //context.currentPage("/content/experience-fragments/siteconfig/sites/standalone/baseline");
        //System.out.println(context.currentPage().getName());

    }

    /**
     * Test of getPropertyId method, of class UnitTypeImagesRequest.
     */
    @Test
    public void testGetPropertyId() {
        String expResult = "the-eugene";
        String result = instance.getPropertyId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPropertyName method, of class UnitTypeImagesRequest.
     */
    @Test
    public void testGetPropertyName() {
        String expResult = "The Eugene";
        String result = instance.getPropertyName();
        assertEquals(expResult, result);

    }

    /**
     * Test of getGroup method, of class UnitTypeImagesRequest.
     */
    @Test
    public void testGetGroup() {
        String expResult = "studio r";
        String result = instance.getGroup();
        assertEquals(expResult, result);

    }

    /**
     * Test of getSearchBasePath method, of class UnitTypeImagesRequest.
     */
    @Test
    public void testGetSearchBasePath() {
        String expResult = "/content/dam/common/properties/the-eugene";
        String result = instance.getSearchBasePath();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGalleryImagePaths method, of class UnitTypeImagesRequest.
     */    
    @Test
    public void testGetGalleryImagePaths() { 
        String[] expResult = new String[]{ "hero","gallery-left","gallery-right" };        
        String[] result = instance.getGalleryImagePaths();
        Assertions.assertArrayEquals(expResult, result);
    }

}
